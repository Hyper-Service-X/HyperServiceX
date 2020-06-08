package com.hsx.common.processor.service;

import com.hsx.common.model.exception.HSXServiceException;
import org.springframework.messaging.Message;

public interface ServiceInterface<O, I> {
    Message<O> execute(Message<I> msg) throws HSXServiceException;
}
