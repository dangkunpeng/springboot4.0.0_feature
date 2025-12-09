package com.sam.vt.signin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
class SignRedisServiceTest {

    @Autowired
    private SignRedisService signRedisService;
    @Test
    void sign() {
        ResponseEntity<String> response = signRedisService.sign("user123", LocalDate.now());
        log.info("Response: {}", response.getBody());
    }
}