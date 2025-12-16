package com.sam.vt.threadinqueen.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sam.vt.utils.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class KeyConcurrentApi {

    public void test(int requestsPerThread) throws InterruptedException {

        // 测试参数配置
        int threadCount = 100;           // 并发线程数
        // 结果收集
        ConcurrentHashMap<String, AtomicInteger> idCounter = new ConcurrentHashMap<>();

        // 准备线程任务
        AtomicLong successCount = new AtomicLong(0);
        AtomicInteger duplicateCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount,
                new ThreadFactoryBuilder().setNameFormat("phyThread-%d").build());
        StopWatch stopwatch = new StopWatch("thread");
        stopwatch.start("executor");
        // 准备线程任务
        for (int i = 0; i < threadCount; i++) {
            final int finalcount = i;
            executor.submit(() -> {
                gen(requestsPerThread, idCounter, duplicateCount, successCount);
            });
        }
        stopwatch.stop();
        log.info("执行{}次，重复{}次", successCount, duplicateCount);
        log.info(stopwatch.prettyPrint());
        // 开始测试
        executor.shutdown();
    }

    public void testVirtual(int requestsPerThread) throws InterruptedException {

        // 测试参数配置
        int threadCount = 100;           // 并发线程数
        // 结果收集
        ConcurrentHashMap<String, AtomicInteger> idCounter = new ConcurrentHashMap<>();

        // 准备线程任务
        AtomicLong successCount = new AtomicLong(0);
        AtomicInteger duplicateCount = new AtomicInteger(0);
        StopWatch stopwatch = new StopWatch("thread");
        stopwatch.start("executor");
        // 准备线程任务
        for (int i = 0; i < threadCount; i++) {
            final int finalcount = i;
            Thread.ofVirtual().name("virThread-" + i).start(() -> {
                gen(requestsPerThread, idCounter, duplicateCount, successCount);
            });
        }

        stopwatch.stop();
        log.info("执行{}次，重复{}次", successCount, duplicateCount);
        log.info(stopwatch.prettyPrint());
    }

    private static void gen(int requestsPerThread, ConcurrentHashMap<String, AtomicInteger> idCounter, AtomicInteger duplicateCount, AtomicLong successCount) {
        try {
            String key = RedisHelper.newKey("key");
            log.info("{}加入,等待计算", key);
            for (int j = 0; j < requestsPerThread; j++) {
                try {
                    // 生成主键
                    String id = RedisHelper.newKey(key + ":");
                    // 统计唯一性
                    AtomicInteger count = idCounter.computeIfAbsent(id, k -> new AtomicInteger(0));
                    if (count.incrementAndGet() > 1) {
                        duplicateCount.incrementAndGet();
                        log.error("重复ID:{}, 出现次数: {}", id, count.get());
                    }
                    idCounter.put(id, count);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    log.error("生成失败: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("有{}线程已完成", Thread.currentThread().getName());
        }
    }
}
