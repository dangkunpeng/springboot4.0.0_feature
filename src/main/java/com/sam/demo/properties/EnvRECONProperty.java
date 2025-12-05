package com.sam.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.info.recon")
public class EnvRECONProperty extends EnvDetail {
}
