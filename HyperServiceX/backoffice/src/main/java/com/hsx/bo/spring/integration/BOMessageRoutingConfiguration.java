package com.hsx.bo.spring.integration;

import com.hsx.common.model.constants.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class BOMessageRoutingConfiguration {

    @Autowired
    BoMessageRoutingOutGateway boMessageRoutingOutGateway;

    @Autowired
    MessageChannel boRSChannel;

    @Autowired
    MessageChannel boRQChannel;

    @Router(inputChannel = "messageRoutingChannel")
    public MessageChannel messageRoutingBasedOnHeader(@Header("MESSAGE_TYPE") MessageType MessageType) {
        MessageChannel messageChannel = null;
        switch (MessageType) {
            default:
                messageChannel = boRQChannel;
                break;

        }
        return messageChannel;
    }
}

