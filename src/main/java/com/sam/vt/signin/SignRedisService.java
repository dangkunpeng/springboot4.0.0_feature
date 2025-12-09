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
        if (getSigned(userId, date)) {
            return Result.error("Already signed in " + day);
        }
        Thread.ofVirtual().start(() -> {
            setSigned(userId, date);
            log.info("on {}, total sign-ins: {}", day, summary(userId, date));
        });
        return Result.success("Sign-in successful");
    }

    private void setSigned(String userId, LocalDate date) {
        stringRedisTemplate.opsForValue()
                .setBit(getSignKey(userId, date), getOffset(date), true);
    }

    private Boolean getSigned(String userId, LocalDate date) {
        return stringRedisTemplate.opsForValue()
                .getBit(getSignKey(userId, date), getOffset(date));
    }

    private static int getOffset(LocalDate date) {
        return date.getDayOfYear() - 1;
    }

    private Integer summary(String userId, LocalDate date) {
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
