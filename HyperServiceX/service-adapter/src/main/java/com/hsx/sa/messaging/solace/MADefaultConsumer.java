package com.hsx.sa.messaging.solace;

import com.bcs.xborder.common.util.messaging.MessageOperation;
import com.hsx.common.model.constants.MessageType;
import com.hsx.sa.routing.GlobalMessageOutRoutingHandler;
import com.hsx.solace.SolaceException;
import com.hsx.solace.consumer.SolaceConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MADefaultConsumer extends SolaceConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MADefaultConsumer.class);

    @Autowired
    private GlobalMessageOutRoutingHandler globalMessageHandler;

    @Override
    public void onMessage(String destination, String message, long senderTimeStamp, long receivedTimestamp) {
        try {
            String[] topicComponents = destination.split("/");
            MessageType messageType = MessageType.getMessageTypeByCode(topicComponents[4]);

            String msgOperation = topicComponents[5];
            MessageOperation operation = MessageOperation.getOperation(msgOperation);

            String direction = topicComponents[6];
            String maMessageEntryNode = destination.split("/")[8];

            //String[] customVals = topicComponents.length > 23 ? Arrays.copyOfRange(topicComponents, 27, topicComponents.length - 1) : new String[]{};
            globalMessageHandler.process(
                    messageType,
                    operation,
                    message,
                    receivedTimestamp,
                    maMessageEntryNode

            );
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error occurred during channeling the message {}", e.getMessage());
        }
    }

    @Override
    public void onException(SolaceException e) {
    }
}
