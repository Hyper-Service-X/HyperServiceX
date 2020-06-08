package com.hsx.solace.producer;

import com.hsx.solace.HSXJCSMPSession;
import com.hsx.solace.SolaceCorrelationData;
import com.hsx.solace.SolaceException;
import com.hsx.solace.SolaceSessionService;
import com.hsx.solace.annotations.SolaceSite;
import com.hsx.solace.autoconfigure.props.SolaceDefaultProperties;
import com.hsx.solace.util.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.solacesystems.jcsmp.*;
import com.solacesystems.jcsmp.impl.AbstractDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "hsx.messaging.service", havingValue = "solace", matchIfMissing = true)
public abstract class SolaceMessageProducer<T> {

    @Autowired
    SolaceSessionService solaceSessionService;

    @Autowired
    private SolaceDefaultProperties solaceDefaultProperties;

    private Logger LOGGER = LoggerFactory.getLogger("SOLACE_LOG");

   /* public void publishDirectMessage(String topicStr, T value) throws SolaceException {
        try {
            JCSMPSession session = solaceSessionService.getDefaultSession().getSession();
            final Topic topic = JCSMPFactory.onlyInstance().createTopic(topicStr);
            XMLMessageProducer prod = session.getMessageProducer(new AbstractSolaceProducerCallback(solaceSessionService.getDefaultSession().getSiteNo(), topicStr, new DoNothingSolaceProducerCallback()));
            TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            sendMessage(prod, topic, msg, value);
        } catch (JCSMPException e) {
            throw new SolaceException(e.getMessage(), e.getExtraInfo());
        } catch (Exception e) {
            throw new SolaceException("Error in publishing message", null);
        }
    }*/

    /*public void publishGuaranteedMessageToTopic(int siteNo, String topic, T value, SolaceProducerCallback callback) throws SolaceException {
        publishGuaranteedMessageToTopic(siteNo, topic, value, callback, solaceSessionService.getDefaultSession().getSession());
    }*/

    public void publishGuaranteedMessageToTopic(String topicName, T value, String correlationData, SolaceProducerCallback callback) throws SolaceException {
        publishGuaranteedMessageToTopic(solaceSessionService.getDefaultSession().getSiteNo(), topicName, value, correlationData, callback, solaceSessionService.getDefaultSession().getSession());
    }

    public void publishGuaranteedMessageToTopic(String topicName, T value, String correlationData) throws SolaceException {
        publishGuaranteedMessageToTopic(solaceSessionService.getDefaultSession().getSiteNo(), topicName, value, correlationData, new DoNothingSolaceProducerCallback(), solaceSessionService.getDefaultSession().getSession());
    }


    public void publishGuaranteedMessageToTopic(String topic, T value, String correlationData, SolaceSite site, int retryCount, long timeout) throws SolaceException {
        publishGuaranteedMessageToTopic(topic, value, correlationData, site, new DoNothingSolaceProducerCallback(), retryCount, timeout);
    }

    public void publishGuaranteedMessageToTopic(String topic, T value, String correlationData, SolaceSite site, SolaceProducerCallback callback, int retryCount, long timeout) throws SolaceException {
        LOGGER.info("Message Publishing to topic {}, site {} ", topic, site);
        int siteNo = site == SolaceSite.DEFAULT ? solaceDefaultProperties.getDefaultSiteNo() : site.getSiteNo();
        if (siteNo < 1) {
            throw new SolaceException("Invalid Site Number " + site, "Invalid Site Number!, Only single site messages could be produced through this method. Site Received is " + site.name());
        }
        publishGuaranteedMessageToTopic(siteNo, topic, value, correlationData, callback, solaceSessionService.getSession(siteNo).getSession(), retryCount, timeout);
    }


    public void publishGuaranteedMessageToTopic(Map<SolaceSite, String> topicMap, T value, String correlationData, SolaceProducerCallback callback, SolaceSite.Collections sites, int retryCount, long timeout) throws SolaceException {
        List<HSXJCSMPSession> sessionList = new ArrayList<>();

        if (sites == SolaceSite.Collections.ALL || sites == SolaceSite.Collections.ANY_ONE_SITE) {
            sessionList.add(solaceSessionService.getDefaultSession());
            sessionList.addAll(solaceSessionService.getAllExceptDefaultSessions());
        } else if (sites == SolaceSite.Collections.ALL_EXCEPT_CURRENT || sites == SolaceSite.Collections.ANY_EXCEPT_CURRENT) {
            sessionList.addAll(solaceSessionService.getAllExceptDefaultSessions());
        } else {
            for (SolaceSite site : topicMap.keySet()) {
                int siteNo = site == SolaceSite.DEFAULT ? solaceDefaultProperties.getDefaultSiteNo() : site.getSiteNo();
                sessionList.add(solaceSessionService.getSession(siteNo));
            }
        }

        if (sites != SolaceSite.Collections.ANY_ONE_SITE || sessionList.size() == 1) {
            for (HSXJCSMPSession session : sessionList) {
                publishGuaranteedMessageToTopic(session.getSiteNo(), topicMap.get(SolaceSite.getSite(session.getSiteNo())), value, correlationData, callback, session.getSession(), retryCount, timeout);
            }
        } else {
            publishToAnyOneSite(sessionList, 1, 2, topicMap, value, correlationData, callback, retryCount, timeout);
        }
    }

    private void publishGuaranteedMessageToTopic(int siteNo, String topicName, T value, String correlationData, SolaceProducerCallback callback, JCSMPSession session) throws SolaceException {
        try {
            LOGGER.info("Sending solace message to Topic {} Site {} callback {}", topicName, siteNo, callback.toString());
            XMLMessageProducer prod = session.getMessageProducer(new AbstractSolaceProducerCallback());
            final Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);
            TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            msg.setDeliveryMode(DeliveryMode.PERSISTENT);
            msg.setCorrelationKey(new SolaceCorrelationData(siteNo,topicName,correlationData,callback));
            sendMessage(prod, topic, msg, value);
        } catch (JCSMPException e) {
            throw new SolaceException(e.getMessage(), e.getExtraInfo());
        } catch (Exception e) {
            throw new SolaceException(e.getMessage(), null);
        }
    }


    public void sendMessage(XMLMessageProducer producer, AbstractDestination destination, TextMessage msg, T value) throws JCSMPException, JsonProcessingException {
        final String message = value instanceof String ? (String) value : JSONUtil.convertToJSONString(value);
        msg.setText(message);
        LOGGER.info("Solace Message Sent to Topic {} | Message {}", destination.getName(), message);
        producer.send(msg, destination);
    }

    private void publishGuaranteedMessageToTopic(int siteNo, String topicName, T value, String correlationData, SolaceProducerCallback callback, JCSMPSession session, int retryCount, long timeout) throws SolaceException {
        publishGuaranteedMessageToTopic(siteNo, topicName, value, correlationData, new SolaceProducerCallback() {
            @Override
            public void handleError(String topic, int siteNo, String correlationData, SolaceException e, long timeStamp) {
                LOGGER.error("Solace Message sending failed topic {} | Site {} | Message ID {} | Error {} | Extra info", topic, siteNo, correlationData, e.getMessage(), e.getExtraInfo());
                if (retryCount <= 0 || System.currentTimeMillis() > timeout) {
                    callback.handleError(topic, siteNo, correlationData, e, timeStamp);
                } else {
                    publishGuaranteedMessageToTopic(siteNo, topic, value, correlationData, callback, session, retryCount - 1, timeout);
                }
            }

            @Override
            public void success(String topic, int siteNo, String correlationData) {
                LOGGER.info("Solace Message Sent successfully topic {} | Site {} | message ID {} | callback {}", topic, siteNo, correlationData, callback.toString());
                callback.success(topic, siteNo, correlationData);
            }

            @Override
            public String toString() {
                return callback.toString();
            }
        }, session);
    }

    private void publishToAnyOneSite(List<HSXJCSMPSession> sessions, int currentSessionSiteNo, int nextSessionSiteNo, Map<SolaceSite, String> topicMap, T value, String correlationData, SolaceProducerCallback callback, int retryCount, long timeout) {
        publishGuaranteedMessageToTopic(currentSessionSiteNo, topicMap.get(SolaceSite.getSite(currentSessionSiteNo)), value, correlationData, new SolaceProducerCallback() {
            @Override
            public void handleError(String topic, int siteNo, String correlationData, SolaceException e, long timeStamp) {
                if (nextSessionSiteNo > sessions.size() || retryCount <= 0 || System.currentTimeMillis() > timeout) {
                    if (retryCount <= 0) {
                        e.setMessage("Retry count exceeded");
                    }
                    if (System.currentTimeMillis() > timeout) {
                        e.setMessage("Message Timeout Exception");
                    }
                    callback.handleError(topic, siteNo, correlationData, e, timeStamp);
                } else {
                    publishToAnyOneSite(sessions, currentSessionSiteNo + 1, nextSessionSiteNo + 1, topicMap, value, correlationData, callback, retryCount - 1, timeout);
                }
            }

            @Override
            public void success(String topic, int siteNo, String correlationData) {
                callback.success(topic, siteNo, correlationData);
            }
        }, sessions.get(currentSessionSiteNo - 1).getSession());
    }

    public List<SolaceSite> getSites(SolaceSite.Collections siteCollection) {
        List<SolaceSite> siteNos = new ArrayList<>();
        switch (siteCollection) {
            case ALL:
            case ANY_ONE_SITE:
                //todo : always add default site first
                siteNos.add(SolaceSite.getSite(solaceDefaultProperties.getDefaultSiteNo()));
            case ALL_EXCEPT_CURRENT:
            case ANY_EXCEPT_CURRENT:
                for (int i = 0; i < solaceDefaultProperties.getNodes().size(); i++) {
                    if (i + 1 != solaceDefaultProperties.getDefaultSiteNo()) {
                        siteNos.add(SolaceSite.getSite(i + 1));
                    }
                }
                break;
        }
        return siteNos;
    }
}
