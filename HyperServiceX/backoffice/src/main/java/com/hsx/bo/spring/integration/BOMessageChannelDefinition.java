package com.hsx.bo.spring.integration;

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
 * @author Nadith on 26/5/2020
 * Modified by
 * @author ... on ... : Modified Reason is ...
 * Usage :
 */
@Configuration
@EnableIntegration
public class BOMessageChannelDefinition {

    @Autowired
    private TaskExecutor messageRoutingTaskExecutor;

    @Autowired
    private TaskExecutor boRQTaskExecutor;

    @Autowired
    private TaskExecutor boRSTaskExecutor;


    //This ExecutorChannel is a subscribable point-to-point channel which is more suitable in our case. Message handling is performed by
    //different threads controlled by a Task Executor
    /* Other than that we have Queue Channels,Priority Channel,Rendezvous Channels, Direct Channels*/
    @Bean
    public MessageChannel messageRoutingChannel() {
        return new ExecutorChannel(messageRoutingTaskExecutor);
    }

    @Bean
    public MessageChannel boRQChannel() {
        return new ExecutorChannel(boRQTaskExecutor);
    }

    @Bean
    public MessageChannel boRSChannel() {
        return new ExecutorChannel(boRSTaskExecutor);
    }

    @Bean
    public MessageChannel unRecoverableErrorChannel() {
        return new PublishSubscribeChannel();
    }

}

