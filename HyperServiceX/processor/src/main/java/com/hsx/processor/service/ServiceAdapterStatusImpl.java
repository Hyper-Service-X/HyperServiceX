package com.hsx.processor.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.hsx.common.model.constants.Formats;
import com.hsx.common.model.error.ErrorLog;
import com.hsx.common.model.hsx.ServiceRegistry;
import com.hsx.common.model.response.HSXMessage;
import com.hsx.common.processor.service.AbstractService;
import com.hsx.solace.util.JSONUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("serviceAdapterStatus")
public class ServiceAdapterStatusImpl extends AbstractService<Void, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAdapterStatusImpl.class);

    @Override
    @ServiceActivator(inputChannel = "saStatusMessageChannel")
    protected Message<Void> executeService(Message<String> msg) {
        try {
            HSXMessage<ServiceRegistry> serviceRegistryHSXMessage = JSONUtil.convertToObject(msg.getPayload(), new TypeReference<HSXMessage<ServiceRegistry>>() {
            });
            ServiceRegistry serviceRegistry = serviceRegistryHSXMessage.getData();
            LOGGER.info("Service Adapter Status Processing: " + serviceRegistry.toString());

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

