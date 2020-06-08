package com.hsx.bo.messaging.solace;

import com.hsx.bo.spring.integration.BoMessageRoutingInGateway;
import com.hsx.common.model.constants.Constants;
import com.hsx.common.model.constants.MessageType;
import com.hsx.solace.SolaceException;
import com.hsx.solace.consumer.SolaceConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public class BODefaultConsumer extends SolaceConsumer {

    @Autowired
    private BoMessageRoutingInGateway boMessageRoutingInGateway;

    @Override
    public void onMessage(String destination, String message, long senderTimeStamp, long receivedTimestamp) {
        Message<String> fastMessage = MessageBuilder
                .withPayload(message)
                .setHeader(Constants.MessageHeaders.MESSAGE_TYPE.name(), MessageType.getMessageTypeByCode(destination.split("/")[4]))
                .build();
        boMessageRoutingInGateway.send(fastMessage);

    }

    @Override
    public void onException(SolaceException e) {

    }
}
