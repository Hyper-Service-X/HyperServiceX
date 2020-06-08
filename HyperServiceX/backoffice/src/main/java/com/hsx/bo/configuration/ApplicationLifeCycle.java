package com.hsx.bo.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLifeCycle implements SmartLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationLifeCycle.class);

    @Override
    public void start() {
        LOGGER.info("Service starting...");
    }

    @Override
    public void stop() {
        LOGGER.info("Service stopping...");
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public boolean isAutoStartup() {
        return false;
    }

    @Override
    public void stop(final Runnable callback) {
        LOGGER.info("Shutdown initiated...");

        //try {
        //Stop listening to the queue.

        //Shudown complete. Regular shutdown will continue.
        callback.run();
        //} catch (final InterruptedException e) {
        //Looks like we got exception while shutting down,
        //log it or do something with it
        //}
    }

    /**
     * This is the most important method.
     * Returning Integer.MAX_VALUE only suggests that
     * we will be the first bean to shutdown and last bean to start
     */
    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
