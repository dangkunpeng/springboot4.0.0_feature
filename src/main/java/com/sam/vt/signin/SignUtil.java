package com.sam.vt.signin;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class SignUtil {
    
    /**
     * 获取今天是今年的第几天
     */
    public static int getDayOfYear() {
        return LocalDate.now().getDayOfYear();
    }
    
    /**
     * 获取指定日期是当年的第几天
     */
    public static int getDayOfYear(LocalDate date) {
        return date.getDayOfYear();
    }
    
    /**
     * 获取当前年份
     */
    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }
    
    /**
     * 获取昨天的日期
     */
    public static LocalDate getYesterday() {
        return LocalDate.now().minusDays(1);
    }
    
    /**
     * 日期转字符串
     */
    public static String dateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_DATE);
    }
    
    /**
     * 字符串转日期
     */
    public static LocalDate stringToDate(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
    }
}