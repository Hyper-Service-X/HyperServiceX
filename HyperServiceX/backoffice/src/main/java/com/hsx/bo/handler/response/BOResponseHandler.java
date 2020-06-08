package com.hsx.bo.handler.response;

import com.bcs.xborder.common.util.messaging.MessageOperation;
import com.bcs.xborder.common.util.util.JSONUtil;
import com.hsx.common.model.constants.ErrorCode;
import com.hsx.common.model.constants.SubMessageType;
import com.hsx.common.model.exception.HSXException;
import com.hsx.common.model.response.HSXMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public abstract class BOResponseHandler<I, O extends HSXMessage> {

    Logger LOGGER = LoggerFactory.getLogger(BOResponseHandler.class);

    @Autowired
    private TaskExecutor boRSTaskExecutor;

    @Value("${NODE.SITE.NO}")
    private int CURRENT_NODE_SITE_NO;

    @Value("${NODE.NO}")
    private int CURRENT_NODE_NO;

    protected com.hsx.common.model.constants.MessageType MessageType;

    protected List<SubMessageType> subMessageTypes;

    protected MessageOperation messageOperation;

    private TaskExecutor taskExecutor;

    @PostConstruct
    protected void initConfig() {
        MessageType = setMessageType();
        messageOperation = setMessageOperation();
        subMessageTypes = setBORequestTypes();
        taskExecutor = setTaskExecutor();
    }

    protected MessageOperation setMessageOperation() {
        return null;
    }

    abstract List<SubMessageType> setBORequestTypes();

    protected TaskExecutor setTaskExecutor() {
        return boRSTaskExecutor;
    }

    protected com.hsx.common.model.constants.MessageType setMessageType() {
        return null;
    }

    public List<SubMessageType> getSubMessageTypes() {
        return subMessageTypes;
    }

    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public HSXMessage resolveResponse(String stringResponse) throws HSXException {
        if (stringResponse != null) {
            try {
                return JSONUtil.convertToObject(stringResponse, HSXMessage.class);
            } catch (Exception e) {
                throw new HSXException(ErrorCode.ERROR_9999.getValue(), "Can not Resolve payload received payload to " + HSXMessage.class.getName());
            }
        }
        return null;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, value = "xbTransactionManager")
    public void updateBOResponse(HSXMessage res) throws HSXException {
    }

    protected void onSuccessResponse(HSXMessage boResponse, Object detail) throws HSXException {
        try {
        } catch (Exception e) {
            LOGGER.error("Error occurred while on default on success response  " + detail + " " + e);
            throw new HSXException(ErrorCode.ERROR_9999.getValue(), "Error occurred while on default on success response  " + detail + " " + e);
        }
    }

    protected void onPartialSuccessResponse(HSXMessage boResponse, Object detail) throws HSXException {
        try {
        } catch (Exception e) {
            LOGGER.error("Error occurred while on default on partial success response  " + detail + " " + e);
            throw new HSXException(ErrorCode.ERROR_9999.getValue(), "Error occurred while on default on partial success response  " + detail + " " + e);
        }
    }

    protected void onErrorResponse(HSXMessage boResponse, Object detail) throws HSXException {
        try {
        } catch (Exception e) {
            LOGGER.error("Error occurred while on default on error response  " + detail + " " + e);
            throw new HSXException(ErrorCode.ERROR_9999.getValue(), "Error occurred while on default on error response  " + detail + " " + e);
        }
    }
}

