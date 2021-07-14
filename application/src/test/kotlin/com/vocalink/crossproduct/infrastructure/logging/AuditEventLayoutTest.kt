package com.vocalink.crossproduct.infrastructure.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import com.vocalink.crossproduct.TestConstants.FIXED_CLOCK
import com.vocalink.crossproduct.domain.audit.UserDetails
import com.vocalink.crossproduct.infrastructure.jpa.audit.AuditDetailsAdapter
import com.vocalink.crossproduct.ui.aspects.EventType
import com.vocalink.crossproduct.ui.aspects.OccurringEvent
import com.vocalink.crossproduct.ui.aspects.OperationType
import java.security.InvalidParameterException
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Arrays
import java.util.stream.Collectors
import org.apache.commons.lang.SystemUtils.LINE_SEPARATOR
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.slf4j.MarkerFactory

class AuditEventLayoutTest {

    companion object {
        val layout = AuditEventLayout()
        val delimiter = ","
        val auditMarker = "AUDIT-MARKER"
        val emptyValue = "EMPTY VALUE"
        val timestampPattern = "dd-MM-yyyy HH:mm:ss.SSS"
        var clock = FIXED_CLOCK
        var allFields = Arrays.stream(AuditEventField.values()).map { e -> e.name }
            .collect(Collectors.toList()).joinToString(separator = ",")
    }

    @BeforeEach
    internal fun setUp() {
        layout.delimiter = delimiter
        layout.auditEventMarker = auditMarker
        layout.timestampPattern = timestampPattern
        layout.emptyValue = emptyValue
    }

    @Test
    fun `should do layout all fields in expected order`() {
        layout.fields = allFields
        val event = mock(ILoggingEvent::class.java)
        val occurringEvent = getOccurringEvent()
        val userDetails = getUserDetails()
        `when`(event.argumentArray).thenReturn(arrayOf(occurringEvent, userDetails))
        `when`(event.marker).thenReturn(MarkerFactory.getMarker(auditMarker))
        `when`(event.timeStamp).thenReturn(clock.millis())
        val expectedTimestamp = ZonedDateTime.ofInstant(Instant.ofEpochMilli(clock.millis()), ZoneId.of("UTC"))
            .format(DateTimeFormatter.ofPattern(timestampPattern))

        val result = layout.doLayout(event)

        assertThat(result.replace(LINE_SEPARATOR, "").split(delimiter)).containsExactly(
            emptyValue,
            emptyValue,
            emptyValue,
            occurringEvent.participantId,
            occurringEvent.content,
            occurringEvent.requestUrl,
            occurringEvent.approvalRequestId,
            occurringEvent.correlationId,
            occurringEvent.client,
            occurringEvent.product,
            userDetails.userId,
            userDetails.firstName,
            userDetails.lastName,
            expectedTimestamp,
            occurringEvent.eventType.name,
            occurringEvent.operationType.name
        )
    }

    @Test
    fun `should throw exception when do layout if marker is invalid`() {
        val event = mock(ILoggingEvent::class.java)
        val occurringEvent = getOccurringEvent()
        val userDetails = getUserDetails()
        `when`(event.argumentArray).thenReturn(arrayOf(occurringEvent, userDetails))
        `when`(event.marker).thenReturn(MarkerFactory.getMarker("INVALID"))
        `when`(event.timeStamp).thenReturn(clock.millis())

        assertThrows(InvalidParameterException::class.java) { layout.doLayout(event) }
    }

    private fun getOccurringEvent(): OccurringEvent {
        return OccurringEvent.builder()
            .eventType(EventType.FILE_ENQUIRY)
            .content("OK")
            .operationType(OperationType.REQUEST)
            .product("P27")
            .client("BPS")
            .approvalRequestId("ApprovalRequestId")
            .requestUrl("RequestUrl")
            .correlationId("CorrelationId")
            .participantId("ParticipantId")
            .build()
    }

    private fun getUserDetails(): UserDetails {
        return UserDetails("UserId", "First Name", "Last Name", "ParticipantId")
    }

}