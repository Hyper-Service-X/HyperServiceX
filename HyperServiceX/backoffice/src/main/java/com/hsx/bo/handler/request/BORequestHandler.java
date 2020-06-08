package com.hsx.bo.handler.request;

import com.bcs.xborder.common.util.messaging.*;
import com.bcs.xborder.common.util.validators.Validator;
import com.bcs.xborder.common.util.validators.ValidatorService;
import com.hsx.bo.assembler.ResultAssembler;
import com.hsx.bo.messaging.solace.SolaceMessageProducerService;
import com.hsx.common.model.RequestsHolder;
import com.hsx.common.model.constants.SubMessageType;
import com.hsx.common.model.error.Status;
import com.hsx.common.model.exception.HSXException;
import com.hsx.common.model.request.BORequest;
import com.hsx.common.model.request.BORequestCriteria;
import com.hsx.common.model.response.HSXApiResponse;
import com.hsx.common.model.response.HSXMessage;
import com.hsx.common.model.util.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

import static com.bcs.xborder.common.util.messaging.MessageDirection.REQ;

@Component
public abstract class BORequestHandler<I extends BORequestCriteria, O extends HSXMessage> {

    Logger LOGGER = LoggerFactory.getLogger(BORequestHandler.class);

    @Autowired
    ValidatorService validatorService;

    @Autowired
    private TaskExecutor boRQTaskExecutor;

    @Autowired
    private SolaceMessageProducerService<BORequest> messageProducerService;

    @Autowired
    private MessageTopicsUtil messagingTopicsUtil;

    @Value("${NODE.SITE.NO}")
    private int CURRENT_NODE_SITE_NO;

    @Value("${NODE.NO}")
    private int CURRENT_NODE_NO;

    @Value("${BO.CALL.TIMEOUT_SEC}")
    protected int TIME_OUT_SEC;

    protected com.hsx.common.model.constants.MessageType MessageType;

    protected List<SubMessageType> subMessageTypes;

    protected MessageOperation messageOperation;

    private int boRequestTimeOut;

    private TaskExecutor taskExecutor;

    private MessageSite.Collections messageSiteCollection;

    private boolean enablePersistent;

    private boolean enableInternalProcessing;

    @PostConstruct
    protected void initConfig() {
        enablePersistent = enableRequestPersistent();
        enableInternalProcessing = enableInternalProcessing();
        messageOperation = setMessageOperation();
        boRequestTimeOut = setBORequestTimeOut();
        subMessageTypes = setBORequestTypes();
        taskExecutor = setTaskExecutor();
        //default to ALL
        if (setMessageSiteCollection() != null) {
            messageSiteCollection = setMessageSiteCollection();
        }
        MessageType = setMessageType();
    }

    protected MessageOperation setMessageOperation() {
        return null;
    }

    abstract List<SubMessageType> setBORequestTypes();

    protected int setBORequestTimeOut() {
        return TIME_OUT_SEC;
    }

    protected MessageSite.Collections setMessageSiteCollection() {
        return MessageSite.Collections.ALL;
    }

    protected abstract com.hsx.common.model.Status.Resubmission setResubmission();

    protected TaskExecutor setTaskExecutor() {
        return boRQTaskExecutor;
    }

    com.hsx.common.model.constants.MessageType setMessageType() {
        //will be set in the run time
        return null;
    }

    protected boolean enableRequestPersistent() {
        return true;
    }

    protected boolean enableInternalProcessing() {
        return false;
    }


    public O persistAdditionalXbBoOperationParameters(I criteria, HashMap<String, String> params) throws HSXException {
        return null;
    }

    public O customizeBOUserResponse(HSXApiResponse boUserResponse) {
        return null;
    }

    public Validator[] validators(Validator... validators) {
        return null;
    }

    public List<SubMessageType> getSubMessageTypes() {
        return subMessageTypes;
    }

    public void validate() throws HSXException {
        validatorService.validate(validators());
    }

    public void sendExternal(BORequest payloadToGW, com.hsx.common.model.constants.MessageType MessageType) {
        I in = (I) payloadToGW.getRequestData();
        sendMessage(payloadToGW, MessageType);
    }


    private void sendMessage(BORequest payloadToGW, com.hsx.common.model.constants.MessageType MessageType) {
        messageProducerService.publishPersistedMessage(
                getTopicName(MessageOperation.INTERNAL_SYNC, payloadToGW.getHeader().getMsgEntryPoint(), MessageType),
                payloadToGW,
                payloadToGW.getHeader().getTransactionId(),
                new MessageProducerCallback() {
                    @Override
                    public void handleError(String topic, int siteNo, String messageId, MessagingException e, long timeStamp) {
                        LOGGER.warn("Error Occurred While Sending {} From {} ", BORequest.class.getName(), this.getClass().getName(), CURRENT_NODE_SITE_NO);
                    }

                    @Override
                    public void success(String topic, int siteNo, String messageId) {
                        LOGGER.warn("SUCCESS Sending {} From {} ", BORequest.class.getName(), this.getClass().getName(), CURRENT_NODE_SITE_NO);
                    }
                }
        );
    }

    private MessageTopic getTopicName(MessageOperation operation, String entryPoint, com.hsx.common.model.constants.MessageType messageType) {
        //then corresponding processor will listen to the echos published to that topic
        return messagingTopicsUtil.getTopic(messageType, MessageSite.DEFAULT, operation, REQ, entryPoint);
    }

    public Status waitForResponses(String jobId) {
        Status status = null;
        return status;
    }

    public HSXApiResponse assembleResponseFromDB(String jobId) {
        HSXApiResponse response = new HSXApiResponse();
        return response;
    }

    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void persistRequest(I boRequestPayload, SubMessageType subMessageType) throws HSXException {

    }

    public boolean isEnablePersistent() {
        return enablePersistent;
    }

    public boolean isInternalProcessing() {
        return enableInternalProcessing;
    }

    public O processInternal(BORequest<I> boRequest) {
        return null;
    }

    private void createParams(I boRequestPayload, List<Object> paramList) throws HSXException {
        HashMap<String, String> params = new HashMap<>();
        persistAdditionalXbBoOperationParameters(boRequestPayload, params);
    }


    private void persist(String jobId, List<Object> paramList, SubMessageType subMessageType) {
    }

    public void sendResponseToBOUser(BORequest boRequest, HSXApiResponse response, Status status) {
        HSXApiResponse<Resource> pack = null;
        response.setStatus(status);
        customizeBOUserResponse(pack);
        ResponseEntity responseEntity = NetworkUtils.wrap(pack);
        sendAsyncResponse(boRequest, responseEntity);
    }

    public void sendResponseToBOUser(BORequest boRequest, HSXMessage hsxMessage) {
        HSXApiResponse<Resource> pack = null;
        pack = ResultAssembler.assemblingResources(hsxMessage);
        customizeBOUserResponse(pack);
        ResponseEntity responseEntity = NetworkUtils.wrap(pack);
        sendAsyncResponse(boRequest, responseEntity);
    }

    private void sendAsyncResponse(BORequest boRequest, ResponseEntity responseEntity) {
        if (RequestsHolder.getXbRestRequestMap().containsKey(boRequest.getHeader().getTransactionId())) {
            DeferredResult<ResponseEntity<String>> deferredResult = RequestsHolder.getXbRestRequestMap().get(boRequest.getHeader().getTransactionId());
            deferredResult.setResult(responseEntity);
        } else {
            LOGGER.info("Request has already timeout. jobID: {}", boRequest.getHeader().getTransactionId());
        }
    }


}
