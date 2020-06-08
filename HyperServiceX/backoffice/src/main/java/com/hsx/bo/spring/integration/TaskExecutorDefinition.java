package com.hsx.bo.spring.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskExecutorDefinition {

    @Value("${messageRoutingTaskExecutor.corePoolSize}")
    private int messageRoutingTaskExecutorCorePoolSize;
    @Value("${messageRoutingTaskExecutor.maxPoolSize}")
    private int messageRoutingTaskExecutorMaxPoolSize;
    @Value("${messageRoutingTaskExecutor.queueCapacity}")
    private int messageRoutingTaskExecutorQueueCapacity;

    @Value("${boRQTaskExecutor.corePoolSize}")
    private int boRQTaskExecutorCorePoolSize;
    @Value("${boRQTaskExecutor.maxPoolSize}")
    private int boRQTaskExecutorMaxPoolSize;
    @Value("${boRQTaskExecutor.queueCapacity}")
    private int boRQTaskExecutorQueueCapacity;

    @Value("${boRSTaskExecutor.corePoolSize}")
    private int boRSTaskExecutorCorePoolSize;
    @Value("${boRSTaskExecutor.maxPoolSize}")
    private int boRSTaskExecutorMaxPoolSize;
    @Value("${boRSTaskExecutor.queueCapacity}")
    private int boRSTaskExecutorQueueCapacity;

    @Bean
    public TaskExecutor messageRoutingTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(messageRoutingTaskExecutorCorePoolSize);
        executor.setMaxPoolSize(messageRoutingTaskExecutorMaxPoolSize);
        executor.setQueueCapacity(messageRoutingTaskExecutorQueueCapacity);
        executor.initialize();
        return executor;
    }

    @Bean
    public TaskExecutor boRQTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(boRQTaskExecutorCorePoolSize);
        executor.setMaxPoolSize(boRQTaskExecutorMaxPoolSize);
        executor.setQueueCapacity(boRQTaskExecutorQueueCapacity);
        executor.initialize();
        return executor;
    }

    @Bean
    public TaskExecutor boRSTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(boRSTaskExecutorCorePoolSize);
        executor.setMaxPoolSize(boRSTaskExecutorMaxPoolSize);
        executor.setQueueCapacity(boRSTaskExecutorQueueCapacity);
        executor.initialize();
        return executor;
    }
}
