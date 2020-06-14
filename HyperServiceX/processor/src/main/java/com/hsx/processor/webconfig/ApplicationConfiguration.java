package com.hsx.processor.webconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ImportResource({"classpath*:applicationContext.xml"})
//@PropertySource({"classpath:application.properties", "classpath:application-${spring.profiles.active}.properties"})
@EnableAspectJAutoProxy
public class ApplicationConfiguration {
}
