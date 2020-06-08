package com.hsx.common.model.error;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HSXErrorInfo implements Serializable {
    private Status status;
    private ErrorSource errorSource; // 4 - Client side Error 		5 - Server side Error
    private ErrorLayer errorLayer;  // 1 - API Layer (SA,BO)		   	2 - Service Layer(Processor)
    private NodeName nodeName;
    private ApiError apiError;
    private ServiceError XBSServiceError;
    private String shortText;
    private List<String> errorList;
    private Exception exception;
    private HttpStatus httpStatus;
    private String reasonCode;

    public boolean _isSuccess() {
        return this.status == Status.SUCCESS;
    }

    public boolean _isWarning() {
        return this.status == Status.PARTIAL_SUCCESS;
    }

    public boolean _isError() {
        return this.status == Status.ERROR;
    }

    public boolean _isTimeOut() {
        return this.status == Status.TIMEOUT;
    }

    @JsonIgnore
    public HttpStatus getCustomizedHttpStatus() {
        if (httpStatus != null) {
            return httpStatus;
        } else {

            if (_isError()) {
                if (errorSource == ErrorSource.SERVER_ERROR) {
                    return HttpStatus.INTERNAL_SERVER_ERROR;
                }
                return HttpStatus.BAD_REQUEST;
            } else {
                return HttpStatus.OK;
            }
        }
    }
}