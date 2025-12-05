package com.sam.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.info.dfm")
public class EnvDfmProperty extends EnvDetail {
}
