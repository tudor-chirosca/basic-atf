package com.vocalink.crossproduct.component.generator;

import static org.springframework.ui.freemarker.FreeMarkerTemplateUtils.processTemplateIntoString;

import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import javax.annotation.PostConstruct;

@Slf4j
@Lazy
@Component
@Profile("component")
public class TemplateParser {

    @Value("${freemarker.templates.folder}")
    private String templateFolder;

    private Configuration cfg;
    private final String TEMPLATE_EXT = ".ftl";

    @PostConstruct
    public void init() {
        try {
            cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
            cfg.setClassForTemplateLoading(this.getClass(), templateFolder);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public String parseTemplate(String templateName, Map<String, String> templateParameters) throws Exception {
        Template template = cfg.getTemplate(templateName + TEMPLATE_EXT);
        return processTemplateIntoString(template, templateParameters);
    }
}
