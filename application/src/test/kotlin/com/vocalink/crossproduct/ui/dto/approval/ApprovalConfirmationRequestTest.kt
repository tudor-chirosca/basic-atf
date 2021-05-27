package com.vocalink.crossproduct.ui.dto.approval

import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationType
import com.vocalink.crossproduct.ui.aspects.EventType
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class ApprovalConfirmationRequestTest {

    @Test
    fun `should return `() {
        val approveRequest = ApprovalConfirmationRequest(ApprovalConfirmationType.APPROVE, "some message")

        assertThat(approveRequest.eventType).isEqualTo(EventType.APPROVE_REQUEST)
        assertThat(approveRequest.message).isEqualTo("some message")
    }

    @Test
    fun `should return 2`() {
        val approveRequest = ApprovalConfirmationRequest(ApprovalConfirmationType.REJECT, "some message")

        assertThat(approveRequest.eventType).isEqualTo(EventType.REJECT_REQUEST)
        assertThat(approveRequest.message).isEqualTo("some message")
    }
}
