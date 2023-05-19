package com.example.bookstore.configuration;

import com.example.bookstore.rest.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class represents the configuration for the application.
 * @author chetanbhatt
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToEnumConverter());
    }

    static class StringToEnumConverter implements Converter<String, Category> {
        @Override
        public Category convert(String source) {
            return Category.valueOf(source.toUpperCase());
        }
    }

}
