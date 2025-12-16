package com.sam.vt.threadinqueen;

import com.sam.vt.threadinqueen.service.KeyConcurrentApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/key")
public class KeyConcurrentController {
    private final KeyConcurrentApi keyConcurrentApi;

    @RequestMapping("/gen/{requestsPerThread}")
    public ResponseEntity<String> gen(@PathVariable int requestsPerThread) throws Exception {
        log.info("{}运行", Thread.currentThread().getName());
        keyConcurrentApi.test(requestsPerThread);
        keyConcurrentApi.testVirtual(requestsPerThread);
        log.info("{}结束", Thread.currentThread().getName());
        return ResponseEntity.ok("success");
    }
}
