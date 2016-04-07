package com.mpw.config;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * Created by wilk.wojtek@gmail.com.
 */


@Configuration
@PropertySources({
    @PropertySource("default.properties"),
    @PropertySource(value = "file:${user.home}/environment.properties", ignoreResourceNotFound = true)
})
public class EncodingConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        registrationBean.setFilter(characterEncodingFilter);
        return registrationBean;
    }
}
