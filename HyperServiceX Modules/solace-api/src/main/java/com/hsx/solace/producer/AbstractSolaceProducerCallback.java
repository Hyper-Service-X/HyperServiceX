package com.hsx.solace.producer;

import com.hsx.solace.SolaceCorrelationData;
import com.hsx.solace.SolaceException;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPStreamingPublishCorrelatingEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AbstractSolaceProducerCallback implements JCSMPStreamingPublishCorrelatingEventHandler {

    private Logger logger = LoggerFactory.getLogger(AbstractSolaceProducerCallback.class);

    @Override
    public void handleError(String s, JCSMPException e, long l) {
        //Never called
    }

    @Override
    public void responseReceived(String s) {
        //Never called
    }

    @Override
    public void responseReceivedEx(Object o) {
        if (o instanceof SolaceCorrelationData) {
            SolaceCorrelationData correlationData = (SolaceCorrelationData) o;
            correlationData.getCallback().success(correlationData.getTopic(), correlationData.getSiteNo(), correlationData.getCorrelationData());
        }
    }

    @Override
    public void handleErrorEx(Object o, JCSMPException e, long l) {
        if (o instanceof SolaceCorrelationData) {
            SolaceCorrelationData correlationData = (SolaceCorrelationData) o;
            correlationData.getCallback().handleError(correlationData.getTopic(), correlationData.getSiteNo(), correlationData.getCorrelationData(), new SolaceException(e.getMessage(), e.getExtraInfo()), l);
        }
    }
}
