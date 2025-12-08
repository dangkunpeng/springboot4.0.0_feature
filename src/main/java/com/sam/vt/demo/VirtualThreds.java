package com.sam.vt.demo;

import lombok.extern.slf4j.Slf4j;
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
}
