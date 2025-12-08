package com.sam.vt.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String TRACE_ID = "traceId";
    public static final String CHAR_SEPARATOR = ";";
    public static final String MQ_MAIN = "mqMain";
    public static final String MQ_MAIL = "mqMail";
    public static final String SYS_DEFAULT_DATETIME_PATTERN = "yyyyMMddHHmmssSSS";
    public static final String SYS_DEFAULT_DAY_PATTERN = "yyyyMMdd";
    public static final String CACHE_NAME = "APP";
    public static final String CACHE_TEMP_NAME = "APP_TEMP";
    // 补位字符
    public static final String PAD_CHAR = "0";


    // 计数器补位长度
    public static final Integer COUNT_LENGTH = 6;

    public static String now() {
        return minusDays(0, SYS_DEFAULT_DATETIME_PATTERN);
    }
    public static String nowDay() {
        return minusDays(0);
    }

    public static String minusDays(long days) {
        return minusDays(days, SYS_DEFAULT_DAY_PATTERN);
    }
    public static String minusDays(long days, String datePattern) {
        return LocalDateTime.now().minusDays(days).format(DateTimeFormatter.ofPattern(datePattern));
    }
}
