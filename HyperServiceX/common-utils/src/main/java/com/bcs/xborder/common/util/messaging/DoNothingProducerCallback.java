package com.bcs.xborder.common.util.messaging;

public class DoNothingProducerCallback implements MessageProducerCallback {
    @Override
    public void handleError(String topic, int siteNo, String messageId, MessagingException e, long timeStamp) {

    }

    @Override
    public void success(String topic, int siteNo, String messageId) {

    }
}
