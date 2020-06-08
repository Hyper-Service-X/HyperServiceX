package com.hsx.sa.scheduler;

import com.bcs.xborder.common.util.messaging.MessageProducerService;
import com.bcs.xborder.common.util.messaging.MessageSite;
import com.bcs.xborder.common.util.messaging.MessageTopicsUtil;
import com.bcs.xborder.common.util.util.DateUtil;
import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.response.HSXMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("saStatusBroadcastScheduler")
public class SAStatusBroadcastScheduler implements DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SAStatusBroadcastScheduler.class);

    @Value("${NODE.NO}")
    private int NODE_NAME;
    @Value("${NODE.SITE.NO}")
    private int LOCAL_SITE_NO;

    @Autowired
    private MessageTopicsUtil messagingTopicsUtil;

    @Autowired
    private MessageProducerService<HSXMessage> hsxMessageMessageProducerService;

    private static final String fromTime = DateUtil.localDateTimeWithMillis();

    @Scheduled(fixedDelayString = "${MA_STATUS.BRAODCAST.INTERVAL}")
    public void broadCast() {
        HSXMessage status = new HSXMessage();
        sendMessage(status);
    }

    @Override
    public void destroy() throws Exception {
        HSXMessage status = new HSXMessage();
        sendMessage(status);
    }

    private void sendMessage(HSXMessage status) {
        try {
            hsxMessageMessageProducerService.publishPersistedMessage(
                    messagingTopicsUtil.getTopic(
                            MessageType.SA_STATUS,
                            MessageSite.DEFAULT,
                            null
                    ),
                    status
            );
        } catch (Exception e) {
            LOGGER.error("Unable to send the SA status.");
        }
    }
}
