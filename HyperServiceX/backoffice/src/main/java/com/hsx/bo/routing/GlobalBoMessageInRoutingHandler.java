package com.hsx.bo.routing;

import com.hsx.common.util.messaging.MessageOperation;
import com.hsx.bo.spring.integration.BoMessageRoutingOutGateway;
import com.hsx.common.model.constants.Constants;
import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.exception.HSXException;
import com.hsx.common.model.request.BORequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class GlobalBoMessageInRoutingHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalBoMessageInRoutingHandler.class);

    @Autowired
    BoMessageRoutingOutGateway boMessageRoutingOutGateway;

    public void handleRequest(BORequest boRequest, long startTime, MessageType MessageType, MessageOperation operation) throws HSXException {
        Message<BORequest> fastMessage = MessageBuilder
                .withPayload(boRequest)
                .setHeader(Constants.MessageHeaders.RECEIVED_TIME.name(), startTime)
                .setHeader(Constants.MessageHeaders.MESSAGE_TYPE.name(), MessageType)
                .build();
        boMessageRoutingOutGateway.send(fastMessage);

    }
}
