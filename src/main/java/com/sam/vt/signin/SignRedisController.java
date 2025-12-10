package com.sam.vt.signin;

import com.google.common.collect.Lists;
import com.sam.vt.db.entity.SignInfo;
import com.sam.vt.signin.service.SignInService;
import com.sam.vt.signin.service.SignRedisService;
import com.sam.vt.utils.JsonUtil;
import com.sam.vt.utils.RedisHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sign")
public class SignRedisController {

    private final SignInService signInService;
    private final SignRedisService signRedisService;

    @RequestMapping("/test/{times}")
    public ResponseEntity<List<String>> sign(@PathVariable Integer times) {
        String userId = RedisHelper.newKey("userId");
        log.info("Initializing sign-in data for userId: {}", userId);
        LocalDate today = LocalDate.of(2025, 1, 1);
        StopWatch stopWatch = new StopWatch("sign");
        stopWatch.start("vt sign");
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < times; i++) {
            int days = ThreadLocalRandom.current().nextInt(1, 3);
            today = today.plusDays(days);
            signRedisService.sign(userId, today);
            ResponseEntity<SignInfo> resp = signInService.sign(userId, today);
            list.add(JsonUtil.toJsonString(resp.getBody()));
        }
        stopWatch.stop();
        log.info("Sign-in test completed. Total time: {} ms", stopWatch.getTotalTimeMillis());

        return ResponseEntity.ok(list);
    }
}
