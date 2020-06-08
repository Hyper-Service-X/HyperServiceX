package com.hsx.common.model.exception;

import com.hsx.common.model.error.HSXErrorInfo;
import lombok.*;

@Getter
@Setter
public class HSXException extends Exception {

    private String className;
    private String methodName;
    private String reasonCode;
    private HSXErrorInfo HSXErrorInfo;
    private Object data;

    public HSXException(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public HSXException(String reasonCode, Object data) {
        this.reasonCode = reasonCode;
        this.data = data;
    }

    public HSXException(String reasonCode, String message, Object data) {
        super(message);
        this.reasonCode = reasonCode;
        this.data = data;
    }

    public HSXException(HSXErrorInfo HSXErrorInfo, Object data) {
        this.reasonCode = HSXErrorInfo.getReasonCode();
        this.HSXErrorInfo = HSXErrorInfo;
        this.data = data;
    }

}
