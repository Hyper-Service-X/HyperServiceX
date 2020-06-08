package com.hsx.bo.service.impl;

import com.hsx.bo.handler.request.BORequestHandler;
import com.hsx.bo.service.AbstractService;
import com.hsx.common.model.constants.Constants;
import com.hsx.common.model.constants.ErrorCode;
import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.constants.SubMessageType;
import com.hsx.common.model.error.Status;
import com.hsx.common.model.exception.HSXException;
import com.hsx.common.model.request.BORequest;
import com.hsx.common.model.request.BORequestCriteria;
import com.hsx.common.model.response.HSXApiResponse;
import com.hsx.common.model.response.HSXMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("bORequestService")
public class BORequestServiceImpl extends AbstractService<Void, BORequest> {

    Logger LOGGER = LoggerFactory.getLogger(BORequestServiceImpl.class);

    private BORequestHandler[] boRequestHandlers = null;

    @PostConstruct
    public void initializeHandlers() {
        boRequestHandlers = new BORequestHandler[]{
        };
    }

    @Value("${NODE.SITE.NO}")
    private int CURRENT_NODE_SITE_NO;

    @Value("${NODE.NO}")
    private int CURRENT_NODE_NO;

    @Value("${BO.CALL.TIMEOUT_SEC}")
    private int TIME_OUT_SEC;

    @Override
    @ServiceActivator(inputChannel = "boRQChannel")
    protected Message<Void> executeService(Message<BORequest> boRequestMessage) {
        MessageType MessageType = boRequestMessage.getHeaders().get(Constants.MessageHeaders.MESSAGE_TYPE.name(), com.hsx.common.model.constants.MessageType.class);
        SubMessageType requestType = boRequestMessage.getPayload().getHeader().getSubMessageType();
        BORequestCriteria boRequestCriteria = boRequestMessage.getPayload().getRequestData();
        setPayloadData(boRequestMessage, (long) boRequestMessage.getHeaders().get(Constants.MessageHeaders.RECEIVED_TIME.name()));
        try {
            BORequestHandler boRequestHandler = selectHandler(requestType);
            if (boRequestHandler != null) {
                boRequestHandler.getTaskExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            boRequestHandler.validate();
                            if (boRequestHandler.isEnablePersistent()) {
                                boRequestHandler.persistRequest(boRequestCriteria, boRequestMessage.getPayload().getHeader().getSubMessageType());
                            }
                            if (boRequestHandler.isInternalProcessing()) {
                                HSXMessage response = boRequestHandler.processInternal(boRequestMessage.getPayload());
                                boRequestHandler.sendResponseToBOUser(boRequestMessage.getPayload(), response);
                            } else {
                                boRequestHandler.sendExternal(boRequestMessage.getPayload(), MessageType);
                                Status status = boRequestHandler.waitForResponses(boRequestMessage.getPayload().getHeader().getTransactionId());
                                HSXApiResponse boUserResponses = boRequestHandler.assembleResponseFromDB(boRequestMessage.getPayload().getHeader().getTransactionId());
                                boRequestHandler.sendResponseToBOUser(boRequestMessage.getPayload(), boUserResponses, status);
                            }

                        } catch (Exception e) {
                            LOGGER.error("Error while processing bo request by {} request handler | JobId {}", boRequestHandler.getClass().getName(), boRequestMessage.getPayload().getHeader().getTransactionId(), e);
                        }
                    }
                });
            } else {
                throw new HSXException(ErrorCode.ERROR_9999.getValue(), "Corresponding Handler Can not be found", null);
            }
        } catch (Exception e) {
            LOGGER.error("Error while processing bo request | JobId {}", boRequestMessage.getPayload().getHeader().getTransactionId(), e);
        }
        return null;
    }

    private BORequestHandler selectHandler(SubMessageType requestType) {
        for (BORequestHandler boRequestHandler : this.boRequestHandlers) {
            if (boRequestHandler.getSubMessageTypes().contains(requestType)) {
                return boRequestHandler;
            }
        }
        return null;
    }


    private void setPayloadData(Message<BORequest> msg, long startTime) {
        msg.getPayload().getHeader().setMsgReceivedTime(String.valueOf(startTime));
        msg.getPayload().getHeader().setMsgEntryPoint("BAO0" + CURRENT_NODE_SITE_NO + "0" + CURRENT_NODE_NO);
    }
}
