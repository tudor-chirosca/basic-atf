package com.vocalink.crossproduct.component.generator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class will create a test data set based on input values
 * REQUIRES following entries to be added to config:
 * <p>
 * - 'testdata.separator' - used for special keys [copy, concat].
 * Test data file entry for ":::" separator Ex.: concat:::x1+x2+ ... +xn; copy:::x1;
 * <p>
 * - 'testdata.generator.class' - used to define methods for data generation mapped from test data file entries.
 * If no config entry is defined than 'DefaultDataGenerator.class' will be called.
 */
@Slf4j
@Lazy
@Component
@Profile("component")
class TestDataParser {

    @Value("${test.data.separator}")
    private String separator;

    private final DataGenerator generator;

    TestDataParser(DataGenerator generator) {
        this.generator = generator;
    }

    /**
     * Through a set of iteration will produce a new set of test data:
     * - Skip entries: Map<k,v> entries with v == 'SKIP' will be removed from final data set.
     * This is performed when certain elements/nodes should not be populated for Negative scenarios testing;
     * - Generate entries: Map<k,v> entries with v == generatorClass.methodName() will be set with method return value.
     * If generatorClass will not have a matching method, the initial value will be set as final;
     * - Copy entries: Map<k,v> entries with v == 'COPY + SEPARATOR + MAP<k,v> k of element to copy v from'
     * - Concat entries: Map<k,v> entries with v == 'CONCAT + SEPARATOR + MAP<k,v> k of elements to concat summed by '+''
     *
     * @param rawTestDataEntries Map<k,v> as extracted from test data file.
     * @return a new set of test data entries
     */
    Map<String, String> parseTestData(Map<String, String> rawTestDataEntries) {
        rawTestDataEntries = new HashMap<>(rawTestDataEntries);
        Map<String, String> testDataMap = removeSkippedEntries(rawTestDataEntries);
        try {
            testDataMap.forEach((k, v) -> {
                testDataMap.replace(k, (compute(v)));
            });

            testDataMap.forEach((k, v) -> {
                if (isPlaceholderEntry(v)) {
                    testDataMap.replace(k, getPlaceholderValue(v));
                }
            });

            testDataMap.forEach((k, v) -> {
                if (v.contains("copy")) {
                    testDataMap.replace(k, testDataMap.get(v.split(separator)[1]));
                }
            });

            testDataMap.forEach((k, v) -> {
                if (v.contains("concat")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String[] concatKeys = v.split(separator)[1].split("\\+");
                    for (String s : concatKeys) {
                        stringBuilder.append(testDataMap.getOrDefault(s, s));
                    }
                    testDataMap.replace(k, stringBuilder.toString());
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return testDataMap;
    }

    /**
     * Looks into generatorClass for existing method with given parameters arity
     *
     * @param tdEntry that match generatorClass method name and params arity. Ex.: generateRandomNumber(10)
     * @return output of invoked method or tdEntry value as string
     */
    private String compute(String tdEntry) {
        String methodName = getMethodName(tdEntry);
        if (methodName != null) {
            try {
                String[] args = getArgs(tdEntry);
                if (args.length == 0) {
                    Method method = generator.getClass().getDeclaredMethod(methodName);
                    return (String) method.invoke(generator);
                } else {
                    Class[] argTypes = new Class[args.length];
                    for (int i = 0; i < args.length; i++) {
                        argTypes[i] = String.class;
                    }
                    Method method = generator.getClass().getDeclaredMethod(methodName, argTypes);
                    return (String) method.invoke(generator, args);
                }
            } catch (NoSuchMethodException ex) {
                return tdEntry;
            } catch (Exception ex) {
                log.error(ex.getMessage() + " for method call: " + methodName);
            }
        }
        return tdEntry;
    }

    private String getMethodName(String tdEntry) {
        Matcher m = Pattern.compile("(.*?)\\(").matcher(tdEntry);
        return m.find() ? m.group(1) : null;
    }

    private String[] getArgs(String tdEntry) {
        String args = tdEntry.substring(tdEntry.indexOf("(") + 1, tdEntry.indexOf(")"));
        if (args.isEmpty()) {
            return new String[] {};
        }
        return args.contains(separator) ? args.split(separator) : new String[] {args};
    }

    private String getPlaceholderValue(String placeholderName) {
        for (TestDataPlaceholders t : TestDataPlaceholders.values()) {
            if (placeholderName.equalsIgnoreCase(t.getPlaceholderName())) {
                return t.getPlaceholderValue();
            }
        }
        return placeholderName;
    }

    private Map<String, String> removeSkippedEntries(Map<String, String> testDataMap) {
        ArrayList<String> sL = new ArrayList<>();
        testDataMap.forEach((k, v) -> {
            if (v.equalsIgnoreCase("skip")) {
                sL.add(k);
            }
        });
        for (String s : sL) {
            testDataMap.remove(s);
        }
        return testDataMap;
    }

    private boolean isPlaceholderEntry(String testDataVariable) {
        for (TestDataPlaceholders t : TestDataPlaceholders.values()) {
            if (testDataVariable.toLowerCase().contains(t.getPlaceholderName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    enum TestDataPlaceholders {
        EMPTY_SPACE("empty", ""), BLANK("blank", " ");

        TestDataPlaceholders(String name, String value) {
            placeholderName = name;
            placeholderValue = value;
        }

        String placeholderName;

        String placeholderValue;

        String getPlaceholderName() {
            return placeholderName;
        }

        String getPlaceholderValue() {
            return placeholderValue;
        }
    }
}
