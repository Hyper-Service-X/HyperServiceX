package com.hsx.solace.producer;

import com.hsx.solace.SolaceException;

public interface SolaceProducerCallback {
    public void handleError(String topic, int siteNo, String correlationData, SolaceException e, long timeStamp);

    public void success(String topic, int siteNo, String correlationData);
}
