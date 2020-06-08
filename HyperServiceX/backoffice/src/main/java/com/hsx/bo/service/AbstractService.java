package com.hsx.bo.service;

import com.hsx.common.model.exception.HSXServiceException;
import org.springframework.messaging.Message;

public abstract class AbstractService<S, T> implements ServiceInterface<S, T> {

    protected long sTime, dTime = 0;

    @Override
    public Message<S> execute(Message<T> msg) throws HSXServiceException {
        Message<S> outputMsg;
        try {
            outputMsg = executeService(msg);
        } catch (Exception e) {
            throw new HSXServiceException(e);
        }
        return outputMsg;
    }

    protected abstract Message<S> executeService(Message<T> msg)
            throws HSXServiceException, InterruptedException;

}
