package com.hsx.sa.web.api.configuration;

import com.hsx.sa.configuration.MAdaptorApplicationConfiguration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletException;


public class MQAdaptorWebApplicationConfiguration implements WebApplicationInitializer {

    @Override
    public void onStartup(javax.servlet.ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(MAdaptorApplicationConfiguration.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));
    }
}
