package com.vocalink.crossproduct.infrastructure.jpa.audit;

import com.vocalink.crossproduct.domain.audit.AuditDetails
import com.vocalink.crossproduct.domain.audit.Event
import com.vocalink.crossproduct.domain.audit.UserDetails
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException
import com.vocalink.crossproduct.infrastructure.jpa.activities.UserActivityJpa
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.TypedQuery

open class AuditDetailsAdapterTest {

    private val repositoryJpa = mock(AuditDetailsRepositoryJpa::class.java)
    private val entityManager = mock(EntityManager::class.java)
    private val typedQuery = mock(TypedQuery::class.java) as TypedQuery<UserActivityJpa>

    private val adapter = AuditDetailsAdapter(entityManager, repositoryJpa)

    companion object {
        private var PARTICIPANT_ID = "anyId"

        private var USER_NAME = "anyUserName"

        private var id = UUID.randomUUID()

        private var detailsJpa = AuditDetailsJpa.builder()
                .id(id)
                .build()

        val event = Event("product", "client", "any", "any", "any", "any", "any", "any", "any")
        val userDetails = UserDetails.builder()
                .userId("any")
                .build()

        const val QUERY_BY_NAME = "select activity from UserActivityJpa as activity where activity.name = :eventName"
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
    fun `should find details by user name`() {
        `when`(repositoryJpa.findByUsername(USER_NAME)).thenReturn(Optional.of(detailsJpa))

        val auditDetails = adapter.getAuditDetailsByUserName(USER_NAME)

        assertThat(auditDetails).isInstanceOf(AuditDetails::class.java)
        assertThat(auditDetails).isNotNull

        assertThat(id).isEqualTo(auditDetails.id)
    }

    @Test
    fun `should throw exception if user activity not found by name`() {
        `when`(entityManager.createQuery(QUERY_BY_NAME, UserActivityJpa::class.java)).thenReturn(typedQuery)

        assertThatExceptionOfType(EntityNotFoundException::class.java).isThrownBy { adapter.logOperation(event, userDetails) }
    }

    @Test
    fun `should log operation with success`() {
        val activity = UserActivityJpa.builder().id(UUID.randomUUID()).build()

        `when`(entityManager.createQuery(QUERY_BY_NAME, UserActivityJpa::class.java)).thenReturn(typedQuery)
        `when`(typedQuery.singleResult).thenReturn(activity)

        adapter.logOperation(event, userDetails)

        verify(repositoryJpa, atLeastOnce()).save(any())
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
}
