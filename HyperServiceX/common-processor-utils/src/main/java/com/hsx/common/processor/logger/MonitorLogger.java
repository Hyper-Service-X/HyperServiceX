package com.hsx.common.processor.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("monitorLogger")
public class MonitorLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger("MONITOR_LOG");

      public void info(String message) {
        if (message != null) {
            message = message.replaceAll(">[\\s]+?<", "><");
            LOGGER.info(":" + message);
        }
    }

    public void error(String className, String methodName, String message, Throwable e) {
        LOGGER.error(message + ":" + e.getMessage(), e);
    }
}
