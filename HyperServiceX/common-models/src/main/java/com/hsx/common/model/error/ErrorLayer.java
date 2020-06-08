package com.hsx.common.model.error;

public enum ErrorLayer {

    API_LAYER(1),
    SERVICE_LAYER(2);

    public final int code;


    ErrorLayer(int code) {
        this.code = code;
    }
}
