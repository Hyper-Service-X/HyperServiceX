package com.hsx.processor.scheduler;

import com.bcs.xborder.common.util.messaging.MessageTopicsUtil;
import com.bcs.xborder.common.util.util.DateUtil;
import com.hsx.common.model.response.HSXMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("statusBroadcastScheduler")
public class StatusBroadcastScheduler implements DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusBroadcastScheduler.class);

    @Autowired
    private MessageTopicsUtil messagingTopicsUtil;

    private static final String fromTime = DateUtil.localDateTimeWithMillis();

    @Value("${NODE.SITE.NO}")
    private int LOCAL_SITE_NO;

    @Value("${NODE.NO}")
    private int CURRENT_NODE_NO;

    @Scheduled(fixedDelayString = "${PROCESSOR_STATUS.BRAODCAST.INTERVAL}")
    public void broadCast() {

    }

    private void sendMessage(HSXMessage status) {
    }

    @Override
    public void destroy() throws Exception {

    }

}
