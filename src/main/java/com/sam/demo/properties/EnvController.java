package com.sam.demo.properties;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/env")
@RequiredArgsConstructor
public class EnvController {
    private final EnvRECONProperty envRECONProperty;
    private final EnvSnowProperty envSnowProperty;
    private final EnvDfmProperty envDfmProperty;
    private final EnvProperty envProperty;


    @RequestMapping("/recon")
    public EnvInfo getReconEnv() {
        return envRECONProperty;
    }

    @RequestMapping("/snow")
    public EnvInfo getSnowEnv() {
        return envSnowProperty;
    }

    @RequestMapping("/dfm")
    public EnvInfo getDfmEnv() {
        return envDfmProperty;
    }

    @RequestMapping("/all")
    public List<EnvInfo> getAllEnv() {
        return Lists.newArrayList(envProperty.getSnow(), envProperty.getDfm(), envProperty.getRecon());
    }
}
