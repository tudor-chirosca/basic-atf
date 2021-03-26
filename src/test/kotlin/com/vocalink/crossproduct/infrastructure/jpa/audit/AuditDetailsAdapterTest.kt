package com.vocalink.crossproduct.infrastructure.jpa.audit;

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*

open class AuditDetailsAdapterTest {

    private val repositoryJpa = mock(AuditDetailsRepositoryJpa::class.java)!!

    private val adapter = AuditDetailsAdapter(repositoryJpa)

    companion object {
        private var PARTICIPANT_ID = "anyId"

        private var id = UUID.randomUUID()

        private var details = AuditDetailsJpa.builder()
                .id(id)
                .build()
    }

    @Test
    fun `should find details by participant id`() {
        `when`(repositoryJpa.findAllByParticipantId(PARTICIPANT_ID)).thenReturn(listOf(details))

        val auditDetails = adapter.getAuditDetailsById(PARTICIPANT_ID)

        assertThat(auditDetails).isInstanceOf(List::class.java)
        assertThat(auditDetails).isNotEmpty

        assertThat(id).isEqualTo(auditDetails[0].id)
    }

}
