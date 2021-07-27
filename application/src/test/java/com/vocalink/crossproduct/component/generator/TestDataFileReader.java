package com.vocalink.crossproduct.component.generator;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Lazy
@Component
@Profile("component")
public class TestDataFileReader {

    @Value("${test.data.folder}")
    private String testDataFolder;

    @Value("${test.data.file}")
    private String testDataFile;

    protected Map<String, String> getTestData() {
        HashMap<String, String> resultMap = new HashMap<>();
        try (InputStreamReader inputStream = getInputStream(testDataFolder + testDataFile)) {
            List<String[]> linesList = new CSVReader(inputStream).readAll();
            for (String[] row : linesList) {
                resultMap.put(trim(row[0]), trim(row[1]));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
        return resultMap;
    }

    private String trim(String s) {
        String[] whitespaceCharsArray = {"\u00A0", "\u0009", "\u000B", "\u000C", "\t"};
        for (String ws : whitespaceCharsArray) {
            s = s.replaceAll(ws, "");
            s = s.trim();
        }
        return s.replace("\"\"", "\"");
    }

    private InputStreamReader getInputStream(String resource) {
        return new InputStreamReader(TestDataFileReader.class.getResourceAsStream(resource));
    }
}
