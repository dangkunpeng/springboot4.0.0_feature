package com.sam.vt.signin;

import com.sam.vt.signin.beans.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.sam.vt.utils.Constants.SYS_DEFAULT_DAY_PATTERN;

@Slf4j
@Service
public class SignRedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Result<String> sign(String userId, LocalDate date) {
        String day = date.format(DateTimeFormatter.ofPattern(SYS_DEFAULT_DAY_PATTERN));
        if (hasSigned(userId, date)) {
            return Result.error("Already signed in " + day);
        } else {
            String year = String.valueOf(date.getYear());
            String signKey = getSignKey(userId, year);
            int offset = date.getDayOfYear() - 1;
            stringRedisTemplate.opsForValue().setBit(signKey, offset, true);
        }
        log.info("on {}, total sign-ins: {}", day, summary(userId, date));
        return Result.success("Sign-in successful");
    }

    private Boolean hasSigned(String userId, LocalDate date) {
        String year = String.valueOf(date.getYear());
        String signKey = getSignKey(userId, year);
        int offset = date.getDayOfYear() - 1;
        return stringRedisTemplate.opsForValue().getBit(signKey, offset);
    }

    private Integer summary(String userId, LocalDate date) {
        Integer count = 0;
        while (hasSigned(userId, date)) {
            count++;
            date = date.minusDays(1);
        }
        return count;

    }

    private static String getSignKey(String userId, String year) {
        return "sign-" + userId + "-" + year;
    }
}
