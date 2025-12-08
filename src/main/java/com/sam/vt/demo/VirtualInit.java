package com.sam.vt.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class VirtualInit implements ApplicationRunner {
    private final VirtualThreds virtualThreds;
    @Override
    public void run(ApplicationArguments args) throws Exception {

//        virtualThreds.hello();
//        virtualThreds.helloWorld();
        log.info("VirtualInit run method executed.");
        Thread.startVirtualThread(() -> {
            log.info("Hello from virtual thread by startVirtualThread!");
        });
    }
}
