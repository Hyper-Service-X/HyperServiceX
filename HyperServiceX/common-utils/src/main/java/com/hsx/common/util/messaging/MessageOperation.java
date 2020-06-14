package com.hsx.common.util.messaging;

public enum MessageOperation {

    INTERNAL_SYNC("INTSYNC"),
    ACK("ACK");

    private String code;

    private MessageOperation(String code) {
        this.code = code;
    }

    public static MessageOperation getOperation(String code) {
        for (MessageOperation operation : MessageOperation.values()) {
            if (operation.getCode().equals(code)) {
                return operation;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}
