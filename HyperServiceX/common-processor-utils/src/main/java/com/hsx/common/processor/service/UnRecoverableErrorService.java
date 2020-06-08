package com.hsx.common.processor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("unRecoverableErrorProcessingService")
public class UnRecoverableErrorService extends AbstractService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnRecoverableErrorService.class);

    @Override
    protected Message executeService(Message msg) throws InterruptedException {
        //The payload should be exception here
        LOGGER.info("Handling UnRecoverable Error");
        if (msg != null && msg.getPayload() != null) {
            LOGGER.info("PAyload is :" + msg.getPayload());
        } else {
            LOGGER.info("Mesge is :" + msg.getHeaders());
            LOGGER.info("Mesge is to :" + msg.toString());
            LOGGER.info("Payload is null");
        }
        return null;
    }
}
