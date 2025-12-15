package com.sam.vt.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 对象转 JSON 字符串
     */
    public static String toJsonString(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字符串转JSON对象
     */

    public static <T> T toObj(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("反序列化JSON为对象失败", e);
        }
    }

    /**
     * 字符串转 List<T>
     */
    public static <T> List<T> toObjList(String json, Class<T> clazz) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            throw new RuntimeException("反序列化JSON为List失败", e);
        }
    }
}
