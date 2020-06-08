package com.hsx.common.model.response;

import com.hsx.common.model.error.ApiError;
import com.hsx.common.model.error.ErrorSource;
import com.hsx.common.model.error.HSXErrorInfo;
import com.hsx.common.model.error.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HSXApiResponse<T> extends HSXErrorInfo implements Serializable {
    private T data;
    private Map<String, String> metadata;

    public HSXApiResponse(T data) {
        this.data = data;
    }

    public HSXApiResponse(HSXErrorInfo hsxErrorInfo) {
        super(hsxErrorInfo.getStatus(), hsxErrorInfo.getErrorSource(), hsxErrorInfo.getErrorLayer(), hsxErrorInfo.getNodeName(), hsxErrorInfo.getApiError(), hsxErrorInfo.getXBSServiceError(), hsxErrorInfo.getShortText(), hsxErrorInfo.getErrorList(), hsxErrorInfo.getException(), hsxErrorInfo.getHttpStatus(), hsxErrorInfo.getReasonCode());

    }

    public HSXApiResponse(Status status, T data) {
        super(status, null, null, null, null, null, null, null, null, null, null);
        this.data = data;
    }

    public HSXApiResponse(Status status, String shortText, HttpStatus httpStatus) {
        super(status, null, null, null, null, null, shortText, null, null, httpStatus, null);
    }

    public HSXApiResponse(Status status, String shortText, HttpStatus httpStatus, ErrorSource errorSource, ApiError apiError) {
        super(status, errorSource, null, null, apiError, null, shortText, null, null, httpStatus, null);
    }
}
