package com.sam.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
@ConfigurationProperties(prefix = "app.info")
public class EnvProperty {

    private EnvDetail recon;
    private EnvDetail snow;
    private EnvDetail dfm;
}
