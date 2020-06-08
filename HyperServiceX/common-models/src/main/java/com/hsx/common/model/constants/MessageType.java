package com.hsx.common.model.constants;

import lombok.Getter;

@Getter
public enum MessageType {

    PROCESSOR_STATUS("PROSTAUS"),
    SA_STATUS("SASTATUS"),

    NA("NA");

    private String code;

    private MessageType(String code) {
        this.code = code;
    }


    public static MessageType getMessageTypeByCode(String code) {
        for (MessageType mt : MessageType.values()) {
            if (mt.getCode().equals(code)) {
                return mt;
            }
        }
        return null;
    }

    public static MessageType getMessageTypeByName(String name) {
        for (MessageType mt : MessageType.values()) {
            if (mt.name().equals(name)) {
                return mt;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}
