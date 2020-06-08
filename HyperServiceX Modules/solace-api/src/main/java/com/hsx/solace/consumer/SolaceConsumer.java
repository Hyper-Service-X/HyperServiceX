package com.hsx.solace.consumer;

import com.hsx.solace.SolaceException;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.XMLMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class SolaceConsumer implements XMLMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger("SOLACE_LOG");

    @Override
    public void onReceive(BytesXMLMessage bytesXMLMessage) {
        if (bytesXMLMessage instanceof TextMessage) {
            try {
                String destination = ((TextMessage) bytesXMLMessage).getDestination().getName();
                TextMessage message = ((TextMessage) bytesXMLMessage);
                LOGGER.info("Solace Message Received | Topic {} | Content {} ", destination, message.getText());
                onMessage(destination, message.getText(), message.getSenderTimestamp() ==null ? 0 : message.getSenderTimestamp(), message.getReceiveTimestamp());
                bytesXMLMessage.ackMessage();
            } catch (Exception e) {
                LOGGER.error("Error occurred in processing message {}", e.getMessage());
                e.printStackTrace();
            }
        } else {
            onException(new SolaceException("Unsupported message format received", null));
        }
    }

    @Override
    public void onException(JCSMPException e) {
        onException(new SolaceException(e.getMessage(), e.getExtraInfo()));
    }

    public abstract void onMessage(String destination, String message, long senderTimeStamp, long receivedTimestamp);

    public abstract void onException(SolaceException e);
}
