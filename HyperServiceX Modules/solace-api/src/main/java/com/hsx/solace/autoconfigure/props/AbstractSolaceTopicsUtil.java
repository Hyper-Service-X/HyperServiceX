package com.hsx.solace.autoconfigure.props;

import com.hsx.solace.YamlPropertySourceFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@PropertySources({
        @PropertySource(value = "classpath:solace-config.yml", factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:solace-config.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:solace-config-${spring.profiles.active}.yml", factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:solace-config-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
})
@ConfigurationProperties("solace.topics")
@Getter
@Setter
public class AbstractSolaceTopicsUtil {

    Map<String, Map<String, String>> templates;

    public String getTemplate(String version, String messageType) {
        return templates.get(version).get(messageType);
    }
}




