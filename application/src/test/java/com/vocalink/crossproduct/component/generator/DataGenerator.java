package com.vocalink.crossproduct.component.generator;

import io.generators.core.RandomAlphabeticStringGenerator;
import io.generators.core.RandomAlphanumericGenerator;
import io.generators.core.RandomPositiveLongGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@Profile("component")
class DataGenerator {

    public String generateRandomNumber(String length) {
        long maxValue = setMaxValue(Integer.parseInt(length));
        long minValue = setMinValue(Integer.parseInt(length));
        return new RandomPositiveLongGenerator(minValue, maxValue).next().toString();
    }

    public String generateRandomString(String length) {
        return new RandomAlphabeticStringGenerator(Integer.parseInt(length)).next();
    }

    public String generateRandomAlphanumeric(String length) {
        return new RandomAlphanumericGenerator(Integer.parseInt(length)).next();
    }

    public String dateTimeNow() {
        return LocalDateTime.now().toString();
    }

    public String dateTimeNow(String pattern) {
        return dateTimeByPattern(pattern, null);
    }

    public String dateTimeNow(String pattern, String zone) {
        return dateTimeByPattern(pattern, zone);
    }

    public String dateTimeNowInMillis(String zone) {
        LocalDateTime localDtTm = LocalDateTime.now(ZoneId.of(zone));
        return String.valueOf(localDtTm.atZone(ZoneId.of(zone)).toInstant().toEpochMilli());
    }

    private long setMaxValue(int dataLength) {
        String res = String.valueOf(9);
        for (int i = 1; i < dataLength; i++) {
            res = res.concat(String.valueOf(9));
        }
        return Long.parseLong(res);
    }

    private long setMinValue(int dataLength) {
        String res = String.valueOf(1);
        for (int i = 1; i < dataLength; i++) {
            res = res.concat(String.valueOf(1));
        }
        return Long.parseLong(res);
    }

    /**
     *
     * @param pattern ex.: yyyyMMdd, yyyyMMddhhmmss
     * @param zone can be null; ex: UTC
     */
    private String dateTimeByPattern(String pattern, String zone) {
        LocalDateTime localDateTime;
        if (zone != null) {
            ZoneId zoneId = ZoneId.of(zone);
            localDateTime = LocalDateTime.now(zoneId);
        } else {
            localDateTime = LocalDateTime.now();
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
