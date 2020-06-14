package com.hsx.common.processor.service;


import com.hsx.common.model.constants.Formats;
import com.hsx.common.util.logging.InternalErrorLogger;
import com.hsx.common.model.error.ErrorLog;
import com.hsx.common.model.exception.HSXServiceException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;

public abstract class AbstractService<S, T> implements ServiceInterface<S, T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);
    protected static final InternalErrorLogger ERROR_LOGGER = new InternalErrorLogger();
    @Autowired
    private ApplicationContext context;

    protected long sTime, dTime = 0;

    @Override
    public Message<S> execute(@NonNull Message<T> msg) throws HSXServiceException {
        Message<S> outputMsg;
        try {
            outputMsg = executeService(msg);
        } catch (Exception e) {
            LOGGER.error(Formats.ERROR_OCCURRED_AT___LOG_DETAIL___ERROR_DETAIL___STACKTRACE___,
                    e.getStackTrace()[0].getMethodName(),
                    new ErrorLog.Builder("Unexpected Error Occurred While processing in " + this.getClass().getName())
                            .addPayload(msg)
                            .build(),
                    e.getMessage(),
                    ExceptionUtils.getStackTrace(e)
            );
            ERROR_LOGGER.error(Formats.ERROR_OCCURRED_AT___LOG_DETAIL___ERROR_DETAIL___STACKTRACE___,
                    e.getStackTrace()[0].getMethodName(),
                    new ErrorLog.Builder("Unexpected Error Occurred While processing in " + this.getClass().getName())
                            .addPayload(msg)
                            .build(),
                    e.getMessage(),
                    ExceptionUtils.getStackTrace(e)
            );
            throw new HSXServiceException(e);
        }
        return outputMsg;
    }

    protected abstract Message<S> executeService(@NonNull Message<T> msg) throws HSXServiceException, InterruptedException;
}
