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

        public Builder addDomesticInstructionId(String dmInstructionId) {
            stringBuilder.append(",Domestic Instruction Id: ".concat(dmInstructionId));
            return this;
        }

        public Builder addReverseCreditInstructionId(String dmInstructionId) {
            stringBuilder.append("Reversal Credit Transfer Domestic Instruction Id: ".concat(dmInstructionId));
            return this;
        }

        public Builder addOriginalInstructionId(String dmInstructionId) {
            stringBuilder.append(",Original Domestic Instruction Id: ".concat(dmInstructionId));
            return this;
        }

        public Builder addXBInstructionId(String xbInstructionId) {
            stringBuilder.append(",XB Instruction Id: ".concat(xbInstructionId));
            return this;
        }


        public Builder addMessageId(String messageID) {
            stringBuilder.append(",Message Id: ".concat(messageID));
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

        public Builder addMsgId(String msgId) {
            stringBuilder.append(",Msg Id: ".concat(msgId));
            return this;
        }

        public Builder addBizId(String bizId) {
            stringBuilder.append(",Biz Id: ".concat(bizId));
            return this;
        }

        public Builder addInstructingInterface(String instructingInterface) {
            stringBuilder.append(",Instructing Interface: ".concat(instructingInterface));
            return this;
        }

        public Builder addInstructedInterface(String instructedInterface) {
            stringBuilder.append(",Instructed Interface: ".concat(instructedInterface));
            return this;
        }

        public Builder addFromInterface(String fromCode) {
            stringBuilder.append(",From Interface: ".concat(fromCode));
            return this;
        }

        public Builder addToInterface(String toCode) {
            stringBuilder.append(",To Interface: ".concat(toCode));
            return this;
        }

        public Builder addFromSiteNo(int fromSiteNo) {
            stringBuilder.append(",From Site No: ".concat(String.valueOf(fromSiteNo)));
            return this;
        }

        public Builder addToSiteNo(int toSiteNo) {
            stringBuilder.append(",To Site No: ".concat(String.valueOf(toSiteNo)));
            return this;
        }

        public Builder addInstructedSiteNo(int instructedSiteNo) {
            stringBuilder.append(",Instructed Site No: ".concat(String.valueOf(instructedSiteNo)));
            return this;
        }

        public Builder addInstructingSiteNo(int instructingSiteNo) {
            stringBuilder.append(",Instructing Site No: ".concat(String.valueOf(instructingSiteNo)));
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

        public Builder addPaymentCancellationId(String assignId) {
            stringBuilder.append(",Payment Cancellation Id: ".concat(assignId));
            return this;
        }

        public Builder addMore(Object payload) {
            if (payload != null) {
                stringBuilder.append(", ".concat(payload.toString()));
            }
            return this;
        }

        public Builder addXBReasonCode(String reasonCode) {
            stringBuilder.append(",XB Exception Reason Code: ".concat(reasonCode));
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
