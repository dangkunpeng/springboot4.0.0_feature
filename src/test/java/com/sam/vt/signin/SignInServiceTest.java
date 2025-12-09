package com.sam.vt.signin;

import com.sam.vt.signin.service.SignInService;
import com.sam.vt.utils.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
class SignInServiceTest {

    @Autowired
    private SignInService signInService;

    @Test
    void testSign() {
        String userId = RedisHelper.newKey("userId");
        signInService.sign(userId, LocalDate.now());
    }
}