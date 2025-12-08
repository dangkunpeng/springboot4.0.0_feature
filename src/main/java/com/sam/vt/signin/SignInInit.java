package com.sam.vt.signin;

import com.sam.vt.utils.RedisHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import static com.sam.vt.utils.Constants.SYS_DEFAULT_DAY_PATTERN;

@Slf4j
@RequiredArgsConstructor
@Component
public class SignInInit implements ApplicationRunner {
    private final SignRedisService signRedisService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String userId = RedisHelper.newKey("userId");
        log.info("Initializing sign-in data for userId: {}", userId);
        LocalDate today = LocalDate.now().minusDays(100);
        for (int i = 0; i < 50; i++) {
            int days = ThreadLocalRandom.current().nextInt(1, 3);
            today = today.plusDays(days);
            log.info("date = {}", today.format(DateTimeFormatter.ofPattern(SYS_DEFAULT_DAY_PATTERN)));
            signRedisService.sign(userId, today);
        }

    }
}
