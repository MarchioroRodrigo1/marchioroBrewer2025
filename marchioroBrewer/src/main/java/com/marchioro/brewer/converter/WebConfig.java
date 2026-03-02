package com.marchioro.brewer.converter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StringToBigDecimalConverter bigDecimalConverter;
    private final PermissaoConverter permissaoConverter; 

    public WebConfig(StringToBigDecimalConverter bigDecimalConverter,
                     PermissaoConverter permissaoConverter) {
        this.bigDecimalConverter = bigDecimalConverter;
        this.permissaoConverter = permissaoConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(bigDecimalConverter);
        registry.addConverter(permissaoConverter);
    }
}
