package com.vocalink.crossproduct.infrastructure.jpa.audit;

import com.vocalink.crossproduct.infrastructure.jpa.activities.UserActivity
import com.vocalink.crossproduct.infrastructure.jpa.activities.UserActivityRepositoryJpa
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.*


@DataJpaTest
@ActiveProfiles("test")
open class AuditDetailsRepositoryJpaTest @Autowired constructor(var auditDetailsRepository: AuditDetailsRepositoryJpa,
                                                                var userActivityRepository: UserActivityRepositoryJpa) {

    companion object {
        val activity = UserActivity.builder()
                .description("desc")
                .name("name")
                .build()

        val details = AuditDetails.builder()
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
                .participantId("anyBIC")
                .build()
    }

    @Test
    fun `should find all audit details`() {
        userActivityRepository.save(activity)
        auditDetailsRepository.save(details)

        val all = auditDetailsRepository.findAll()

        assertThat(all).isNotNull
        assertThat(all).contains(details)
    }
}