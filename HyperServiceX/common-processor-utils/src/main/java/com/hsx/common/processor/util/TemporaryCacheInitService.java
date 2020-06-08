package com.hsx.common.processor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

//TODO TO BE DELETED
@Service
public class TemporaryCacheInitService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemporaryCacheInitService.class);

    private final AtomicBoolean atomic = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!this.atomic.get()) {
            try {
                atomic.set(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
