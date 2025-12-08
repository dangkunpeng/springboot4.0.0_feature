package com.sam.vt.version;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VersionController {

    @RequestMapping(value = "/user", version = "1")
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok("hello world");
    }

    @RequestMapping(value = "/user", version = "2")
    public ResponseEntity<String> getVersion2() {
        return ResponseEntity.ok("hello v2");
    }
}
