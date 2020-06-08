package com.hsx.common.model.constants;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //ALL UNEXPECTED EXCEPTIONS THROWS THIS ERROR CODES AND HANDLING IT INTERNALLY
    ERROR_9999("9999");

    private String value;

    private ErrorCode(String value){
        this.value = value;
    }
}
