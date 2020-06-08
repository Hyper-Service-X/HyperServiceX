package com.hsx.solace.producer;

import com.hsx.solace.SolaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoNothingSolaceProducerCallback implements SolaceProducerCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger("SOLACE_LOG");

    @Override
    public void handleError(String topic, int siteNo, String messageId, SolaceException e, long timeStamp) {
        LOGGER.info("Error in sending message Correlation Data {}, Topic {} , Site No {}, Error {}", messageId, topic, siteNo, e.getMessage());
    }

    @Override
    public void success(String topic, int siteNo, String messageId) {
        LOGGER.info("Message Sent Successfully.Correlation Data {} Topic {} Site No {}", messageId, topic, siteNo);
    }
}
