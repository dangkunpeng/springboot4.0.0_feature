package com.sam.vt.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Component
@RequiredArgsConstructor
public class TemplateUtils {
    private final TemplateEngine templateEngine;

    public String processTemplate(String templateName, Context context) {
        return templateEngine.process(templateName, context);
    }
}
