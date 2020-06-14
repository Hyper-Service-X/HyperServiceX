package com.hsx.bo.service.impl;

import com.hsx.common.util.util.JSONUtil;
import com.hsx.bo.handler.response.BOResponseHandler;
import com.hsx.bo.service.AbstractService;
import com.hsx.common.model.constants.Constants;
import com.hsx.common.model.constants.ErrorCode;
import com.hsx.common.model.exception.HSXException;
import com.hsx.common.model.response.HSXMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Prasad on 24th March 2020
 */

@Component("boResponseService")
public class BOResponseServiceImpl extends AbstractService<Void, String> {

    Logger LOGGER = LoggerFactory.getLogger(BOResponseServiceImpl.class);


    private BOResponseHandler[] boResponseHandlers = null;

    @PostConstruct
    public void initializeHandlers() {
        boResponseHandlers = new BOResponseHandler[]{
        };
    }

    @Value("${NODE.SITE.NO}")
    private int CURRENT_NODE_SITE_NO;

    @Override
    @ServiceActivator(inputChannel = "boRSChannel")
    protected Message<Void> executeService(Message<String> msg) {
        LOGGER.info("executeService {} message...", msg.getHeaders().get(Constants.MessageHeaders.MESSAGE_TYPE.name()));
        try {

            BOResponseHandler boResponseHandler = selectHandler(msg.getPayload());
            HSXMessage hsxMessage = boResponseHandler.resolveResponse(msg.getPayload());
            boResponseHandler.updateBOResponse(hsxMessage);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private BOResponseHandler selectHandler(String payload) throws HSXException {
        HSXMessage boResponse = resolveBoRequestType(payload);
        for (BOResponseHandler boResponseHandler : this.boResponseHandlers) {
            if (boResponseHandler.getSubMessageTypes().contains(boResponse.getHeader().getSubMessageType())) {
                return boResponseHandler;
            }
        }
        return null;
    }


    private HSXMessage resolveBoRequestType(String stringResponse) throws HSXException {
        if (stringResponse != null) {
            try {
                return JSONUtil.convertToObject(stringResponse, HSXMessage.class);
            } catch (Exception e) {
                throw new HSXException(ErrorCode.ERROR_9999.getValue(), "Bo request type can not be found from received payload to " + HSXMessage.class.getName());
            }
        }
        return null;
    }

}
