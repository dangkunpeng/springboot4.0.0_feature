package com.sam.vt.utils;

import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


public class ExceptionUtils {
    // RFC 5322-邮件正则
    private static final Pattern mailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    public static boolean isEmail(String txt) {
        return mailPattern.matcher(txt).matches();
    }

    private static final List<String> headers = Lists.newArrayList(
            "x-forwarded-for"
            , "Proxy-Client-IP"
            , "WL-Proxy-Client-IP"
            , "HTTP_CLIENT_IP"
            , "HTTP_X_FORWARDED_FOR"
    );

    /**
     * 获取客户端IP地址
     *
     * @param request HttpServletRequest
     * @return IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = "";
        for (String header : headers) {
            // 从header
            ip = request.getHeader(header);
            if (StringUtils.isNotBlank(ip) && !Objects.equals(ip, "unknown")) {
                break;
            }
        }
        if (StringUtils.isNotBlank(ip) && !Objects.equals(ip, "unknown")) {
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
