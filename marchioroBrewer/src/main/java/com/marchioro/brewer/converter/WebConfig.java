package com.marchioro.brewer.converter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StringToBigDecimalConverter bigDecimalConverter;

    public WebConfig(StringToBigDecimalConverter bigDecimalConverter) {
        this.bigDecimalConverter = bigDecimalConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(bigDecimalConverter);
    }
}
