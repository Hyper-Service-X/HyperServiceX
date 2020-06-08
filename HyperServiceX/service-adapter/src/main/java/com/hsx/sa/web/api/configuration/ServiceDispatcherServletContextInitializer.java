package com.hsx.sa.web.api.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

@Order(3)
public class ServiceDispatcherServletContextInitializer extends AbstractDispatcherServletInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDispatcherServletContextInitializer.class);

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        LOGGER.info("Initializing ServiceDispatcherServletContextInitializer");
        AnnotationConfigWebApplicationContext systemServletWebContext = new AnnotationConfigWebApplicationContext();
        systemServletWebContext.register(ServiceServletConfig.class);
        return systemServletWebContext;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/sg-xb-xb/*"};
    }

    @Override
    protected String getServletName() {
        return "service-dispatcher";
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return null;
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(new MultipartConfigElement("/tmp"));
    }
}
