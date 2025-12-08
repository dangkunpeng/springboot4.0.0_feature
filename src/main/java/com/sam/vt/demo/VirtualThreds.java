package com.sam.vt.demo;

import com.sam.vt.utils.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VirtualThreds {

    @Async
    public void hello() {
        log.info("Hello from virtual thread by Service!");
    }

    public void helloWorld() {
        log.info("Hello world from virtual thread by Service!");
    }

    @Cacheable(value = "helloWorld", key = "#root.methodName + '-' + #key", unless = "#result==null")
    public String getDemoKey(String key) {
        log.info("No demo key and  gen new key={}", key);
        return RedisHelper.newKey(key);
    }
}
