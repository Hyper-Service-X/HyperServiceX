package com.hsx.sa.service.impl;

import com.hsx.common.model.exception.HSXServiceException;
import com.hsx.common.model.response.HSXMessage;
import com.hsx.sa.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("requestSchemaValidationServiceImpl")
public class RequestSchemaValidationServiceImpl extends AbstractService<HSXMessage, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestSchemaValidationServiceImpl.class);

    @Override
    public Message<HSXMessage> executeService(Message<String> msg) throws HSXServiceException, InterruptedException {
        sTime = System.nanoTime();
        return null;
    }
}
