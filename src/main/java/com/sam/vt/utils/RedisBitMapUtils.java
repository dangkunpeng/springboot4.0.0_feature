package com.sam.vt.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RedisBitMapUtils {

    private final StringRedisTemplate stringRedisTemplate;

    public Boolean setSigned(String userId, LocalDate date) {
        if (getSigned(userId, date)) {
            return false;
        }
        return stringRedisTemplate.opsForValue()
                .setBit(getSignKey(userId, date), getOffset(date), true);
    }

    private Boolean getSigned(String userId, LocalDate date) {
        return stringRedisTemplate.opsForValue()
                .getBit(getSignKey(userId, date), getOffset(date));
    }

    private static int getOffset(LocalDate date) {
        return date.getDayOfYear() - 1;
    }

    public Integer continusSignInfo(String userId, LocalDate date) {
        Integer count = 0;
        while (getSigned(userId, date)) {
            count++;
            date = date.minusDays(1);
        }
        return count;
    }

    private static String getSignKey(String userId, LocalDate date) {
        return date.getYear() + ":sign:" + userId;
    }
}
