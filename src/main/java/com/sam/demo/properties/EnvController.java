package com.sam.demo.properties;

import com.google.common.collect.Lists;
import com.sam.demo.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EnvController {
    private final EnvProperty envProperty;
    @Autowired
    private Map<String, EnvInfo> envPropertyMap;

    @RequestMapping("/env/{envName}")
    public ResponseEntity<String> getReconEnv(@PathVariable String envName) {
        EnvInfo envInfo = envPropertyMap.get(envName);
        if (envInfo == null) {
            return ResponseEntity.badRequest().body("Invalid environment name: " + envName);
        }
        return ResponseEntity.ok(envInfo.getServer() + envInfo.getUrl());
    }


    @RequestMapping("/env/all")
    public ResponseEntity<String> getAllEnv() {
        return ResponseEntity.ok(JsonUtil.toJsonString(Lists.newArrayList(
                envProperty.getSnow(),
                envProperty.getDfm(),
                envProperty.getRecon())));
    }
}
