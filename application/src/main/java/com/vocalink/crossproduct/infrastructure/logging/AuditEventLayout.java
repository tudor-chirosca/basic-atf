package com.vocalink.crossproduct.infrastructure.logging;

import static java.util.Optional.ofNullable;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class AuditEventLayout extends LayoutBase<ILoggingEvent> {

    private static final String LIST_DELIMITER = ",";

    private String delimiter;

    private String timestampPattern;

    private String auditEventMarker;

    private String emptyValue;

    private String fields;

    @Override
    public String doLayout(final ILoggingEvent logEvent) {
        if (logEvent.getMarker() == null
                || !Objects.equals(logEvent.getMarker().getName(), auditEventMarker)) {
            throw new InvalidParameterException("Audit event layout cannot be used for events not marked as: "
                    + auditEventMarker);
        }
        return Arrays.stream(ofNullable(fields).orElse(StringUtils.EMPTY).split(LIST_DELIMITER))
                .filter(e -> EnumUtils.isValidEnum(AuditEventField.class, e))
                .map(e -> AuditEventField.valueOf(e).getFunction().apply(logEvent, this))
                .collect(Collectors.joining(delimiter)) + System.lineSeparator();
    }

}
