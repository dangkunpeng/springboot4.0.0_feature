package com.sam.vt.threadinqueen;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CompletableFutureChainExample {
    
    public static void main(String[] args) throws Exception {
//        log.info("=== 示例1: 基础链式调用 ===");
//        example1_basicChain();
        
//        log.info("=== 示例2: 带异常处理的链式调用 ===");
//        example2_exceptionHandling();
//
//        log.info("=== 示例3: 组合多个CompletableFuture ===");
//        example3_combineFutures();
//
        log.info("=== 示例4: 实际业务场景 ===");
        example4_realWorldScenario();
    }
    
    /**
     * 示例1: 基础链式调用
     * 模拟: 获取用户信息 -> 查询订单 -> 计算价格
     */
    public static void example1_basicChain() throws Exception {
        CompletableFuture<Void> future = CompletableFuture
            .supplyAsync(() -> {
                log.info("步骤1: 开始获取用户信息...");
                sleep(1);
                return "用户ID: 1001";
            })
            .thenApply(userInfo -> {
                log.info("步骤2: 获取到 " + userInfo + ", 开始查询订单...");
                sleep(1);
                return userInfo + " -> 订单: ORD2024001";
            })
            .thenApply(orderInfo -> {
                log.info("步骤3: 获取到 " + orderInfo + ", 开始计算价格...");
                sleep(1);
                return orderInfo + " -> 总价: ￥999.00";
            })
            .thenAccept(result -> {
                log.info("步骤4: 最终结果: " + result);
            });
        
        // 等待所有步骤完成
        future.get();
        log.info("所有步骤执行完成！");
    }
    
    /**
     * 示例2: 带异常处理的链式调用
     */
    public static void example2_exceptionHandling() throws Exception {
        CompletableFuture.supplyAsync(() -> {
                log.info("1. 开始获取数据...");
                if (Math.random() > 0.5) {
                    throw new RuntimeException("sorry,something is wrong");
                }
                return "helloWorld";
            })
            .thenApply(data -> {
                log.info("2. 处理数据: " + data);
                return data.toUpperCase();
            })
            .exceptionally(ex -> {
                // 异常处理
                log.error("发生异常: " + ex.getMessage());
                return ex.getMessage();
            })
            .thenApply(result -> {
                log.info("3. 最终处理: " + result);
                return "处理完成: " + result;
            })
            .thenAccept(log::info)
            .get();
    }
    
    /**
     * 示例3: 组合多个CompletableFuture
     */
    public static void example3_combineFutures() throws Exception {
        // 并行执行两个独立任务
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            sleep(2);
            return "任务1结果";
        });
        
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            sleep(1);
            return "任务2结果";
        });
        
        // 组合两个任务的结果
        task1.thenCombine(task2, (result1, result2) -> {
                log.info("组合结果: " + result1 + " + " + result2);
                return result1 + " | " + result2;
            })
            .thenAccept(combined -> {
                log.info("最终组合: " + combined);
            })
            .get();
        
        // 等待任意一个任务完成
        CompletableFuture<Object> anyResult = CompletableFuture.anyOf(task1, task2);
        anyResult.thenAccept(result -> {
            log.info("最先完成的任务: " + result);
        });
        
        // 等待所有任务完成
        CompletableFuture<Void> allResult = CompletableFuture.allOf(task1, task2);
        allResult.thenRun(() -> {
            log.info("所有任务都已完成");
        }).get();
    }
    
    /**
     * 示例4: 实际业务场景 - 订单处理流程
     */
    public static void example4_realWorldScenario() throws Exception {
        // 模拟完整的订单处理流程
        CompletableFuture<String> orderProcessing = CompletableFuture
            .supplyAsync(() -> validateOrder("ORD001"))
            .thenApply(validatedOrder -> checkInventory(validatedOrder))
            .thenApply(checkedOrder -> calculatePrice(checkedOrder))
            .thenApply(pricedOrder -> applyDiscount(pricedOrder))
            .thenApply(discountedOrder -> processPayment(discountedOrder))
            .thenApply(paidOrder -> sendNotification(paidOrder))
            .exceptionally(ex -> {
                log.error("订单处理失败: " + ex.getMessage());
                handleOrderFailure(ex);
                return "订单处理失败";
            });
        
        String result = orderProcessing.get();
        log.info("订单处理最终结果: " + result);
    }
    
    // 模拟业务方法
    private static String validateOrder(String orderId) {
        log.info("[验证订单] 验证订单: " + orderId);
        sleep(0.5);
        if (orderId.contains("INVALID")) {
            throw new IllegalArgumentException("无效的订单号");
        }
        return orderId + " (已验证)";
    }
    
    private static String checkInventory(String order) {
        log.info("[检查库存] 检查订单库存: " + order);
        sleep(0.8);
        return order + " (库存充足)";
    }
    
    private static String calculatePrice(String order) {
        log.info("[计算价格] 计算订单价格: " + order);
        sleep(0.3);
        return order + " (价格: ￥299.00)";
    }
    
    private static String applyDiscount(String order) {
        log.info("[应用折扣] 应用会员折扣: " + order);
        sleep(0.2);
        return order + " (折后: ￥269.10)";
    }
    
    private static String processPayment(String order) {
        log.info("[处理支付] 处理订单支付: " + order);
        sleep(1);
        double random = Math.random();
        if (random < 0.2) {  // 20%概率支付失败
            throw new RuntimeException("支付失败: 余额不足");
        }
        return order + " (支付成功)";
    }
    
    private static String sendNotification(String order) {
        log.info("[发送通知] 发送订单通知: " + order);
        sleep(0.5);
        return order + " (通知已发送)";
    }
    
    private static void handleOrderFailure(Throwable ex) {
        log.info("[错误处理] 执行补偿操作: " + ex.getMessage());
    }
    
    private static void sleep(double seconds) {
        try {
            TimeUnit.MILLISECONDS.sleep((long)(seconds * 300));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}