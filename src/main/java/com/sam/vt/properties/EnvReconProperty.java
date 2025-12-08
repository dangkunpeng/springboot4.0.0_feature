package com.sam.vt.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@EqualsAndHashCode(callSuper = true)
@Configuration("EnvRecon")
@ConfigurationProperties(prefix = "app.info.recon")
public class EnvReconProperty extends EnvInfo {
}
