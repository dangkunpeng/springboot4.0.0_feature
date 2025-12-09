package com.sam.vt.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class VtJob {

    private final VirtualThreds virtualThreds;

    //    @Scheduled(fixedRate = 300L)
    public void scheduleTask() {
        Thread.startVirtualThread(() -> {
            log.info("Hello from virtual thread by startVirtualThread!");
        });
        Thread.ofVirtual().name("hello-virtual-thread", 0).start(() -> {
            log.info("Hello from virtual thread by Thread.ofVirtual()!");
        });
    }

    //    @Scheduled(fixedRate = 400L)
    public void hello() {
        virtualThreds.hello();
    }

    //    @Scheduled(fixedRate = 500L)
    public void ByService() {
        virtualThreds.getDemoKey("helloWorld");
    }


}
