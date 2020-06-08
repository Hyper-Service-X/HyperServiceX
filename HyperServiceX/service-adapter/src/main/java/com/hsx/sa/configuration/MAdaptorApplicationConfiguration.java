package com.hsx.sa.configuration;

import com.hsx.sa.properties.EncryptedPropertiesFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.Resource;

@Configuration
@ImportResource("classpath:applicationContext.xml")
@EnableAspectJAutoProxy
public class MAdaptorApplicationConfiguration implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Value("classpath:security.properties")
    private Resource securityPropsResource;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public EncryptedPropertiesFactoryBean securityProperties() {
        EncryptedPropertiesFactoryBean encryptedPropertiesFactoryBean = new EncryptedPropertiesFactoryBean();
        encryptedPropertiesFactoryBean.setLocation(securityPropsResource);
        return encryptedPropertiesFactoryBean;
    }
}