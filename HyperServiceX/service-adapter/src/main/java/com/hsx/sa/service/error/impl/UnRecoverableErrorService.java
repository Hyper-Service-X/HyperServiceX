package com.hsx.sa.service.error.impl;

import com.hsx.common.model.exception.HSXServiceException;
import com.hsx.sa.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


@Component("unRecoverableErrorProcessingService")
public class UnRecoverableErrorService extends AbstractService<Void, Exception> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnRecoverableErrorService.class);

    @Override
    public Message<Void> executeService(Message<Exception> msg) throws HSXServiceException, InterruptedException {
        //The payload should be exception here
        LOGGER.error(msg.getPayload().getMessage(), msg.getPayload());
        return null;
    }
}
