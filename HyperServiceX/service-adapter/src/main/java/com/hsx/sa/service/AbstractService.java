package com.hsx.sa.service;

import com.hsx.common.model.exception.HSXServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;

public abstract class AbstractService<S, T> implements ServiceInterface<S, T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);

    @Value("${NODE.SITE.NO}")
    protected int CURRENT_NODE_SITE_NO;

    @Value("${NODE.NO}")
    protected String CURRENT_NODE_NAME;

    @Autowired
    private ApplicationContext context;

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

    protected abstract Message<S> executeService(Message<T> msg) throws HSXServiceException, InterruptedException;

}
