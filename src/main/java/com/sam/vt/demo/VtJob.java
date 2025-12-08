package com.sam.vt.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class VtJob {

    private final VirtualThreds virtualThreds;

    @Scheduled(fixedRate = 500L)
    public void scheduleTask() {
        Thread.startVirtualThread(() -> {
            log.info("Hello from virtual thread by startVirtualThread!");
        });
    }

    @Scheduled(fixedRate = 300L)
    public void ByService() {
        virtualThreds.hello();
        virtualThreds.helloWorld();
    }
}
