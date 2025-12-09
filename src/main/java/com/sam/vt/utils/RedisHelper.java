package com.sam.vt.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.sam.vt.utils.Constants.COUNT_LENGTH;
import static com.sam.vt.utils.Constants.PAD_CHAR;


@Service
public class RedisHelper {

    private static final long KEY_EXPIRE_MINUTES = 60 * 12;
    private static StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        RedisHelper.stringRedisTemplate = stringRedisTemplate;
    }

    // 设置缓存（带过期时间）
    public static <T> void set(String key, T value) {
        set(key, value, KEY_EXPIRE_MINUTES);
    }

    // 设置缓存（带过期时间）
    public static <T> void set(String key, T value, long minutes) {
        stringRedisTemplate.opsForValue().set(key, JsonUtil.toJsonString(value), minutes + ThreadLocalRandom.current().nextInt(1, 10), TimeUnit.MINUTES);
    }

    // 获取缓存（解决缓存穿透：缓存空值）
    public static <T> T get(String key, Class<T> type) {
        String json = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(json)) {
            // 判断是否为缓存的空值（防止缓存穿透）
            // 缓存不存在，返回null，由调用方决定是否从数据源加载
            return null;
        }
        return JsonUtil.toObj(json, type);
    }

    // 删除缓存
    public static Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    // 判断缓存是否存在
    public static Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public static Boolean expire(String key) {
        return expire(key, KEY_EXPIRE_MINUTES);
    }

    public static Boolean expire(String key, long minutes) {
        return stringRedisTemplate.expire(key, minutes * 60 + ThreadLocalRandom.current().nextInt(1, 10), TimeUnit.SECONDS);
    }

    /**
     * leftPush 和 rightPop 可以实现一个简单的队列
     *
     * @param key
     * @param value
     */
    public static void leftPush(String key, String value) {
        stringRedisTemplate.opsForList().leftPush(key, value);
        expire(key);
    }

    public static Object rightPop(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    public static Long listSize(String key) {
        return stringRedisTemplate.opsForList().size(key);
    }

    public static String newKey(String id) {
        // 这里可以调用 KeyTool 来生成主键
        StringBuilder result = new StringBuilder();
        // 时间戳
        result.append(id).append(Constants.nowDay());
        // 获取时间戳的使用次数
        Long counter = stringRedisTemplate.opsForValue().increment(result.toString());
        expire(result.toString(), KEY_EXPIRE_MINUTES * 2);
        // 拼接上序号
        result.append(StringUtils.leftPad(String.valueOf(counter), COUNT_LENGTH, PAD_CHAR));
        return result.toString();
    }
}