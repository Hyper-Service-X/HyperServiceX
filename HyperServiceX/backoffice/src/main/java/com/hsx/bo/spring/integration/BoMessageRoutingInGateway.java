package com.hsx.bo.spring.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@MessagingGateway(
        name = "boMessageRoutingInGateway",
        defaultRequestChannel = "messageRoutingChannel")
@Component
public interface BoMessageRoutingInGateway {

    @Gateway
    void send(Message<String> xbInputMessage);

    //if we need a request-reply channel

    @Gateway
    Message<Boolean> sendAndAck(Message<?> inputMessage);

    //if we need a event publish-subscribe channel
    @Gateway
    void publishEvent(Message<?> inputMessage);
}

