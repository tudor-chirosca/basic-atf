package com.vocalink.crossproduct.infrastructure.jpa.audit

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.*


@DataJpaTest
@ActiveProfiles("test")
open class AuditDetailsRepositoryJpaTest @Autowired constructor(var auditDetailsRepository: AuditDetailsRepositoryJpa) {

    companion object {
        private const val PARTICIPANT_ID = "any_id"

        private val details = AuditDetailsJpa.builder()
                .id(UUID.randomUUID())
                .activityName("activityString")
                .approvalRequestId("1")
                .correlationId("corId")
                .scheme("P27-SEK")
                .channel("BPS")
                .username(UUID.randomUUID().toString())
                .approvalRequestId("1")
                .requestOrResponseEnum("blah")
                .requestUrl("reqUrl")
                .contents("contentJson")
                .firstName("John")
                .lastName("Snow")
                .participantId(PARTICIPANT_ID)
                .build()
    }

    @BeforeEach
    fun init() {
        auditDetailsRepository.save(details)
    }

    @Test
    fun `should find by participant id`() {
        val found = auditDetailsRepository.findAllByParticipantId(PARTICIPANT_ID)

        assertThat(found).isNotNull
        assertThat(PARTICIPANT_ID).isEqualTo(found[0].participantId)
    }
}
