package com.sam.demo.properties;

import com.google.common.collect.Lists;
import com.sam.demo.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> getReconEnv() {
        return ResponseEntity.ok(envRECONProperty.toString());
    }

    @RequestMapping("/snow")
    public ResponseEntity<String> getSnowEnv() {
        return ResponseEntity.ok(JsonUtil.toJsonString(envSnowProperty));
    }

    @RequestMapping("/dfm")
    public ResponseEntity<String> getDfmEnv() {
        return ResponseEntity.ok(envDfmProperty.getServer() + " - " + envDfmProperty.getUrl());
    }

    @RequestMapping("/all")
    public ResponseEntity<String> getAllEnv() {
        return ResponseEntity.ok(JsonUtil.toJsonString(Lists.newArrayList(
                envProperty.getSnow(),
                envProperty.getDfm(),
                envProperty.getRecon())));
    }
}
