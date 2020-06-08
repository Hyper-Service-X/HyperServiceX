package com.hsx.common.model.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HSXDAOException extends Exception {

    private String tableName;
    private String operation;

    public HSXDAOException(String message) {
        super(message);
    }

    public HSXDAOException(String message, Throwable e) {
        super(message, e);
    }

    public HSXDAOException(String message, String operation, String tableName) {
        super(message);
        this.tableName = tableName;
        this.operation = operation;
    }
}
