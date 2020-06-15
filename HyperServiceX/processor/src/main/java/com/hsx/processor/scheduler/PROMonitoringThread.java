package com.hsx.processor.scheduler;

import com.hsx.common.processor.logger.MonitorLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Component
public class PROMonitoringThread {


    @Autowired
    private MonitorLogger logger;

    @Autowired
    ThreadPoolTaskExecutor operationalMessageTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor commonOperationalTaskExecutor;


    private int maxThreadshold = 30;

    List<ThreadPoolTaskExecutor> maThreadPool = new ArrayList<ThreadPoolTaskExecutor>();
    private static final Logger LOGGER = LoggerFactory.getLogger(PROMonitoringThread.class);


    @Scheduled(fixedDelayString = "${MONITOR.REFRESH.DELAY}")
    public void monitorMessageAdaptorThreadPool() {
        showThreadPoolCount();
    }

    private void showThreadPoolCount() {
        logger.info("********************************************");
        maThreadPool.forEach(threadPool -> {
            if (threadPool != null) {
                int queueSize = threadPool.getThreadPoolExecutor().getQueue().size();
                int remainingSize = threadPool.getThreadPoolExecutor().getQueue().remainingCapacity();
                //int total = queueSize + remainingSize;

                logger.info(threadPool.getThreadNamePrefix() +
                        "::Active-Count::" + threadPool.getActiveCount() +
                        "::Pool-Size::" + threadPool.getPoolSize() +
                        "::Core-Pool-Size::" + threadPool.getPoolSize() +
                        "::Queue-Size::" + queueSize +
                        "::Remaining-Size::" + remainingSize);
            }
        });
    }

    @PostConstruct
    public void init() {
        maThreadPool.add(operationalMessageTaskExecutor);
        maThreadPool.add(commonOperationalTaskExecutor);
    }
}
