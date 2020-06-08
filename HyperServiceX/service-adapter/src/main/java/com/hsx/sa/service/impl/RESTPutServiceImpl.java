package com.hsx.sa.service.impl;

import com.hsx.common.model.RequestsHolder;
import com.hsx.common.model.constants.Constants;
import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.exception.HSXIOException;
import com.hsx.common.model.exception.HSXServiceException;
import com.hsx.sa.rest.RestResponseHandler;
import com.hsx.sa.service.AbstractService;
import com.hsx.sa.web.api.utils.MAURLParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

@Component("RESTPutServiceImpl")
public class RESTPutServiceImpl extends AbstractService<Void, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RESTPutServiceImpl.class);

    @Autowired
    RestResponseHandler restResponseHandler;

    @Value("${NODE.SITE.NO}")
    private int CURRENT_NODE_SITE_NO;

    @Value("${REST.CONNECTION.TIMEOUT}")
    private int REST_CONNECTION_TIMEOUT;

    @Value("${REST.READ.TIMEOUT}")
    private int REST_READ_TIMEOUT;

    @Value("${REST.HOSTNAME.SITE1}")
    private String REST_GW_HOSTNAME_SITE1;

    @Value("${REST.HOSTNAME.SITE2}")
    private String REST_GW_HOSTNAME_SITE2;

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

    private ResponseEntity restCallForPOST(String uriPath, String payload, Class<String> stringClass, int toInterfaceSiteNo, boolean retry, String instrId, String authKey, String msgDef, MessageType MessageType) throws Exception {
        if (payload != null) {
            Map<String, String> headers = new HashMap<>();
            headers.put(HttpHeaders.CONTENT_TYPE, "text/xml; charset=utf-8"); //todo : Move to constants
            headers.put(HttpHeaders.ACCEPT, getMediaType(MessageType));
            headers.put(HttpHeaders.AUTHORIZATION, authKey);
            headers.put(MAURLParams.X_MSG_DEF, msgDef);
            headers.put(MAURLParams.X_INSTR_ID, instrId);
            headers.put(MAURLParams.X_API_VERSION, "v1");
            return restCallForPOST(uriPath, payload, stringClass, toInterfaceSiteNo, retry, headers);
        }
        return null;
    }

    private ResponseEntity restCallForPOST(String uriPath, String payload, Class<String> stringClass, int toInterfaceSiteNo, boolean retry, Map<String, String> httpHeaders) throws Exception {
        ResponseEntity responseEntity;
        try {
            responseEntity = restResponseHandler.restCallForPOST(uriPath, payload, stringClass, toInterfaceSiteNo, retry, httpHeaders, REST_CONNECTION_TIMEOUT, REST_READ_TIMEOUT, REST_GW_HOSTNAME_SITE1, REST_GW_HOSTNAME_SITE2);
        } catch (ResourceAccessException e) {
            LOGGER.info("REST Output Gateway Service | Sending Acknowledgment");
            HSXIOException HSXIOException = new HSXIOException(e);
            HSXIOException.setData(payload);
            throw HSXIOException;
        } catch (HSXIOException e) {
            e.setData(payload);
            LOGGER.info("REST Output Gateway Service | Sending Acknowledgment");
            throw e;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error("REST Output Gateway Service | REST call {} | Error {}", uriPath, e.getMessage());
            throw new HSXServiceException(e.getMessage(), e);

        }
        return responseEntity;
    }

    private String getMediaType(MessageType MessageType) {
        switch (MessageType) {
            //TODO add more message Type based on the ACCEPTED format
            default:
                return MediaType.APPLICATION_XML_VALUE;
        }
    }
}
