package com.sam.vt.template;

import com.sam.vt.utils.FmtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;
import org.thymeleaf.context.Context;

@Slf4j
@SpringBootTest
class TemplateUtilsTest {

    @Autowired
    private TemplateUtils templateUtils;

    String msg = """
            <!DOCTYPE html>
            <!-- 引入thymeleaf命名空间，方便使用thymeleaf属性 -->
            <html lang="en" xmlns:th="http://www.thymeleaf.org">
            <head>
                <meta charset="UTF-8">
                <title>thymeleaf入门</title>
            </head>
            <body>
            <!-- 静态页面显示程序员，动态页面使用后端传来的msg数据代替 -->
            <h1>{}</h1>
            </body>
            """;

    @Test
    void processTemplate() {
        String templateName = "mail/msg";
        var context = new Context();
        context.setVariable("msg", "hello, Thymeleaf!");
        StopWatch stopWatch = new StopWatch("Template Processing");
        stopWatch.start("Thymeleaf Template Processing");
        String txt = templateUtils.processTemplate(templateName, context);
        stopWatch.stop();

        stopWatch.start("txt Template Processing");
        String txt01 = FmtUtils.fmtMsg(msg, "hello, Thymeleaf!");
        stopWatch.stop();
        log.info("stopWatch {}", stopWatch.prettyPrint());
    }
}