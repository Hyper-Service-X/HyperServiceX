package com.hsx.processor.scheduler;

import com.hsx.common.model.constants.MessageType;
import com.hsx.common.model.hsx.ServiceRegistry;
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

@Component("proStatusBroadcastScheduler")
public class PROStatusBroadcastScheduler implements DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(PROStatusBroadcastScheduler.class);

    private static final String fromTime = DateUtil.localDateTimeWithMillis();

    @Value("${NODE.NAME}")
    private String NODE_NAME;

    @Value("${SERVICE.NAME}")
    private String SERVICE_NAME;
    @Autowired
    SolaceMessageProducerService<HSXMessage> solaceMessageProducerService;

    @Autowired
    private MessageTopicsUtil messagingTopicsUtil;

    @Scheduled(fixedDelayString = "${PROCESSOR_STATUS.BROADCAST.INTERVAL}")
    public void broadCast() {
        ServiceRegistry serviceRegistry = new ServiceRegistry(SERVICE_NAME, NODE_NAME, true);
        serviceRegistry.setLastAvailableTime(DateUtil.localDateTimeWithMillis());
        serviceRegistry.setStatusFrom(fromTime);
        HSXMessage status = new HSXMessage(serviceRegistry, null, null);
        sendMessage(status);
    }

    private void sendMessage(HSXMessage status) {
        MessageTopic topic = messagingTopicsUtil.getTopic(MessageType.PROCESSOR_STATUS, MessageSite.DEFAULT, null);
        solaceMessageProducerService.publishPersistedMessage(topic, status);
    }

    @Override
    public void destroy() throws Exception {
        ServiceRegistry serviceRegistry = new ServiceRegistry(SERVICE_NAME, NODE_NAME, true);
        serviceRegistry.setLastAvailableTime(DateUtil.localDateTimeWithMillis());
        serviceRegistry.setStatusFrom(fromTime);
        HSXMessage status = new HSXMessage(serviceRegistry, null, null);
        sendMessage(status);
    }

}
