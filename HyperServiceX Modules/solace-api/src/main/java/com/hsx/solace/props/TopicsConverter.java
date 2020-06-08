package com.hsx.solace.props;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@ConfigurationPropertiesBinding
public class TopicsConverter implements Converter<String, List> {
    @Override
    public List convert(String source) {
        return Arrays.asList(source.split(","));
    }
}
