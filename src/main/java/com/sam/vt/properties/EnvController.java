package com.sam.vt.properties;

import com.sam.vt.properties.bean.EnvInfo;
import com.sam.vt.properties.bean.EnvProperty;
import com.sam.vt.utils.FmtUtils;
import com.sam.vt.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/env")
@RequiredArgsConstructor
public class EnvController {

    private static final String ENV_DESC = """
            version={},
            dfm ={},
            snow ={},
            recon ={},
            """;
    private final EnvProperty envProperty;
    @Autowired
    private Map<String, EnvInfo> envPropertyMap;

    @RequestMapping("/byName/{envName}")
    public ResponseEntity<String> getReconEnv(@PathVariable String envName) {
        EnvInfo envInfo = envPropertyMap.get(envName);
        if (envInfo == null) {
            return ResponseEntity.badRequest().body("Invalid environment name: " + envName);
        }
        return ResponseEntity.ok(envInfo.getServer() + envInfo.getUrl());
    }


    @RequestMapping("/all")
    public ResponseEntity<String> getAllEnv() {
        String version = envProperty.getVersion();
        String snow = JsonUtil.toJsonString(envProperty.getSnow());
        String dfm = JsonUtil.toJsonString(envProperty.getDfm());
        String recon = JsonUtil.toJsonString(envProperty.getRecon());

        return ResponseEntity.ok(FmtUtils.fmtMsg(ENV_DESC, version, dfm, snow, recon));
    }
}
