package com.hsx.sa.service.impl.out.operational;


import com.fasterxml.jackson.core.type.TypeReference;
import com.hsx.common.model.cache.HSXHealthCheckCache;
import com.hsx.common.model.cache.HSXProcessorStatusCache;
import com.hsx.common.model.constants.Formats;
import com.hsx.common.model.constants.ServiceRegistryType;
import com.hsx.common.model.error.ErrorLog;
import com.hsx.common.model.hsx.ServiceRegistry;
import com.hsx.common.model.response.HSXMessage;
import com.hsx.sa.service.AbstractService;
import com.hsx.solace.util.JSONUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("processorStatusService")
public class ProcessorStatusImpl extends AbstractService<Void, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorStatusImpl.class);

    @Override
    @ServiceActivator(inputChannel = "processorStatusMessageChannel")
    protected Message<Void> executeService(Message<String> msg) {
        try {
            HSXMessage<ServiceRegistry> serviceRegistryHSXMessage = JSONUtil.convertToObject(msg.getPayload(), new TypeReference<HSXMessage<ServiceRegistry>>() {
            });
            ServiceRegistry serviceRegistry = serviceRegistryHSXMessage.getData();
            List<ServiceRegistry> currentlyUpProcessors = HSXProcessorStatusCache.INSTANCE.findAllByStatus(serviceRegistry.getServiceName(), true);

            if (currentlyUpProcessors != null && !currentlyUpProcessors.isEmpty()) {
                HSXProcessorStatusCache.INSTANCE.getCache().put(serviceRegistry.getNodeName(), serviceRegistry);
            } else {
                if (ServiceRegistryType.PRO.name().equals(serviceRegistry.getServiceName())) {
                    HSXProcessorStatusCache.INSTANCE.getCache().put(serviceRegistry.getNodeName(), serviceRegistry);
                    currentlyUpProcessors = HSXProcessorStatusCache.INSTANCE.findAllByStatus(serviceRegistry.getServiceName(), true);
                    if (currentlyUpProcessors == null || currentlyUpProcessors.isEmpty()) {
                        HSXHealthCheckCache.INSTANCE.getCache().put(serviceRegistry.getServiceName(), false);
                    } else {
                        HSXHealthCheckCache.INSTANCE.getCache().put(serviceRegistry.getServiceName(), true);
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error(Formats.ERROR_OCCURRED_AT___LOG_DETAIL___ERROR_DETAIL___STACKTRACE___,
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    new ErrorLog.Builder("Processor Status Service")
                            .addPayload(msg).build(),
                    ExceptionUtils.getStackTrace(e)
            );
        }
        return null;
    }
}

