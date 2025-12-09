package com.sam.vt.signin;

import com.sam.vt.utils.RedisHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sign")
public class SignRedisController {
    private final SignRedisService signRedisService;

    @RequestMapping("/test/{times}")
    public ResponseEntity<String> sign(@PathVariable Integer times) {
        String userId = RedisHelper.newKey("userId");
        log.info("Initializing sign-in data for userId: {}", userId);
        LocalDate today = LocalDate.of(2025, 1, 1);
        StopWatch stopWatch = new StopWatch("sign");
        stopWatch.start("vt sign");
        for (int i = 0; i < times; i++) {
//            int days = ThreadLocalRandom.current().nextInt(1, 3);
            today = today.plusDays(1);
            signRedisService.sign(userId, today);
        }
        stopWatch.stop();
        log.info("Sign-in test completed. Total time: {} ms", stopWatch.getTotalTimeMillis());
        return ResponseEntity.ok("sign  in" + stopWatch.getTotalTimeMillis() + "  ms");
    }
}
