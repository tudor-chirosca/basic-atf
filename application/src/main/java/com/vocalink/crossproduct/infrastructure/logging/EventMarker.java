package com.vocalink.crossproduct.infrastructure.logging;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventMarker {

    public static final Marker AUDIT_EVENT_MARKER = MarkerFactory.getMarker("AUDIT-EVENT");
}
