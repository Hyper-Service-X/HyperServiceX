package com.hsx.sa.scheduler;

import com.hsx.sa.logger.MonitorLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Component
public class SAMonitoringThread {


    @Autowired
    private MonitorLogger logger;

    @Autowired
    ThreadPoolTaskExecutor mqPutTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor RESTPutTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor messageSigningTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor assignGatewayTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor resProcessingTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor creditTransferOutTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor paymentStatusOutTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor syncMqaTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor schemaValidationTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor signatureValidationTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor commonMessageInRoutingTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor transactionalMessageInRoutingTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor adminMessageInRoutingTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor operationalMessageInRoutingTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor paymentCancellationReqTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor paymentCancellationResTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor resolutionOfInvestigationResTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor proxyLookUpReqTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor proxyLookUpResTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor transactionalMessageOutRoutingTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor adminMessageOutRoutingTaskExecutor;

    @Autowired
    ThreadPoolTaskExecutor operationalMessageOutRoutingTaskExecutor;


    private int maxThreadshold = 30;

    List<ThreadPoolTaskExecutor> maThreadPool = new ArrayList<ThreadPoolTaskExecutor>();
    private static final Logger LOGGER = LoggerFactory.getLogger(SAMonitoringThread.class);


    //@Scheduled(fixedDelayString = "${MONITOR.REFRESH.DELAY}")
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
//        maThreadPool.add(mqMessageListenerTaskExecutor);
//        maThreadPool.add(mqMessagePutTaskExecutor);
        maThreadPool.add(mqPutTaskExecutor);
        maThreadPool.add(RESTPutTaskExecutor);
        maThreadPool.add(messageSigningTaskExecutor);
        maThreadPool.add(assignGatewayTaskExecutor);
        maThreadPool.add(resProcessingTaskExecutor);
        maThreadPool.add(creditTransferOutTaskExecutor);
        maThreadPool.add(paymentStatusOutTaskExecutor);
        maThreadPool.add(syncMqaTaskExecutor);
        maThreadPool.add(schemaValidationTaskExecutor);
        maThreadPool.add(signatureValidationTaskExecutor);
        maThreadPool.add(commonMessageInRoutingTaskExecutor);
        maThreadPool.add(transactionalMessageInRoutingTaskExecutor);
        maThreadPool.add(adminMessageInRoutingTaskExecutor);
        maThreadPool.add(operationalMessageInRoutingTaskExecutor);
        maThreadPool.add(paymentCancellationReqTaskExecutor);
        maThreadPool.add(paymentCancellationResTaskExecutor);
        maThreadPool.add(resolutionOfInvestigationResTaskExecutor);
        maThreadPool.add(proxyLookUpReqTaskExecutor);
        maThreadPool.add(proxyLookUpResTaskExecutor);
        maThreadPool.add(transactionalMessageOutRoutingTaskExecutor);
        maThreadPool.add(adminMessageOutRoutingTaskExecutor);
        maThreadPool.add(operationalMessageOutRoutingTaskExecutor);
    }
}
