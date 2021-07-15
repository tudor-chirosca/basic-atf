package com.vocalink.crossproduct.infrastructure.jpa.audit

import com.vocalink.crossproduct.domain.audit.AuditSearchRequest
import com.vocalink.crossproduct.domain.audit.Event
import com.vocalink.crossproduct.domain.audit.UserDetails
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException
import java.time.Clock
import java.util.Optional
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.projection.SpelAwareProxyProjectionFactory

open class AuditDetailsAdapterTest {

    private val repositoryJpa = mock(AuditDetailsRepositoryJpa::class.java)

    private val adapter = AuditDetailsAdapter(repositoryJpa, Clock.systemUTC())

    companion object {
        private var PARTICIPANT_ID = "anyId"

        private var USER_NAME = "anyUserName"

        private var id = UUID.randomUUID()

        private var detailsJpa = AuditDetailsJpa.builder()
                .id(id)
                .build()

        private val event = Event.builder()
            .product("product")
            .client("client")
            .requestUrl("request_url")
            .userId("user_id")
            .participantId("participant_id")
            .content("contents")
            .correlationId("correlation_id")
            .eventType("event_type")
            .operationType("operation_type")
            .approvalRequestId("approval_request_id")
            .ipAddress("127.0.0.1")
            .userRoleList("CLEARING,READ-ONLY")
            .scheme("scheme_name")
            .build()
        private val userDetails = UserDetails.builder()
                .userId("user_id")
                .build()
    }

    @Test
    fun `should find details by participant id`() {
        `when`(repositoryJpa.findAllByParticipantId(PARTICIPANT_ID)).thenReturn(listOf(detailsJpa))

        val auditDetails = adapter.getAuditDetailsByParticipantId(PARTICIPANT_ID)

        assertThat(auditDetails).isInstanceOf(List::class.java)
        assertThat(auditDetails).isNotEmpty

        assertThat(id).isEqualTo(auditDetails[0].id)
    }

    @Test
    fun `should throw exception with incorrect user name`() {
        `when`(repositoryJpa.findByUsername(any())).thenReturn(null)

        assertThatExceptionOfType(EntityNotFoundException::class.java)
                .isThrownBy { adapter.getAuditDetailsByUsername(USER_NAME) }
    }

    @Test
    fun `should log operation with success`() {
        adapter.logOperation(event, userDetails)
        val captor = ArgumentCaptor.forClass(AuditDetailsJpa::class.java)

        verify(repositoryJpa, atLeastOnce()).save(captor.capture())
        val result = captor.value
        assertThat(result.approvalRequestId).isEqualTo(event.approvalRequestId)
        assertThat(result.activityName).isEqualTo(event.eventType)
        assertThat(result.correlationId).isEqualTo(event.correlationId)
        assertThat(result.requestOrResponseEnum).isEqualTo(event.operationType)
        assertThat(result.channel).isEqualTo(event.client)
        assertThat(result.requestUrl).isEqualTo(event.requestUrl)
        assertThat(result.contents).isEqualTo(event.content)
        assertThat(result.participantId).isEqualTo(event.participantId)
        assertThat(result.username).isEqualTo(event.userId)
        assertThat(result.firstName).isEqualTo(userDetails.firstName)
        assertThat(result.lastName).isEqualTo(userDetails.lastName)
        assertThat(result.ipsSuiteApplicationName).isEqualTo(event.product)
        assertThat(result.scheme).isEqualTo(event.scheme)
        assertThat(result.approvalRequestId).isEqualTo(event.approvalRequestId)
        assertThat(result.ipAddress).isEqualTo(event.ipAddress)
        assertThat(result.userRoleList).isEqualTo(event.userRoleList)
    }

    @Test
    fun `should get audit details by id`() {
        val id = UUID.randomUUID()
        val serviceId = 1L
        val detailsJpa = AuditDetailsJpa.builder().id(id).serviceId(serviceId).build()

        `when`(repositoryJpa.findById(id)).thenReturn(Optional.of(detailsJpa))

        val auditDetails = adapter.getAuditDetailsById(id.toString())

        assertThat(id).isEqualTo(auditDetails.id)
    }

    @Test
    fun `should throw exception if no entity found by id`() {
        val id = UUID.randomUUID().toString()

        `when`(repositoryJpa.findById(any())).thenReturn(Optional.empty())

        assertThatExceptionOfType(EntityNotFoundException::class.java)
                .isThrownBy { adapter.getAuditDetailsById(id) }
    }

    @Test
    fun `should throw exception if input id is invalid UUID`() {
        val id = 1L

        assertThatExceptionOfType(InfrastructureException::class.java)
                .isThrownBy { adapter.getAuditDetailsById(id.toString()) }
    }

    @Test
    fun `should get user references by participant id`() {
        val id = "id"

        adapter.getAuditDetailsByParticipantIdAndGroupByUser(id)

        verify(repositoryJpa, atLeastOnce()).findByParticipantId(id)
    }

    @Test
    fun `should find anything with success with default sorting option`() {
        val parameters = AuditSearchRequest.builder()
                .offset(0)
                .limit(10)
                .build()

        val factory = SpelAwareProxyProjectionFactory()
        val projection = factory.createProjection(AuditDetailsView::class.java)
        val pageImpl = PageImpl<AuditDetailsView>(listOf(projection))

        `when`(repositoryJpa.getAllByParameters(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(pageImpl)

        val details = adapter.getAuditDetailsByParameters(parameters)

        assertThat(details.totalResults).isEqualTo(1)
        assertThat(details.items.size).isEqualTo(1)
    }

    @Test
    fun `should find anything and use multiple sorting and ordering`() {
        val parameters = AuditSearchRequest.builder()
                .offset(0)
                .limit(10)
                .sort(listOf("+serviceId", "-createdAt", "user", "-responseContent"))
                .build()

        val factory = SpelAwareProxyProjectionFactory()
        val projection = factory.createProjection(AuditDetailsView::class.java)
        val pageImpl = PageImpl<AuditDetailsView>(listOf(projection))

        `when`(repositoryJpa.getAllByParameters(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(pageImpl)

        val details = adapter.getAuditDetailsByParameters(parameters)

        assertThat(details.totalResults).isEqualTo(1)
        assertThat(details.items.size).isEqualTo(1)
    }
}
