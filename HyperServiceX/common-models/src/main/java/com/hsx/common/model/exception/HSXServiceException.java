package com.hsx.common.model.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HSXServiceException extends Exception {

    private String className;
    private String methodName;

    public HSXServiceException() {
    }

    public HSXServiceException(String message) {
        super(message);
    }

    public HSXServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public HSXServiceException(Throwable cause) {
        super(cause);
    }

    public HSXServiceException(String message, String className, String methodName) {
        super(message);
        this.className = className;
        this.methodName = methodName;
    }

    public HSXServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
