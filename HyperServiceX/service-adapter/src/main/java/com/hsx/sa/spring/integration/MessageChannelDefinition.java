package com.hsx.sa.spring.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;

/**
 * Created by
 *
 * @author Nadith on 15/6/2020
 * Modified by
 * @author ... on ... : Modified Reason is ...
 * Usage :
 * Assign Task Executors to ExecutorChannels
 */
@Configuration
@EnableIntegration
public class MessageChannelDefinition {


    @Autowired
    private TaskExecutor operationalMessageTaskExecutor;
    @Autowired
    private TaskExecutor commonOperationalTaskExecutor;

    //This ExecutorChannel is a subscribable point-to-point channel which is more suitable in our case. Message handling is performed by
    //different threads controlled by a Task Executor
    /* Other than that we have Queue Channels,Priority Channel,Rendezvous Channels, Direct Channels*/
    @Bean
    public MessageChannel operationalMessageChannel() {
        return new ExecutorChannel(operationalMessageTaskExecutor);
    }

    @Bean
    public MessageChannel processorStatusMessageChannel() {
        return new ExecutorChannel(commonOperationalTaskExecutor);
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //Error channels are publish-subscribable channels where we can listen from any where
    @Bean
    public MessageChannel processorStatusErrorChannel() {
        return new PublishSubscribeChannel();
    }

    /*
    use this DirectChannel to point to point channels (Command Messages,Document Messages,Request-Reply Messages) - Transactional Integrity
    @Bean
    public MessageChannel schemaValidationChannel() {
        return new DirectChannel();
    }

    use this PublishSubscribeChannel to publish-subscribe channels (Event Messages)

    @Bean
    public MessageChannel testChannel() {
        return new PublishSubscribeChannel();
    }


    Define a queue channel – queue size.this is a pollable channel. Durable queue
    @Bean
    public MessageChannel queueChannel() {
        return new QueueChannel(10);
    }

    Define a Priority Queue channel
    @Bean
    public MessageChannel priorityQueueChannel() {
        //can add a comparator to handle the order here it will process based on the acs order of hash code
        return new PriorityChannel(Comparator.comparingLong((Message<?> m) -> m.getPayload().hashCode()));
    }

     */
}
