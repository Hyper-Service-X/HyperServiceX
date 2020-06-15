package com.hsx.sa.spring.integration.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.stereotype.Component;

@Component("processorStatusErrorChannelService")
public class ProcessorStatusErrorChannelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorStatusErrorChannelService.class);

    @ServiceActivator(inputChannel = "processorStatusErrorChannel")
    void handleCreditTransferException(Message<MessageHandlingException> msg) {
        //The payload should be exception here
        MessageHandlingException ex = msg.getPayload();
        LOGGER.error("Exception Occurred at CT SI: {}. {}", ex.getFailedMessage(), ex.getMessage());
    }
}