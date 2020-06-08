package com.hsx.bo.scheduler;

import com.hsx.bo.service.GlobalCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class BOResponseMonitorThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(BOResponseMonitorThread.class);

    @Value("${TOTAL.SITE.COUNT}")
    private int TOTAL_SITE_COUNT;

    @Scheduled(fixedDelayString = "${BO.MONITOR.PERIOD}")
    public void monitorResponses() {
        for (String jobId : GlobalCache.boRequestMap.keySet()) {
//            XbBoOperationDTO requestDTO = xbBoOperationDAO.getByJobId(jobId);
//            if (requestDTO == null) {
//                GlobalCache.boRequestMap.remove(jobId);
//            } else {
//                List<XbBoOperationDetailDTO> pendingRes = xbBoOperationDetailDAO.getAllByStatusAndJobId(jobId, com.hsx.common.model.Status.BORequestDetail.SENT);
//                if (pendingRes == null || pendingRes.size() == 0) {
//                    LOGGER.error("All Processor Response Received. J- "+jobId);
//                    CompletableFuture<Status> completableFuture = GlobalCache.boRequestMap.get(jobId);
//                    GlobalCache.boRequestMap.remove(jobId);
//                    List<XbBoOperationDetailDTO> allRequests = xbBoOperationDetailDAO.getAllByJobId(jobId);
//                    List<XbBoOperationDetailDTO> failedRes = xbBoOperationDetailDAO.getAllByStatusAndJobId(jobId, com.hsx.common.model.Status.BORequestDetail.FAIL);
//
//                    if (completableFuture != null) {
//                        if (failedRes.size() == 0) {
//                            LOGGER.error("All Processor Response Overall Status - "+ Constants.COMMON.SUCCESS+" J- "+jobId);
//                            completableFuture.complete(Status.SUCCESS);
//                        }
//                        else if (failedRes.size() == allRequests.size()){
//                            LOGGER.error("All Processor Response Overall Status. - "+ Constants.COMMON.FAILURE+" J- "+jobId);
//                            completableFuture.complete(Status.ERROR);
//                        }
//                        else {
//                            LOGGER.error("All Processor Response Overall Status. - "+ Constants.COMMON.PARTIAL_SUCCESS+" J- "+jobId);
//                            completableFuture.complete(Status.PARTIAL_SUCCESS);
//                        }
//                    }
//                }
//            }
        }
    }
}
