package com.hsx.common.util.messaging;

import java.util.List;

public interface MessageProducerService<T> {
    public void publishPersistedMessage(MessageTopic topic, T value, MessageProducerCallback callback) throws MessagingException;

    public void publishPersistedMessage(MessageTopic topic, T value) throws MessagingException ;

    public void publishPersistedMessage(MessageTopic topic, T value, MessageSite site) throws MessagingException ;

    public void publishPersistedMessage(MessageTopic topic, T value, MessageSite.Collections siteCollection) throws MessagingException ;

    public void publishPersistedMessage(MessageTopic topic, T value, String correlationData, MessageSite site, MessageProducerCallback callback) throws MessagingException;

    public void publishPersistedMessage(MessageTopic topic, T value, String correlationData, MessageProducerCallback callback) throws MessagingException;

    public void publishPersistedMessage(MessageTopic topic, T value, String correlationData, MessageSite.Collections siteCollection, MessageProducerCallback callback) throws MessagingException ;

    public void  publishPersistedMessageToMultipleSites(List<MessageTopic> topics, T value, String correlationData, MessageSite.Collections site, MessageProducerCallback callback) throws MessagingException;
}
