package com.sam.vt.mdc;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MdcFilterConfig {
    @Bean
    public FilterRegistrationBean<MdcLoggingFilter> loggingFilter() {
        FilterRegistrationBean<MdcLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MdcLoggingFilter());
        registrationBean.addUrlPatterns("/*"); // 设置过滤器应用的URL模式
        return registrationBean;
    }
}
