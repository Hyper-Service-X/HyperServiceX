package com.hsx.sa.rest.httpErrorHandler;

import com.bcs.xborder.common.util.messaging.MessageProducerService;
import com.bcs.xborder.common.util.messaging.MessageSite;
import com.bcs.xborder.common.util.messaging.MessageTopic;
import com.bcs.xborder.common.util.messaging.MessageTopicsUtil;
import com.hsx.common.model.constants.Constants;
import com.hsx.common.model.constants.Formats;
import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.error.ErrorLog;
import com.hsx.common.model.response.HSXMessage;
import com.hsx.sa.service.AbstractService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("globalHttpStatusErrorHandler")
public class GlobalHttpStatusErrorHandler extends AbstractService<Void, HSXMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalHttpStatusErrorHandler.class);

    @Autowired
    private MessageTopicsUtil messagingTopicsUtil;

    @Autowired
    private MessageProducerService<HSXMessage> restCallAckMessageProducerService;

    @Override
    public Message<Void> executeService(Message<HSXMessage> msg) {
        sTime = System.nanoTime();
        MessageType messageType = msg.getHeaders().get(Constants.MessageHeaders.MESSAGE_TYPE.name(), com.hsx.common.model.constants.MessageType.class);
        try {
            restCallAckMessageProducerService.publishPersistedMessage(
                    getTopicName(messageType, ""),
                    msg.getPayload()
            );
        } catch (Exception ex) {
            LOGGER.error(Formats.ERROR_OCCURRED_AT___LOG_DETAIL___ERROR_DETAIL___STACKTRACE___,
                    ex.getStackTrace()[0].getMethodName(),
                    new ErrorLog.Builder("Global Http Status Error Handler")
                            .addPayload(msg.toString())
                            .build(),
                    ex.getMessage(),
                    ExceptionUtils.getStackTrace(ex)
            );
        }

        return null;
    }


    private MessageTopic getTopicName(MessageType messageType, String endPointCode) {
        return messagingTopicsUtil.getTopic(
                messageType, MessageSite.DEFAULT, "");
    }

}
