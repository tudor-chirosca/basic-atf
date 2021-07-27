package com.vocalink.crossproduct.component.generator;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Profile("component")
public class TestMessageGenerator {

    private final TemplateParser templateParser;

    private final TestDataFactory testDataFactory;

    TestMessageGenerator(TemplateParser templateParser, TestDataFactory testDataFactory) {
        this.templateParser = templateParser;
        this.testDataFactory = testDataFactory;
    }

    public String createMessageBody(String template) throws Exception {
        return templateParser.parseTemplate(template, testDataFactory.fetchTestDataSet());
    }

    public String createMessageBody(String template, Map<String, String> overrideMap) throws Exception {
        return templateParser.parseTemplate(template, testDataFactory.fetchTestDataSet(overrideMap));
    }

    public String createFromData(String template, Map<String, String> testDataMap) throws Exception {
        return templateParser.parseTemplate(template, testDataMap);
    }
}
