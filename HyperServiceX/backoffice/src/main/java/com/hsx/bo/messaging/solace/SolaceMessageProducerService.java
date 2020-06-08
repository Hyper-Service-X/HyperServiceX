package com.hsx.bo.messaging.solace;

import com.bcs.xborder.common.util.messaging.*;
import com.hsx.solace.SolaceException;
import com.hsx.solace.annotations.SolaceSite;
import com.hsx.solace.producer.SolaceMessageProducer;
import com.hsx.solace.producer.SolaceProducerCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@ConditionalOnProperty(name = "ngs.messaging.service", havingValue = "solace", matchIfMissing = true)
public class SolaceMessageProducerService<T> extends SolaceMessageProducer<T> implements MessageProducerService<T> {

    @Value("${NODE.SITE.NO}")
    private int currentSiteNo;

    @Autowired
    private MessageTopicsUtil messagingTopicsUtil;

    @Override
    public void publishPersistedMessage(MessageTopic topic, T value, MessageProducerCallback callback) throws MessagingException {
        this.publishGuaranteedMessageToTopic(messagingTopicsUtil.generateTopicString(topic), value, null, new ConversionCallback(callback));
    }

    @Override
    public void publishPersistedMessage(MessageTopic topic, T value) throws MessagingException {
        this.publishGuaranteedMessageToTopic(messagingTopicsUtil.generateTopicString(topic), value, null);
    }

    @Override
    public void publishPersistedMessage(MessageTopic topic, T value, MessageSite site) throws MessagingException {
        this.publishGuaranteedMessageToTopic(messagingTopicsUtil.generateTopicString(topic), value, null, SolaceSite.valueOf(site.name()), 1, Long.MAX_VALUE);
    }

    @Override
    public void publishPersistedMessage(MessageTopic topic, T value, MessageSite.Collections siteCollection) throws MessagingException {
        publishPersistedMessage(topic, value, null, siteCollection, new DoNothingProducerCallback());
    }

    @Override
    public void publishPersistedMessage(MessageTopic topic, T value, String correlationData, MessageSite site, MessageProducerCallback callback) throws MessagingException {
        this.publishGuaranteedMessageToTopic(messagingTopicsUtil.generateTopicString(topic), value, null, SolaceSite.valueOf(site.name()), new ConversionCallback(callback), 0, Long.MAX_VALUE);
    }

    @Override
    public void publishPersistedMessage(MessageTopic topic, T value, String correlationData, MessageProducerCallback callback) throws MessagingException {
        this.publishGuaranteedMessageToTopic(messagingTopicsUtil.generateTopicString(topic), value, correlationData, new ConversionCallback(callback));
    }

    @Override
    public void publishPersistedMessage(MessageTopic topic, T value, String correlationData, MessageSite.Collections siteCollection, MessageProducerCallback callback) throws MessagingException {
        List<SolaceSite> sites = getSites(SolaceSite.Collections.valueOf(siteCollection.name()));
        List<MessageTopic> topics = new ArrayList<>();
        for (SolaceSite site : sites) {
            topics.add(
                    new MessageTopic(
                            topic.getTopicTemplate(),
                            topic.getMessageType(),
                            topic.getMessageOperation(),
                            topic.getDirection(),
                            topic.getVersion(),
                            MessageSite.valueOf(site.name()),
                            topic.getMessageEntryPoint(),
                            topic.getCustomValues()
                    )
            );
        }
        publishPersistedMessageToMultipleSites(topics, value, correlationData, siteCollection, callback);
    }

    @Override
    public void publishPersistedMessageToMultipleSites(List<MessageTopic> topics, T value, String correlationData, MessageSite.Collections site, MessageProducerCallback callback) throws MessagingException {
        Map<SolaceSite, String> topicsMap = new HashMap<>();
        for (MessageTopic topic : topics) {
            topicsMap.put(
                    SolaceSite.valueOf(topic.getDestinationSite().name()),
                    messagingTopicsUtil.generateTopicString(topic)
            );
        }

        this.publishGuaranteedMessageToTopic(topicsMap, value, correlationData, new ConversionCallback(callback), SolaceSite.Collections.valueOf(site.name()), 0, Long.MAX_VALUE);
    }

    private class ConversionCallback implements SolaceProducerCallback {
        private MessageProducerCallback callback;
        private Logger logger = LoggerFactory.getLogger(ConversionCallback.class);

        public ConversionCallback(MessageProducerCallback callback) {
            this.callback = callback;
        }

        @Override
        public void handleError(String topic, int siteNo, String correlationData, SolaceException e, long timeStamp) {
            //todo:
            callback.handleError(topic, siteNo, correlationData, new MessagingException(), timeStamp);
        }

        @Override
        public void success(String topic, int siteNo, String correlationDAta) {
            callback.success(topic, siteNo, correlationDAta);
        }
    }
}
