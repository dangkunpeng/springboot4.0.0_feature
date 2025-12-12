package com.sam.vt.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Objects;

@Log4j2
public class AjaxUtils {


    /**
     * 验证是否是ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return Objects.equals("XMLHttpRequest", request.getHeader("X-Requested-With"));
    }

    public static boolean isAjaxUploadRequest(HttpServletRequest request) {
        return request.getParameter("ajaxUpload") != null;
    }


    public static String getTraceId() {
        String traceId = MDC.get(SysDefaults.TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            putTraceId();
        }
        return MDC.get(SysDefaults.TRACE_ID);
    }

    public static void putTraceId() {
        putTraceId(RedisHelper.newKey(SysDefaults.TRACE_ID));
    }

    public static void putTraceId(String traceId) {
        MDC.put(SysDefaults.TRACE_ID, traceId);
    }

    public static void removeTraceId() {
        MDC.remove(SysDefaults.TRACE_ID);
    }
}
