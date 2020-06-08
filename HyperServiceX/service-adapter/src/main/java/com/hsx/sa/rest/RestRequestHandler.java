package com.hsx.sa.rest;

import com.hsx.common.model.constants.Constants;
import com.hsx.common.model.exception.HSXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/*
This file is created by Nadith
on 11/3/2020-11:01 AM     
*/
@Service
public class RestRequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestRequestHandler.class);

    @Value("${NODE.SITE.NO}")
    private int CURRENT_SITE_NO;

    @Autowired
    private MessageChannel schemaValidationChannel;


    public void handleRequest(String payload, long startTime, String transactionId) throws HSXException {
        try {
            Message<String> fastMessage = MessageBuilder
                    .withPayload(payload)
                    .setHeader(Constants.MessageHeaders.RECEIVED_TIME.name(), startTime)
                    .build();
            schemaValidationChannel.send(fastMessage);
        } catch (Exception e) {
            LOGGER.error("Error Occurred while  forwarding the message into schemaValidationChannel: {}", payload);
            throw new HSXException("Error Occurred while forwarding the message into schemaValidationChannel: " + payload);
        }
    }
}
