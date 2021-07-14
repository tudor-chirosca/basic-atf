package com.vocalink.crossproduct.ui.dto.approval

import com.fasterxml.jackson.databind.ObjectMapper
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationType
import com.vocalink.crossproduct.ui.aspects.EventType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.skyscreamer.jsonassert.JSONAssert

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApprovalConfirmationRequestTest {

    private lateinit var objectMapper: ObjectMapper

    private companion object {
        const val REQUEST_WITH_MESSAGE_NULL = """{"action":"REJECT"}"""
    }

    @BeforeAll
    fun init() {
        objectMapper = ObjectMapper()
    }

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

    @Test
    fun `should ignore null optional fields when serialize ApprovalConfirmationRequest`() {
        val request = ApprovalConfirmationRequest(ApprovalConfirmationType.REJECT, null)

        val result = objectMapper.writeValueAsString(request)

        JSONAssert.assertEquals(REQUEST_WITH_MESSAGE_NULL, result, true)
    }
}
