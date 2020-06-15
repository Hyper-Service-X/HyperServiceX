package com.hsx.common.model.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HSXMessageException extends Exception {

    private String messageType;

    public HSXMessageException() {
    }

    public HSXMessageException(String message) {
        super(message);
    }

    public HSXMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public HSXMessageException(Throwable cause) {
        super(cause);
    }

    public HSXMessageException(String message, String messageType) {
        super(message);
        this.messageType = messageType;
    }

    public HSXMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
