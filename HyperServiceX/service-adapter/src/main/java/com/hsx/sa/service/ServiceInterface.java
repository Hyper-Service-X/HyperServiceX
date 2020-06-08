package com.hsx.sa.service;

import com.hsx.common.model.exception.HSXServiceException;
import org.springframework.messaging.Message;

public interface ServiceInterface<S, T> {
    Message<S> execute(Message<T> msg) throws HSXServiceException;
}
