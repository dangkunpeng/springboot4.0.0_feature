package com.sam.vt.threadinqueen.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sam.vt.utils.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class KeyConcurrentApi {
    public static final int THREAD_COUNT = 100;

    public void test(int requestsPerThread) throws InterruptedException {
        runThread(requestsPerThread, Executors.newFixedThreadPool(THREAD_COUNT,
                new ThreadFactoryBuilder().setNameFormat("phyThread-%d").build()));
    }


    public void testVirtual(int requestsPerThread) throws InterruptedException {
        ThreadFactory threadFactory = Thread.ofVirtual()
                .name("virThread-", 0)
                .uncaughtExceptionHandler((t, e) -> {
                    // 异常时记录线程名称
                    log.error("Virtual Thread Exception [name: {}]", t.getName(), e);
                }).factory();
        // 准备线程任务
        runThread(requestsPerThread, Executors.newThreadPerTaskExecutor(threadFactory));
    }

    private static void runThread(int requestsPerThread, ExecutorService executor) {

        long startTime = System.currentTimeMillis(); // 开始时间
        // 准备线程任务
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> gen(requestsPerThread));
        }
        // 关闭线程池，不再接受新的任务
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // 等待直到所有任务完成
        } catch (InterruptedException e) {
            executor.shutdownNow(); // (new behavior)
            Thread.currentThread().interrupt();
        }
        long endTime = System.currentTimeMillis(); // 结束时间
        long duration = (endTime - startTime); // 计算耗时
        log.info("总花费时间：{} ms", duration);
    }

    private static void gen(int requestsPerThread) {
        log.info("gen运行{}", Thread.currentThread().getName());
        try {
            String key = RedisHelper.newKey("key");
//            log.info("{}加入{},等待计算", key, Thread.currentThread().getName());
            for (int j = 0; j < requestsPerThread; j++) {
                try {
                    // 生成主键
                    String id = RedisHelper.newKey(key + ":");
                } catch (Exception e) {
                    log.error("生成失败: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            log.info("有{}线程已完成", Thread.currentThread().getName());
        }
    }
}
