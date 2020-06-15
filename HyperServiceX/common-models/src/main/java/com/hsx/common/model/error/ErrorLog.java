package com.hsx.common.model.error;

import com.hsx.common.model.util.ErrorUtil;
import org.springframework.stereotype.Component;

@Component
public class ErrorLog {

    public static class Builder {
        private StringBuilder stringBuilder;

        public Builder() {
            stringBuilder = new StringBuilder();
        }

        public Builder(String logName) {
            stringBuilder = new StringBuilder().append(logName);
        }


        public Builder addMethod(String methodName) {
            stringBuilder.append(",Method Name: ".concat(methodName));
            return this;
        }

        public Builder addMessageId(String messageID) {
            stringBuilder.append(",transaction Id: ".concat(messageID));
            return this;
        }

        public Builder addBORequestType(String boRequestType) {
            stringBuilder.append(",BO Request Type: ".concat(boRequestType));
            return this;
        }

        public Builder addMessageType(String messageType) {
            stringBuilder.append(",Message Type: ".concat(messageType));
            return this;
        }

        public Builder addMessageOperation(String messageOperation) {
            stringBuilder.append(",Message Operation: ".concat(messageOperation));
            return this;
        }

        public Builder addPayload(String payload) {
            stringBuilder.append(",Input Payload: ".concat(payload));
            return this;
        }

        public Builder addPayload(Object payload) {
            stringBuilder.append(",Input Payload: ".concat(payload.toString()));
            return this;
        }

        public Builder addMore(Object payload) {
            if (payload != null) {
                stringBuilder.append(", ".concat(payload.toString()));
            }
            return this;
        }

        public Builder addGlobalError(HSXErrorInfo HSXErrorInfo) {
            stringBuilder.append(",Global Error Code: ".concat(ErrorUtil.mapErrorCode(HSXErrorInfo)));
            return this;
        }

        public Builder add(String key, Object value) {
            stringBuilder.append(", " + key.concat(": ").concat(value.toString()));
            return this;
        }

        public String build() {
            return stringBuilder.toString();
        }
    }

    public ErrorLog() {
    }
}
