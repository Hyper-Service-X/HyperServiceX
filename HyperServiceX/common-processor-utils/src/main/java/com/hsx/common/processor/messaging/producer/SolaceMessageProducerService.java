package com.hsx.common.processor.messaging.producer;

import com.hsx.common.util.messaging.*;
import com.hsx.solace.SolaceException;
import com.hsx.solace.annotations.SolaceSite;
import com.hsx.solace.producer.SolaceMessageProducer;
import com.hsx.solace.producer.SolaceProducerCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@ConditionalOnProperty(name = "hsx.messaging.service", havingValue = "solace", matchIfMissing = true)
public class SolaceMessageProducerService<T> extends SolaceMessageProducer<T> implements MessageProducerService<T> {

    @Value("${NODE.SITE.NO}")
    private int currentSiteNo;

    @Value("${NODE.NO}")
    private int currentNode;

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
    public void publishPersistedMessage(MessageTopic topic, T value, String correlationData, MessageSite site, MessageProducerCallback callback) throws MessagingException {
        this.publishGuaranteedMessageToTopic(messagingTopicsUtil.generateTopicString(topic), value, correlationData, SolaceSite.valueOf(site.name()), new ConversionCallback(callback), 0, Long.MAX_VALUE);
    }

    @Override
    public void publishPersistedMessage(MessageTopic topic, T value, String correlationData, MessageProducerCallback callback) throws MessagingException {

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

        this.publishGuaranteedMessageToTopic(topicsMap, value, null, new ConversionCallback(callback), SolaceSite.Collections.valueOf(site.name()), 0, Long.MAX_VALUE);

    }


    private class ConversionCallback implements SolaceProducerCallback {
        private MessageProducerCallback callback;

        public ConversionCallback(MessageProducerCallback callback) {
            this.callback = callback;
        }

        @Override
        public void handleError(String topic, int siteNo, String messageId, SolaceException e, long timeStamp) {
            //todo:
            callback.handleError(topic, siteNo, messageId, new MessagingException(), timeStamp);
        }

        @Override
        public void success(String topic, int siteNo, String messageId) {
            callback.success(topic, siteNo, messageId);
        }
    }
}
