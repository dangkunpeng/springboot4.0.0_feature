package com.sam.vt.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.regex.Pattern;


public class ExceptionUtils {
    // RFC 5322-邮件正则
    private static final Pattern mailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    public static boolean isEmail(String txt) {
        return mailPattern.matcher(txt).matches();
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * @param ex
     * @return String
     * @history:
     */
    public static String getExceptionStackTrace(Exception ex) {
        StackTraceElement[] steArr = ex.getStackTrace();
        StringBuffer strb = new StringBuffer(ex.toString());
        for (StackTraceElement ste : steArr) {
            strb.append(System.getProperty("line.separator"));
            strb.append(ste.toString());
        }
        return strb.toString();
    }
}
