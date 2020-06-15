package com.hsx.sa.spring.integration.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("saGlobalErrorChannelService")
public class SAGlobalErrorChannelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SAGlobalErrorChannelService.class);

    @ServiceActivator(inputChannel = "errorChannel")
    public void handleException(Message<Exception> msg) {
        //The payload should be exception here
        LOGGER.error("Exception Occurred at SI: {}. {}", msg.getHeaders().toString(), msg.getPayload().toString());
    }
}
