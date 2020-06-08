package com.hsx.bo.configuration;

import com.hsx.bo.properties.EncryptedPropertiesFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@PropertySource({"classpath:application.properties", "classpath:application-${spring.profiles.active}.properties"})
@ImportResource("classpath:applicationContext.xml")
@EnableSwagger2
public class ApplicationConfiguration implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfiguration.class);

    private ApplicationContext applicationContext;

    @Value("classpath:security.properties")
    private Resource securityPropsResource;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public EncryptedPropertiesFactoryBean securityProperties(){
        EncryptedPropertiesFactoryBean encryptedPropertiesFactoryBean = new EncryptedPropertiesFactoryBean();
        encryptedPropertiesFactoryBean.setLocation(securityPropsResource);
        return encryptedPropertiesFactoryBean;
    }


}