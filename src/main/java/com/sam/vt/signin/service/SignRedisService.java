package com.sam.vt.signin.service;

import com.sam.vt.utils.RedisBitMapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.sam.vt.utils.SysDefaults.SYS_DEFAULT_DAY_PATTERN;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignRedisService {

    private static final String SUMMARY_INFO = "on {}, continuous sign-ins: {}";
    private final RedisBitMapUtils redisBitMapUtils;

    public ResponseEntity<String> sign(String userId, LocalDate date) {
        Thread.ofVirtual().start(() -> {
            redisBitMapUtils.setSigned(userId, date);
            String day = date.format(DateTimeFormatter.ofPattern(SYS_DEFAULT_DAY_PATTERN));
            log.info(SUMMARY_INFO, day, redisBitMapUtils.continusSignInfo(userId, date));
        });
        return ResponseEntity.ok("Sign-in successful");
    }

}
