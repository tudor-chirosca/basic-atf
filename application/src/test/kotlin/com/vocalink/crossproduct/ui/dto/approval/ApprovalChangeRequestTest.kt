package com.vocalink.crossproduct.ui.dto.approval

import com.vocalink.crossproduct.ui.aspects.EventType
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class ApprovalChangeRequestTest {

    @Test
    fun `should return eventType according to BATCH_CANCELLATION requestType`() {
        val request = ApprovalChangeRequest("BATCH_CANCELLATION",
                mapOf("batchId" to "Q27ISTXBANKSESS"), "some message")

        val result = request.eventType

        assertThat(result).isEqualTo(EventType.REQ_BATCH_CANCELLATION)
    }

    @Test
    fun `should return auditable content according to BATCH_CANCELLATION requestType`() {
        val request = ApprovalChangeRequest("BATCH_CANCELLATION",
                mapOf("batchId" to "Q27ISTXBANKSESS"), "some message")

        val result = request.auditableContent

        assertThat(result.get("notes")).isEqualTo(request.notes)
        assertThat(result.get("batchId")).isEqualTo(request.requestedChange.get("batchId"))
    }

    @Test
    fun `should return eventType according to CONFIG_CHANGE requestType`() {
        val request = ApprovalChangeRequest("CONFIG_CHANGE",
                mapOf("batchId" to "Q27ISTXBANKSESS"), "some message")

        val result = request.eventType

        assertThat(result).isEqualTo(EventType.AMEND_PTT_CONFIG)
    }

    @Test
    fun `should return auditable content according to CONFIG_CHANGE requestType`() {
        val request = ApprovalChangeRequest("CONFIG_CHANGE",
                mapOf("id" to "Q27ISTXBANKSESS"), "some message")

        val result = request.auditableContent

        assertThat(result.get("notes")).isEqualTo(request.notes)
        assertThat(result.get("id")).isEqualTo(request.requestedChange.get("id"))
    }
}
