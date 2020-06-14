package com.hsx.bo.aspect;

import com.hsx.common.model.constants.Formats;
import com.hsx.common.model.exception.HSXException;
import com.hsx.common.model.error.ErrorLog;
import com.hsx.common.util.logging.InternalErrorLogger;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LoggingAspect {

    protected static final InternalErrorLogger ERROR_LOGGER = new InternalErrorLogger();

    @AfterThrowing(pointcut = "execution(* com.hsx.bo.*(..))", throwing = "ex")
    public void logAfterThrowingAllMethods(Exception ex) throws Throwable {
        if (ex instanceof HSXException) {
            HSXException HSXException = (HSXException) ex;
            if (HSXException.getClassName() != null && HSXException.getMethodName() != null) {
                if (HSXException.getHSXErrorInfo() != null) {
                    ERROR_LOGGER.error(Formats.ERROR_OCCURRED_AT___LOG_DETAIL___ERROR_DETAIL___STACKTRACE___,
                            "Class: ".concat(HSXException.getClassName()).concat(",Method: ").concat(HSXException.getMethodName()),
                            new ErrorLog.Builder()
                                    .addXBReasonCode(HSXException.getReasonCode())
                                    .addGlobalError(HSXException.getHSXErrorInfo())
                                    .addPayload(HSXException.getData())
                                    .build(),
                            HSXException.getMessage(),
                            ExceptionUtils.getStackTrace(HSXException));
                } else {
                    ERROR_LOGGER.error(Formats.ERROR_OCCURRED_AT___LOG_DETAIL___ERROR_DETAIL___STACKTRACE___,
                            "Class: ".concat(HSXException.getClassName()).concat(",Method: ").concat(HSXException.getMethodName()),
                            new ErrorLog.Builder()
                                    .addXBReasonCode(HSXException.getReasonCode())
                                    .addPayload(HSXException.getData())
                                    .build(),
                            HSXException.getMessage(),
                            ExceptionUtils.getStackTrace(HSXException));
                }
            } else {
                ERROR_LOGGER.error(Formats.ERROR_OCCURRED_AT___LOG_DETAIL___ERROR_DETAIL___STACKTRACE___,
                        "Method: ".concat(HSXException.getStackTrace()[0].getMethodName()),
                        new ErrorLog.Builder()
                                .addXBReasonCode(HSXException.getReasonCode())
                                .addPayload(HSXException.getData())
                                .build(),
                        HSXException.getMessage(),
                        ExceptionUtils.getStackTrace(HSXException));
            }
        }
    }
}