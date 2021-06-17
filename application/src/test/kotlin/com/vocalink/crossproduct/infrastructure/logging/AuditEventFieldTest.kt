package com.vocalink.crossproduct.infrastructure.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import com.vocalink.crossproduct.domain.audit.UserDetails
import com.vocalink.crossproduct.ui.aspects.EventType
import com.vocalink.crossproduct.ui.aspects.OccurringEvent
import com.vocalink.crossproduct.ui.aspects.OperationType
import java.security.InvalidParameterException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.slf4j.MarkerFactory

class AuditEventFieldTest {

    @ParameterizedTest
    @EnumSource(com.vocalink.crossproduct.infrastructure.logging.AuditEventField::class)
    fun `should resolve not null field values`(field: AuditEventField) {
        val event = mock(ILoggingEvent::class.java)!!
        val occurringEvent = getOccurringEvent()
        val userDetails = getUserDetails()
        val layout = getAuditEventLayout()
        `when`(event.argumentArray).thenReturn(arrayOf(occurringEvent, userDetails))
        `when`(event.marker).thenReturn(MarkerFactory.getMarker(AuditEventLayoutTest.auditMarker))
        `when`(event.timeStamp).thenReturn(AuditEventLayoutTest.clock.millis())

        val fieldValue = field.function.apply(event, layout)

        assertThat(fieldValue).isNotNull()
    }

    @Test
    fun `should extract occurred event from log event`() {
        val event = mock(ILoggingEvent::class.java)!!
        val expectedEvent = getOccurringEvent()
        val userDetails = getUserDetails()
        `when`(event.argumentArray).thenReturn(arrayOf(expectedEvent, userDetails))

        val actualEvent = AuditEventField.getOccurringEvent(event)

        assertThat(actualEvent).usingRecursiveComparison().isEqualTo(expectedEvent)
    }

    @Test
    fun `should throw exception if no occurred event in log event`() {
        val event = mock(ILoggingEvent::class.java)!!

        assertThrows(InvalidParameterException::class.java) { AuditEventField.getOccurringEvent(event) }
    }

    @Test
    fun `should throw exception if occurred event argument in log event has invalid type`() {
        val event = mock(ILoggingEvent::class.java)!!
        `when`(event.argumentArray).thenReturn(arrayOf(Object(), Object()))

        assertThrows(InvalidParameterException::class.java) { AuditEventField.getOccurringEvent(event) }
    }

    @Test
    fun `should extract user details from log event`() {
        val event = mock(ILoggingEvent::class.java)!!
        val occurringEvent = getOccurringEvent()
        val expectedUserDetails = getUserDetails()
        `when`(event.argumentArray).thenReturn(arrayOf(occurringEvent, expectedUserDetails))

        val actualUserDetails = AuditEventField.getUserDetails(event)

        assertThat(actualUserDetails.get()).usingRecursiveComparison().isEqualTo(expectedUserDetails)
    }

    @Test
    fun `should return Optional null if no user details in log event`() {
        val event = mock(ILoggingEvent::class.java)!!
        val occurringEvent = getOccurringEvent()
        `when`(event.argumentArray).thenReturn(arrayOf(occurringEvent))

        val userDetails = AuditEventField.getUserDetails(event)

        assertThat(userDetails.isPresent).isFalse()
    }

    @Test
    fun `should return Optional null if user details argument in log event has invalid type`() {
        val event = mock(ILoggingEvent::class.java)!!
        `when`(event.argumentArray).thenReturn(arrayOf(Object(), Object()))

        val userDetails = AuditEventField.getUserDetails(event)

        assertThat(userDetails.isPresent).isFalse()
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

    private fun getAuditEventLayout(): AuditEventLayout {
        val layout = AuditEventLayout()
        layout.timestampPattern = "dd-MM-yyyy HH:mm:ss.SSS"
        layout.emptyValue = "n/a"
        return layout
    }
}