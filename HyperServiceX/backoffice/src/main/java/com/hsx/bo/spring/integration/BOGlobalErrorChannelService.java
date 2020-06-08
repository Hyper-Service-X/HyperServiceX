package com.hsx.bo.spring.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


@Component("boGlobalErrorChannelService")
public class BOGlobalErrorChannelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BOGlobalErrorChannelService.class);

    @ServiceActivator(inputChannel = "errorChannel")
    public void handleException(Message<Exception> msg) {
        LOGGER.error("Exception Occurred at SI: {}. {}", msg.getHeaders().toString(), msg.getPayload().toString());
    }
}