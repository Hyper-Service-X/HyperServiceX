package com.hsx.common.model.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Getter
@Setter
public class HSXIOException extends IOException {

    private HttpStatus httpStatus;
    private Object data;

    public HSXIOException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HSXIOException(Exception e) {
       super(e);
    }

}
