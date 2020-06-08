package com.hsx.sa.web.api.configuration.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;


@Component
public class LoggerFilter extends AbstractRequestLoggingFilter {


    public LoggerFilter() {
        super();
        this.setBeforeMessagePrefix("Request Received ");
        this.setAfterMessagePrefix("Response Generated ");
        this.setIncludeHeaders(true);
        this.setIncludePayload(true);
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }
}
