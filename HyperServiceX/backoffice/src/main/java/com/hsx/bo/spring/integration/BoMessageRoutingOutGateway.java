package com.hsx.bo.spring.integration;

import com.hsx.common.model.request.BORequest;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@MessagingGateway(
        name = "boMessageRoutingOutGateway",
        defaultRequestChannel = "messageRoutingChannel")
@Component
public interface BoMessageRoutingOutGateway {

    @Gateway
    void send(Message<BORequest> xbInputMessage);

    //if we need a request-reply channel

    @Gateway
    Message<Boolean> sendAndAck(Message<?> inputMessage);

    //if we need a event publish-subscribe channel
    @Gateway
    void publishEvent(Message<?> inputMessage);
}

