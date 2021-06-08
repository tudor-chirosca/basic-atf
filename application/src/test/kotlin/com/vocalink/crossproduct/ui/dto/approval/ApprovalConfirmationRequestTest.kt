package com.vocalink.crossproduct.ui.dto.approval

import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationType
import com.vocalink.crossproduct.ui.aspects.EventType
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class ApprovalConfirmationRequestTest {

    @Test
    fun `should return APPROVE_REQUEST event type `() {
        val approveRequest = ApprovalConfirmationRequest(ApprovalConfirmationType.APPROVE, "some message")

        assertThat(approveRequest.eventType).isEqualTo(EventType.APPROVE_REQUEST)
    }

    @Test
    fun `should return REJECT_REQUEST event type`() {
        val approveRequest = ApprovalConfirmationRequest(ApprovalConfirmationType.REJECT, "some message")

        assertThat(approveRequest.eventType).isEqualTo(EventType.REJECT_REQUEST)
    }

    @Test
    fun `should return auditable content`() {
        val approveRequest = ApprovalConfirmationRequest(ApprovalConfirmationType.REJECT, "some message")

        assertThat(approveRequest.auditableContent).isEqualTo(mapOf("message" to "some message"))
    }
}
