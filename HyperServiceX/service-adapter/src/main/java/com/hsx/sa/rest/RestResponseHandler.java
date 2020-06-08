package com.hsx.sa.rest;

import com.hsx.common.model.constants.Constants;
import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.error.ErrorLog;
import com.hsx.common.model.exception.HSXException;
import com.hsx.common.model.exception.HSXIOException;
import com.hsx.common.model.response.HSXMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Component("restResponseHandler")
public class RestResponseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseHandler.class);

    @Value("${NODE.SITE.NO}")
    private int CURRENT_SITE_NO;

    @Autowired
    RestTemplateResponseErrorHandler restTemplateResponseErrorHandler;

    @Autowired
    RestRequestHandler restRequestHandler;

    @Autowired
    private MessageChannel schemaValidationChannel;

    @Autowired
    private MessageChannel globalHttpStatusErrorHandlerChannel;

    public void handleResponse(String payload, String endpointCode, String instructionId, int fromSiteNo) throws HSXException {
        try {
            Message<String> fastMessage = MessageBuilder
                    .withPayload(payload)
                    .setHeader(Constants.MessageHeaders.RECEIVED_TIME.name(), System.currentTimeMillis())
                    .setHeader(Constants.MessageHeaders.MESSAGE_TYPE.name(), MessageType.NA)
                    .build();
            schemaValidationChannel.send(fastMessage);
        } catch (Exception e) {
            LOGGER.error("Error Occurred while  forwarding the message into schemaValidationChannel: {}", payload);
            throw new HSXException("Error Occurred while forwarding the message into schemaValidationChannel: " + payload);
        }
    }

    public void handleAck(HSXMessage failedPayload, MessageType messageType, String fromMemberCode) {
        try {
            switch (messageType) {
                case NA:
                    Message<HSXMessage> fastMessage = MessageBuilder
                            .withPayload(failedPayload)
                            .setHeader(Constants.MessageHeaders.MESSAGE_TYPE.name(), messageType)
                            .build();
                    globalHttpStatusErrorHandlerChannel.send(fastMessage);
                    break;
                default:
                    if (failedPayload.getHsxErrorInfo().getHttpStatus() == null) {
                        LOGGER.error(new ErrorLog.Builder("XB End Point Is Not Available")
                                .add(" Detailed Error", failedPayload.getHsxErrorInfo().getException().getMessage())
                                .add(" POST Payload", ((HSXIOException) failedPayload.getHsxErrorInfo().getException()).getData())
                                .build());
                    } else {
                        LOGGER.error("REST Output Gateway Service | Message Type {} | Instruction Id {} | Http Status {} | Failed Payload {}",
                                messageType.name(),
                                failedPayload.getData().toString(),
                                failedPayload.getHsxErrorInfo().getCustomizedHttpStatus().value(),
                                ((HSXIOException) failedPayload.getHsxErrorInfo().getException()).getData()
                        );
                    }
            }
        } catch (Exception e) {
            LOGGER.error("Error Occurred while  forwarding the message into globalHttpStatusErrorHandlerChannel | Message Type  {} | Failed payload {}", failedPayload.toString());
        }
    }

    private RestTemplate getRestTemplate(int restConnectionTimeout, int restReadTimeout) {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(restConnectionTimeout))
                .errorHandler(restTemplateResponseErrorHandler)
                .setReadTimeout(Duration.ofSeconds(restReadTimeout))
                .build();
    }

    public <T> ResponseEntity<T> restCallForPOST(String urlPath, @Nullable Object request, Class<T> responseType, int toInterfaceSiteNo, boolean retry, Map<String, String> httpHeaders, int restConnectionTimeout, int restReadTimeout, String REST_GW_HOSTNAME_SITE1, String REST_GW_HOSTNAME_SITE2) throws Exception {
        ResponseEntity responseEntity;
        if (toInterfaceSiteNo == 1) {
            try {
                ResponseEntity<T> tResponseEntity = postForEntity(REST_GW_HOSTNAME_SITE1.concat(urlPath), request, responseType, httpHeaders, restConnectionTimeout, restReadTimeout);
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set(Constants.MessageHeaders.MESSAGE_OPERATION.name(), "1");
                responseHeaders.addAll(tResponseEntity.getHeaders());
                responseEntity = ResponseEntity.ok().headers(responseHeaders).body(tResponseEntity.getBody());
                return responseEntity;
            } catch (Exception e) {
                if (retry)
                    return restCallForPOST(urlPath, request, responseType, 2, false, httpHeaders, restConnectionTimeout, restReadTimeout, REST_GW_HOSTNAME_SITE1, REST_GW_HOSTNAME_SITE2);
                LOGGER.error("REST API call Failed.Both Hosts are Down {} ,{}", REST_GW_HOSTNAME_SITE1, REST_GW_HOSTNAME_SITE2);
                throw e;
            }
        } else if (toInterfaceSiteNo == 2) {
            try {
                ResponseEntity<T> tResponseEntity = postForEntity(REST_GW_HOSTNAME_SITE2.concat(urlPath), request, responseType, httpHeaders, restConnectionTimeout, restReadTimeout);
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set(Constants.MessageHeaders.MESSAGE_OPERATION.name(), "2");
                responseHeaders.addAll(tResponseEntity.getHeaders());
                responseEntity = ResponseEntity.ok().headers(responseHeaders).body(tResponseEntity.getBody());
                return responseEntity;
            } catch (Exception e) {
                if (retry)
                    return restCallForPOST(urlPath, request, responseType, 1, false, httpHeaders, restConnectionTimeout, restReadTimeout, REST_GW_HOSTNAME_SITE1, REST_GW_HOSTNAME_SITE2);
                LOGGER.error("REST API call Failed. Both Hosts are Down {} ,{}", REST_GW_HOSTNAME_SITE1, REST_GW_HOSTNAME_SITE2);
                throw e;
            }
        }
        return null;
    }


    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Map<String, String> httpHeaders, int restConnectionTimeout, int restReadTimeout) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        httpHeaders.keySet().forEach(key -> {
            headers.set(key, httpHeaders.get(key));
        });
        HttpEntity requestWithHeaders = new HttpEntity<>(request, headers);
        return getRestTemplate(restConnectionTimeout, restReadTimeout).postForEntity(url, requestWithHeaders, responseType);
    }

}
