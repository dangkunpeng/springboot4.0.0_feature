package com.sam.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.info.snow")
public class EnvSnowProperty extends EnvInfo {
}
