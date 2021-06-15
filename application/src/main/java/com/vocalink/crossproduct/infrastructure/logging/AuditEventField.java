package com.vocalink.crossproduct.infrastructure.logging;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.BiFunction;

import static java.util.Optional.ofNullable;

import static org.apache.commons.lang3.StringUtils.defaultString;

import com.vocalink.crossproduct.domain.audit.UserDetails;
import com.vocalink.crossproduct.infrastructure.jpa.audit.AuditDetailsAdapter;
import com.vocalink.crossproduct.ui.aspects.OccurringEvent;

import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuditEventField {

    USER_ROLE_LIST((event, layout) -> layout.getEmptyValue()),
    IP_ADDRESS((event, layout) -> layout.getEmptyValue()),
    CUSTOMER((event, layout) -> AuditDetailsAdapter.CUSTOMER),
    PARTICIPANT_ID((event, layout) -> {
        OccurringEvent occurringEvent = getOccurringEvent(event);
        return getEmptyIfNull(occurringEvent.getParticipantId(), layout);
    }),
    CONTENTS((event, layout) -> {
        OccurringEvent occurringEvent = getOccurringEvent(event);
        return getEmptyIfNull(occurringEvent.getContent(), layout);
    }),
    REQUEST_URL((event, layout) -> {
        OccurringEvent occurringEvent = getOccurringEvent(event);
        return getEmptyIfNull(occurringEvent.getRequestUrl(), layout);
    }),
    APPROVAL_REQUEST_ID((event, layout) -> {
        OccurringEvent occurringEvent = getOccurringEvent(event);
        return getEmptyIfNull(occurringEvent.getApprovalRequestId(), layout);
    }),
    CORRELATION_ID((event, layout) -> {
        OccurringEvent occurringEvent = getOccurringEvent(event);
        return getEmptyIfNull(occurringEvent.getCorrelationId(), layout);
    }),
    CHANNEL((event, layout) -> {
        OccurringEvent occurringEvent = getOccurringEvent(event);
        return getEmptyIfNull(occurringEvent.getClient(), layout);
    }),
    APPLICATION_NAME((event, layout) -> {
        OccurringEvent occurringEvent = getOccurringEvent(event);
        return getEmptyIfNull(occurringEvent.getProduct(), layout);
    }),
    USERNAME((event, layout) -> {
        UserDetails userDetails = getUserDetails(event);
        return getEmptyIfNull(userDetails.getUserId(), layout);
    }),
    FIRST_NAME((event, layout) -> {
        UserDetails userDetails = getUserDetails(event);
        return getEmptyIfNull(userDetails.getFirstName(), layout);
    }),
    LAST_NAME((event, layout) -> {
        UserDetails userDetails = getUserDetails(event);
        return getEmptyIfNull(userDetails.getLastName(), layout);
    }),
    TIMESTAMP((event, layout) -> {
        Instant instant = Instant.ofEpochMilli(event.getTimeStamp());
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
        return dateTime.format(DateTimeFormatter.ofPattern(layout.getTimestampPattern()));
    }),
    EVENT_TYPE((event, layout) -> {
        OccurringEvent occurringEvent = getOccurringEvent(event);
        return ofNullable(occurringEvent.getEventType()).map(Enum::name).orElse(layout.getEmptyValue());
    }),
    OPERATION_TYPE((event, layout) -> {
        OccurringEvent occurringEvent = getOccurringEvent(event);
        return ofNullable(occurringEvent.getOperationType()).map(Enum::name).orElse(layout.getEmptyValue());
    });

    private final BiFunction<ILoggingEvent, AuditEventLayout, String> function;

    protected static OccurringEvent getOccurringEvent(final ILoggingEvent logEvent) {
        return (OccurringEvent) Arrays.stream(ofNullable(logEvent.getArgumentArray()).orElse(new Object[0]))
                .findFirst()
                .filter(OccurringEvent.class::isInstance)
                .orElseThrow(() -> new InvalidParameterException("Invalid first log event argument"));
    }

    protected static UserDetails getUserDetails(final ILoggingEvent logEvent) {
        return (UserDetails) Arrays.stream(ofNullable(logEvent.getArgumentArray()).orElse(new Object[0]))
                .skip(1)
                .findFirst()
                .filter(UserDetails.class::isInstance)
                .orElseThrow(() -> new InvalidParameterException("Invalid second log event argument"));
    }

    protected static String getEmptyIfNull(String value, AuditEventLayout layout) {
        return defaultString(value, layout.getEmptyValue());
    }
}
