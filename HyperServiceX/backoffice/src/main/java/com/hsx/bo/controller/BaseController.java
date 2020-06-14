package com.hsx.bo.controller;

import com.hsx.common.model.error.ErrorLog;
import com.hsx.common.util.messaging.MessageOperation;
import com.hsx.bo.routing.GlobalBoMessageInRoutingHandler;
import com.hsx.common.model.RequestsHolder;
import com.hsx.common.model.constants.Formats;
import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.constants.SubMessageType;
import com.hsx.common.model.request.BORequest;
import com.hsx.common.model.request.BORequestCriteria;
import com.hsx.common.model.request.Header;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;

public class BaseController {

    Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    GlobalBoMessageInRoutingHandler globalBoMessageInRoutingHandler;

    public DeferredResult<ResponseEntity<String>> handleRequest(BORequestCriteria requestData, SubMessageType subMessageType, long requestTimeout, MessageType messageType, MessageOperation messageOperation, RequestsHolder.RequestHolderCallback callback) {
        BORequest boRequest = new BORequest();
        boRequest.setRequestData(requestData);
        String jobId = UUID.randomUUID().toString();
        boRequest.setHeader(
                new Header.Builder()
                        .setTransactionId(jobId)
                        .setSubMessageType(subMessageType)
                        .setMessageType(messageType)
                        .build()
        );
        DeferredResult<ResponseEntity<String>> deferredResult =
                RequestsHolder.createDeferredResult(
                        jobId,
                        requestTimeout,
                        callback
                );
        try {
            globalBoMessageInRoutingHandler.handleRequest(boRequest, System.currentTimeMillis(), messageType, messageOperation);
        } catch (Exception e) {
            LOGGER.error(Formats.ERROR_OCCURRED_AT___LOG_DETAIL___ERROR_DETAIL___STACKTRACE___,
                    e.getStackTrace()[0].getMethodName(),
                    new ErrorLog.Builder("Back Office Common Request Handler")
                            .addBORequestType(subMessageType.name())
                            .addMessageType(messageType.name())
                            .addMessageOperation(messageOperation.name())
                            .addPayload(requestData),
                    e.getMessage(),
                    ExceptionUtils.getStackTrace(e)
            );
        }
        return deferredResult;
    }
}
