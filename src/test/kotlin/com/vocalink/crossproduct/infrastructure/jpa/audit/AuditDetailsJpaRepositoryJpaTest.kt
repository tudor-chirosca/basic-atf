package com.vocalink.crossproduct.infrastructure.jpa.audit;

import com.vocalink.crossproduct.infrastructure.jpa.activities.UserActivityJpa
import com.vocalink.crossproduct.infrastructure.jpa.activities.UserActivityRepositoryJpa
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.*


@DataJpaTest
@ActiveProfiles("test")
open class AuditDetailsJpaRepositoryJpaTest @Autowired constructor(var auditDetailsRepository: AuditDetailsRepositoryJpa,
                                                                   var userActivityRepository: UserActivityRepositoryJpa) {

    companion object {
        private const val PARTICIPANT_ID = "any_id"

        private val activity = UserActivityJpa.builder()
                .description("desc")
                .name("name")
                .build()

        val details = AuditDetailsJpa.builder()
                .activityId(activity)
                .approvalRequestId("1")
                .correlationId("corId")
                .channel("BPS")
                .username("userName")
                .userActivityString("activityString")
                .approvalRequestId("1")
                .requestOrResponseEnum("blah")
                .requestUrl("reqUrl")
                .contents("contentJson")
                .userId(UUID.randomUUID().toString())
                .firstName("John")
                .lastName("Snow")
                .participantId(PARTICIPANT_ID)
                .build()
    }

    @BeforeEach
    fun init() {
        userActivityRepository.save(activity)
        auditDetailsRepository.save(details)
    }

    @Test
    fun `should find by participant id`() {
        val found = auditDetailsRepository.findAllByParticipantId(PARTICIPANT_ID)

        assertThat(found).isNotNull
        assertThat(PARTICIPANT_ID).isEqualTo(found[0].participantId)
    }
}
