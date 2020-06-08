package com.bcs.xborder.common.util.messaging;


public interface MessageProducerCallback {

    public void handleError(String topic, int siteNo, String messageId, MessagingException e, long timeStamp);

    public void success(String topic, int siteNo, String messageId);

}
