package com.hsx.common.model.error;

public enum ErrorSource {
    CLIENT_ERROR(4),
    SERVER_ERROR(5);

    public final int code;


    ErrorSource(int code) {
        this.code = code;
    }
}
