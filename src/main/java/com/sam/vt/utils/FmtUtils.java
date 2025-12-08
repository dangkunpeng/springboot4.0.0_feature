package com.sam.vt.utils;

import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;

@Component
public class FmtUtils {

    /**
     * "Rewrite {} from {} to {}";
     * 转换成
     * "Rewrite S4_SAP_POST_IN_PROGRESS from s4 Sap Post In Progress to S4_
     *
     * @param fmt
     * @param array
     * @return
     */
    public static String fmtMsg(String fmt, Object... array) {
        return MessageFormatter.arrayFormat(fmt, array).getMessage();
    }
}
