package com.hsx.sa.routing;


import com.hsx.common.model.constants.Constants;
import com.hsx.common.model.exception.HSXServiceException;
import com.hsx.common.model.request.SARequest;
import com.hsx.sa.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("globalMessageInRoutingHandler")
public class GlobalMessageInRoutingHandler extends AbstractService<SARequest, SARequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalMessageInRoutingHandler.class);

    @Override
    public Message<SARequest> executeService(Message<SARequest> msg) throws HSXServiceException, InterruptedException {
        long startTime = (long) msg.getHeaders().get(Constants.MessageHeaders.RECEIVED_TIME.name());
        return msg;
    }
}
