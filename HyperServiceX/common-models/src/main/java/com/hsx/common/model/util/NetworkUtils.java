package com.hsx.common.model.util;

import com.hsx.common.model.error.ErrorLayer;
import com.hsx.common.model.error.HSXErrorInfo;
import com.hsx.common.model.response.HSXApiResponse;
import com.hsx.common.model.response.HSXResponseWrapper;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

public class NetworkUtils {


    public static ResponseEntity wrap(HSXApiResponse data) {

        try {
            if (data != null && data.getData() != null) {
                if (data.getData().getClass() == Resources.class || data.getData().getClass() == PagedResources.class) {
                    Resources resources = (Resources) data.getData();

                    if (resources != null) {
                        return new ResponseEntity(new HSXResponseWrapper<>(data, resources), data.getCustomizedHttpStatus());
                    }
                }
            }
            return new ResponseEntity(new HSXResponseWrapper<>(data), data.getCustomizedHttpStatus());
        } catch (Exception e) {
            return wrap(e, "Error while wrapping final response");
        }

    }

    public static ResponseEntity wrap(Exception e, String message) {

        HSXErrorInfo error = new HSXErrorInfo();
        error.setErrorLayer(ErrorLayer.API_LAYER);
        error.setShortText(message);
        error.setException(e);
        return new ResponseEntity(new HSXResponseWrapper<>(error), error.getCustomizedHttpStatus());
    }

    public static ResponseEntity wrap(ResponseEntity message, Exception e) {

        if (message.hasBody()) {
            HSXResponseWrapper responseWrapper = (HSXResponseWrapper) message.getBody();
            if (responseWrapper.getStatusCode() == -1 && responseWrapper.getData() != null) {
                if (responseWrapper.getData().size() == 1 && e == null) {
                    Object o = responseWrapper.getData().get(0);
                    if (o instanceof HSXApiResponse) {
                        return wrap((HSXApiResponse) o);
                    }
                }

                if (responseWrapper.getData().size() == 1 && e != null) {
                    return wrap(e, ((HSXApiResponse) responseWrapper.getData().get(0)).getShortText());
                }

                List errorList = responseWrapper.getData();

                if (errorList.size() > 0) {
                    HSXErrorInfo error = new HSXErrorInfo();
                    error.setErrorLayer(ErrorLayer.API_LAYER);
                    error.setErrorList((List<String>) errorList.stream().map(o ->
                    {
                        if (o instanceof HSXApiResponse) {
                            return ((HSXApiResponse) o).getShortText();
                        }
                        return null;
                    }).collect(Collectors.toList()));
                    error.setException(e);
                    error.setShortText("Failed with one or more errors");
                    return new ResponseEntity(new HSXResponseWrapper<>(error), error.getCustomizedHttpStatus());
                }
            } else if (responseWrapper.getStatusCode() == -1) {
                HSXErrorInfo error = new HSXErrorInfo();
                error.setErrorLayer(ErrorLayer.API_LAYER);
                error.setShortText(responseWrapper.getErrorMessage());
                error.setException(e);
                return new ResponseEntity(new HSXResponseWrapper<>(error), error.getCustomizedHttpStatus());
            }
        }

        return message;
    }
}
