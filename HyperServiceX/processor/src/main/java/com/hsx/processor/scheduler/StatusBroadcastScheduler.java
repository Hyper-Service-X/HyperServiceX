package com.hsx.processor.scheduler;

import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.constants.ServiceRegistryType;
import com.hsx.common.model.hsx.ServiceRegistry;
import com.hsx.common.model.request.Header;
import com.hsx.common.model.response.HSXMessage;
import com.hsx.common.processor.messaging.producer.SolaceMessageProducerService;
import com.hsx.common.util.messaging.MessageSite;
import com.hsx.common.util.messaging.MessageTopic;
import com.hsx.common.util.messaging.MessageTopicsUtil;
import com.hsx.common.util.util.DateUtil;
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

    private static final String fromTime = DateUtil.localDateTimeWithMillis();

    @Value("${NODE.SITE.NO}")
    private int LOCAL_SITE_NO;

    @Value("${NODE.NO}")
    private int CURRENT_NODE_NO;

    @Autowired
    SolaceMessageProducerService<HSXMessage> solaceMessageProducerService;

    @Autowired
    private MessageTopicsUtil messagingTopicsUtil;

    @Scheduled(fixedDelayString = "${PROCESSOR_STATUS.BROADCAST.INTERVAL}")
    public void broadCast() {
        ServiceRegistry serviceRegistry = new ServiceRegistry(ServiceRegistryType.PRO.name(), LOCAL_SITE_NO, CURRENT_NODE_NO, true);
        HSXMessage status = new HSXMessage(serviceRegistry, new Header.Builder().build(), null);
        sendMessage(status);
    }

    private void sendMessage(HSXMessage status) {
        MessageTopic topic = messagingTopicsUtil.getTopic(MessageType.PROCESSOR_STATUS, MessageSite.DEFAULT, null);
        solaceMessageProducerService.publishPersistedMessage(topic, status);
    }

    @Override
    public void destroy() throws Exception {
        ServiceRegistry serviceRegistry = new ServiceRegistry(ServiceRegistryType.PRO.name(), LOCAL_SITE_NO, CURRENT_NODE_NO, false);
        HSXMessage status = new HSXMessage(serviceRegistry, new Header.Builder().build(), null);
        sendMessage(status);
    }

}
