package com.sam.vt.properties.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@EqualsAndHashCode(callSuper = true)
@Configuration("EnvSnow")
@ConfigurationProperties(prefix = "app.info.snow")
public class EnvSnowProperty extends EnvInfo {
}
