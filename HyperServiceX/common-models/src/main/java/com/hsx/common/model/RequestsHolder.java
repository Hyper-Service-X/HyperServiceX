package com.hsx.common.model;

import com.hsx.common.model.error.Status;
import com.hsx.common.model.response.HSXApiResponse;
import com.hsx.common.model.util.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RequestsHolder {

    static Logger LOGGER = LoggerFactory.getLogger(RequestsHolder.class);


    public static HSXApiResponse xbDefaultTimeoutResponse = new HSXApiResponse(com.hsx.common.model.error.Status.TIMEOUT, "Request is Timeout", HttpStatus.REQUEST_TIMEOUT);

    public static HSXApiResponse xbDefaultErrorResponse = new HSXApiResponse(Status.ERROR, "Unexpected Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);

    public static Map<String, DeferredResult> REST_REQ_HOLDER = new ConcurrentHashMap<>();

    public DeferredResult<ResponseEntity<String>> getDeferredResult(String key) {
        return REST_REQ_HOLDER.get(key);
    }

    public static DeferredResult<ResponseEntity<String>> createDeferredResult(String key,
                                                                              long timeout, RequestHolderCallback callback) {
        return createDeferredResult(REST_REQ_HOLDER, key, timeout, callback);
    }

    private static DeferredResult<ResponseEntity<String>> createDeferredResult(Map<String, DeferredResult> map, String key,
                                                                               long timeout, RequestHolderCallback callback) {
        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<ResponseEntity<String>>(timeout);

        deferredResult.onCompletion(() -> {
            if (callback != null) {
                callback.onComplete(map.get(key));
            }

            if (map.containsKey(key)) {
                map.remove(key);
            }
            LOGGER.info("Request Successfully Completed | Unique Id {} ", key);
        });

        deferredResult.onError(throwable -> {
            if (callback != null) {
                callback.onError(map.get(key));
            } else {
                //default error response
                deferredResult.setErrorResult(
                        ResponseEntity.status(xbDefaultErrorResponse.getHttpStatus()).body(NetworkUtils.wrap(xbDefaultErrorResponse)));
            }
            if (map.containsKey(key)) {
                map.remove(key);
            }
            LOGGER.info("Error in processing response | Unique Id {} ", key);
        });

        deferredResult.onTimeout(() -> {
            if (callback != null) {
                callback.onTimeout(map.get(key));
            } else {
                //default timeout response
                deferredResult.setErrorResult(
                        ResponseEntity.status(xbDefaultTimeoutResponse.getHttpStatus()).body(NetworkUtils.wrap(xbDefaultTimeoutResponse)));
            }

            if (map.containsKey(key)) {
                map.remove(key);
            }
            LOGGER.info("Request timeout occurred | Unique Id {} ", key);
        });

        map.put(key, deferredResult);
        LOGGER.info("Request added to static map | Unique Id {}", key);
        return deferredResult;
    }

    public interface RequestHolderCallback {
        public void onError(DeferredResult r);

        public void onComplete(DeferredResult r);

        public void onTimeout(DeferredResult r);
    }

    public static Map<String, DeferredResult> getXbRestRequestMap() {
        return REST_REQ_HOLDER;
    }

}
