package com.hsx.solace.autoconfigure.props;

import com.hsx.solace.YamlPropertySourceFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Component
@PropertySources({
        @PropertySource(value = "classpath:solace-config.yml", factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:solace-config.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:solace-config-${spring.profiles.active}.yml", factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:solace-config-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
})
@ConfigurationProperties("solace")
@Getter
@Setter
public class SolaceDefaultProperties {

    @Max(10)
    @Min(1)
    @NotNull
    private int defaultSiteNo;

    private List<SolaceJavaProperties> nodes;

}
