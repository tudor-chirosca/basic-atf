package com.vocalink.crossproduct.component.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Lazy
@Component
@Profile("component")
public class TestDataFactory {

    private final TestDataFileReader testDataFileReader;
    private final TestDataParser testDataParser;

    @Autowired
    TestDataFactory(TestDataFileReader testDataFileReader, TestDataParser testDataParser) {
        this.testDataFileReader = testDataFileReader;
        this.testDataParser = testDataParser;
    }

    private Map<String, String> rawTestDataSet;

    public Map<String, String> fetchTestDataSet() {
        setRawTestDataSet();
        return testDataParser.parseTestData(rawTestDataSet);
    }

    public Map<String, String> fetchTestDataSet(Map<String, String> passedParams) {
        setRawTestDataSet();
        Map<String, String> map = new HashMap<>(rawTestDataSet);
        return testDataParser.parseTestData(overrideTestDataSet(passedParams, map));
    }

    public Map<String, String> overrideTestDataSet(Map<String, String> fromSet, Map<String, String> destinationSet) {
        destinationSet.putAll(fromSet);
        return destinationSet;
    }

    private void setRawTestDataSet() {
        if (rawTestDataSet == null) {
            rawTestDataSet = Collections.unmodifiableMap(testDataFileReader.getTestData());
        }
    }
}
