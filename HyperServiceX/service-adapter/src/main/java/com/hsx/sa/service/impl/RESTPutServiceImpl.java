package com.hsx.sa.service.impl;

import com.hsx.common.model.RequestsHolder;
import com.hsx.common.model.constants.Constants;
import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.exception.HSXServiceException;
import com.hsx.sa.rest.RestResponseHandler;
import com.hsx.sa.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component("RESTPutServiceImpl")
public class RESTPutServiceImpl extends AbstractService<Void, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RESTPutServiceImpl.class);

    @Autowired
    RestResponseHandler restResponseHandler;

    @Value("${NODE.SITE.NO}")
    private int CURRENT_NODE_SITE_NO;

    @Value("${NODE.NAME}")
    private String NODE_NAME;

    @Override
    public Message<Void> executeService(Message<String> msg) throws HSXServiceException, InterruptedException {
        sTime = System.nanoTime();
        String payload = msg.getPayload();
        MessageType MessageType = (com.hsx.common.model.constants.MessageType) msg.getHeaders().get(Constants.MessageHeaders.MESSAGE_TYPE.name());
        String uriPath = null;
        ResponseEntity responseEntity = null;
        try {
            switch (MessageType) {
                default:
                    if (RequestsHolder.getXbRestRequestMap().containsKey("")) {
                        DeferredResult<ResponseEntity<String>> deferredResult = RequestsHolder.getXbRestRequestMap().get("");
                        deferredResult.setResult(
                                ResponseEntity.status(HttpStatus.ACCEPTED)
                                        .body(payload));
                    } else {
                        LOGGER.info("Echo Request has already timeout. InstructionID: {}", "");
                    }
                    break;
            }
        } catch (Exception e) {
            throw new HSXServiceException(e.getMessage(), e);
        }
        return null;
    }
}
