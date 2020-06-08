package com.hsx.common.model.error;

public enum ApiError {


    NO_ERROR(0, ""),
    API_VALIDATION_ERROR(1, ""),

    ERR_UNKNOWN_EXCEPTION(9999, "Unknown error has occurred");

    public final int code;
    public final String message;

    ApiError(final int code, String message) {
        this.code = code;
        this.message = message;
    }


}
