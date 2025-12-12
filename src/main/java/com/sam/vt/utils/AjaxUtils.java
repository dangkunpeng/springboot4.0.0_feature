package com.sam.vt.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

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
}
