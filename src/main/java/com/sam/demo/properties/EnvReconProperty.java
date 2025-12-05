package com.sam.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration("EnvRecon")
@ConfigurationProperties(prefix = "app.info.recon")
public class EnvReconProperty extends EnvInfo {
}
