package com.hsx.sa.messaging;

import com.hsx.solace.SolaceException;

public interface MessagingCallback {

    public void handleError(String topic, int siteNo, String messageId, SolaceException e, long timeStamp);

    public void success(String topic, int siteNo, String messageId);

}
