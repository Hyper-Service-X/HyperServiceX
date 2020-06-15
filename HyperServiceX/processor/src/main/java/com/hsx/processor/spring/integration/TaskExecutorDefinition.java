package com.hsx.processor.spring.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by
 *
 * @author Nadith on 15/6/2020
 * Modified by
 * @author ... on ... : Modified Reason is ...
 * Usage :
 */
@Configuration
public class TaskExecutorDefinition {


    @Value("${operationalMessageTaskExecutor.corePoolSize}")
    private int operationalMessageTaskExecutorCorePoolSize;
    @Value("${operationalMessageTaskExecutor.maxPoolSize}")
    private int operationalMessageTaskExecutorMaxPoolSize;
    @Value("${operationalMessageTaskExecutor.queueCapacity}")
    private int operationalMessageTaskExecutorQueueCapacity;

    @Value("${commonOperationalTaskExecutor.corePoolSize}")
    private int commonOperationalTaskExecutorCorePoolSize;
    @Value("${commonOperationalTaskExecutor.maxPoolSize}")
    private int commonOperationalTaskExecutorMaxPoolSize;
    @Value("${commonOperationalTaskExecutor.queueCapacity}")
    private int commonOperationalTaskExecutorQueueCapacity;


    @Bean
    public ThreadPoolTaskExecutor operationalMessageTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(operationalMessageTaskExecutorCorePoolSize);
        executor.setMaxPoolSize(operationalMessageTaskExecutorMaxPoolSize);
        executor.setQueueCapacity(operationalMessageTaskExecutorQueueCapacity);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor commonOperationalTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(commonOperationalTaskExecutorCorePoolSize);
        executor.setMaxPoolSize(commonOperationalTaskExecutorMaxPoolSize);
        executor.setQueueCapacity(commonOperationalTaskExecutorQueueCapacity);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

}
