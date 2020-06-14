package com.hsx.common.util.logging;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("internalErrorLogger")
public class InternalErrorLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger("ERROR_LOG");

    public void info(String className, String methodName, String message) {
        if (message != null) {
            message = message.replaceAll(">[\\s]+?<", "><");
            LOGGER.info(message);
        }
    }

    public void info(String message) {
        if (message != null) {
            message = message.replaceAll(">[\\s]+?<", "><");
            LOGGER.info(message);
        }
    }

    public void error(String message, Throwable e) {
        LOGGER.error("Error Message:" + message + ",Exception:" + ExceptionUtils.getStackTrace(e));

    }

    public void error(String format, Object... arguments) {
        LOGGER.error(format, arguments);

    }

    public void error(String className, String methodName, String message, Throwable e) {
        LOGGER.error("Class Name: " + className + ",Method Name:" + methodName + ",Error Message:" + message + ",Exception:" + ExceptionUtils.getStackTrace(e));
    }
}
