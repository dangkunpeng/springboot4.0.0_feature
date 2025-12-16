package com.sam.vt.hello;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/redis")
public class RedisKeyExpireController {

    private final StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/expiring/{matcher}/{seconds}")
    public ResponseEntity<String> expiringKey(@PathVariable String matcher, @PathVariable Long seconds) {
        log.info("Monitoring keys matching '*{}*' that are expiring within {} seconds", matcher, seconds);
        AtomicInteger count = new AtomicInteger(0);
        // 使用SCAN命令查找不包含"*matcher*"的键
        ScanOptions scanOptions = ScanOptions.scanOptions().match("*" + matcher + "*").count(100).build();
        // 遍历处理
        try (Cursor<String> cursor = stringRedisTemplate.scan(scanOptions)) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                count.getAndIncrement();
                log.info("expiring for key: {}", key);
                stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            }
        }
        return ResponseEntity.ok("Expiring " + count + " keys successfully");
    }
}
