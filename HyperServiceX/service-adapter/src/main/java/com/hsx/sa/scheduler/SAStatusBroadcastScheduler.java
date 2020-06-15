package com.hsx.sa.scheduler;

import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.constants.ServiceRegistryType;
import com.hsx.common.model.hsx.ServiceRegistry;
import com.hsx.common.model.response.HSXMessage;
import com.hsx.common.util.messaging.MessageProducerService;
import com.hsx.common.util.messaging.MessageSite;
import com.hsx.common.util.messaging.MessageTopicsUtil;
import com.hsx.common.util.util.DateUtil;
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

    @Value("${NODE.NAME}")
    private String NODE_NAME;
    @Value("${NODE.SITE.NO}")
    private int LOCAL_SITE_NO;

    @Autowired
    private MessageTopicsUtil messagingTopicsUtil;

    @Autowired
    private MessageProducerService<HSXMessage> hsxMessageMessageProducerService;

    private static final String fromTime = DateUtil.localDateTimeWithMillis();

    @Scheduled(fixedDelayString = "${MA_STATUS.BROADCAST.INTERVAL}")
    public void broadCast() {
        ServiceRegistry serviceRegistry = new ServiceRegistry(ServiceRegistryType.SA.name(), NODE_NAME, true);
        serviceRegistry.setLastAvailableTime(DateUtil.localDateTimeWithMillis());
        serviceRegistry.setStatusFrom(fromTime);
        HSXMessage status = new HSXMessage(serviceRegistry, null, null);
        sendMessage(status);
    }

    @Override
    public void destroy() throws Exception {
        ServiceRegistry serviceRegistry = new ServiceRegistry(ServiceRegistryType.SA.name(), NODE_NAME, false);
        serviceRegistry.setLastAvailableTime(DateUtil.localDateTimeWithMillis());
        serviceRegistry.setStatusFrom(fromTime);
        HSXMessage status = new HSXMessage(serviceRegistry, null, null);
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
