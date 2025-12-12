package com.sam.vt.mdc;

import com.sam.vt.utils.RedisHelper;
import com.sam.vt.utils.SysDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

@Slf4j
public class MdcUtils {

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
