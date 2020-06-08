package com.hsx.solace.consumer;

import com.hsx.solace.SolaceException;
import com.hsx.solace.util.JSONUtil;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.XMLMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unchecked")
public abstract class TypedSolaceConsumer<T> implements XMLMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger("SOLACE_LOG");

    @Override
    public void onReceive(BytesXMLMessage bytesXMLMessage) {
        if (bytesXMLMessage instanceof TextMessage) {
            try {
                TextMessage message = ((TextMessage) bytesXMLMessage);
                onMessage(message.getDestination().getName(), (T) JSONUtil.convertToObject(message.getText(), messageType()), message.getSenderTimestamp(), message.getReceiveTimestamp());
                bytesXMLMessage.ackMessage();
            } catch (Exception e) {
                LOGGER.error("Error occurred in processing message {}", e.getMessage());
            }
        } else {
            onException(new SolaceException("Unsupported message format received", null));
        }
    }

    @Override
    public void onException(JCSMPException e) {
        onException(new SolaceException(e.getMessage(), e.getExtraInfo()));
    }

    public abstract void onMessage(String destination, T message, long senderTs, long receiverTs);

    public abstract void onException(SolaceException e);

    public abstract Class messageType();
}
