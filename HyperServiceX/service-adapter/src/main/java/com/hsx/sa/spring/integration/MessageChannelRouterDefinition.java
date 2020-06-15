package com.hsx.sa.spring.integration;

import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.exception.HSXMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Created by
 *
 * @author Nadith on 10/5/2020
 * Modified by
 * @author ... on ... : Modified Reason is ...
 * Usage :
 * This class will define the routing in SI based on {@link Header}.
 */

@Component
public class MessageChannelRouterDefinition {

    @Autowired
    MessageChannel processorStatusMessageChannel;

    @Router(inputChannel = "operationalMessageChannel")
    public MessageChannel operationalMessageRouter(@Header("MESSAGE_TYPE") MessageType messageType) throws HSXMessageException {
        MessageChannel messageChannel;
        switch (messageType) {
            case PROCESSOR_STATUS:
                messageChannel = processorStatusMessageChannel;
                break;
            default:
                throw new HSXMessageException("Message channel is not configured for " + messageType.name());
        }
        return messageChannel;
    }
}
