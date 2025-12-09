package com.sam.vt.properties.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@EqualsAndHashCode(callSuper = true)
@Configuration("EnvDfm")
@ConfigurationProperties(prefix = "app.info.dfm")
public class EnvDfmProperty extends EnvInfo {
}
