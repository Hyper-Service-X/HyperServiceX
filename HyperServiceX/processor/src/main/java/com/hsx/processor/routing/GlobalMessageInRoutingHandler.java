package com.hsx.processor.routing;

import com.bcs.xborder.common.util.messaging.MessageOperation;
import com.hsx.common.model.constants.Constants;
import com.hsx.common.model.constants.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component("globalMessageInRoutingHandler")
public class GlobalMessageInRoutingHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalMessageInRoutingHandler.class);

    public void process(MessageType messageType, MessageOperation operation, String message, long timeStamp, String messageEntryNode, String... customValues) throws Exception {

        LOGGER.debug("Message Received. Message type {} timestamp{} message {}", message, timeStamp, message);

        Message<String> fastOutMessage = MessageBuilder
                .withPayload(message)
                .setHeader(Constants.MessageHeaders.RECEIVED_TIME.name(), timeStamp)
                .setHeader(Constants.MessageHeaders.CUSTOM_VALUES.name(), customValues)
                .setHeader(Constants.MessageHeaders.MESSAGE_TYPE.name(), messageType)
                .setHeader(Constants.MessageHeaders.MESSAGE_OPERATION.name(), operation)
                .setHeader(Constants.MessageHeaders.MA_ENTRY_POINT.name(), messageEntryNode)
                .setHeader(Constants.MessageHeaders.MESSAGE_TYPE.name(), messageType)
                .build();

        switch (messageType) {
            default:
                throw new Exception("Invalid messageType");
        }
    }
}
