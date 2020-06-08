package com.hsx.common.model.error;

public enum Status {

    SUCCESS("SUCCESS", 1),
    PARTIAL_SUCCESS("PARTIAL SUCCESS", 0),
    ERROR("ERROR", -1),
    TIMEOUT("TIMEOUT", -2);

    public final String status;
    public final int code;

    public String getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    Status(String status, int code) {
        this.status = status;
        this.code = code;
    }
}
