package com.vocalink.crossproduct.ui.dto.approval

import com.fasterxml.jackson.databind.ObjectMapper
import com.vocalink.crossproduct.ui.aspects.EventType
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.skyscreamer.jsonassert.JSONAssert

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApprovalChangeRequestTest {

    private lateinit var objectMapper: ObjectMapper

    private companion object {
        const val REQUEST_WITHOUT_OPTIONAL_FIELDS =
            """{
                "requestType": "CONFIG_CHANGE",
                "requestedChange": {
                    "id": "DABASESXGBG"
                }
            }"""
    }

    @BeforeAll
    fun init() {
        objectMapper = ObjectMapper()
    }

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

        assertThat(result["notes"]).isEqualTo(request.notes)
        assertThat(result["batchId"]).isEqualTo(request.requestedChange["batchId"])
    }

    @Test
    fun `should return eventType according to CONFIG_CHANGE requestType`() {
        val request = ApprovalChangeRequest("CONFIG_CHANGE",
                mapOf("id" to "Q27ISTXBANKSESS"), "some message")

        val result = request.eventType

        assertThat(result).isEqualTo(EventType.AMEND_PARTICIPANT_CONFIG)
    }

    @Test
    fun `should return auditable content according to PARTICIPANT_SUSPEND requestType`() {
        val request = ApprovalChangeRequest("SUSPEND_PARTICIPANT",
                mapOf("id" to "Q27ISTXBANKSESS"), "some message")

        val result = request.auditableContent

        assertThat(result["notes"]).isEqualTo(request.notes)
        assertThat(result["id"]).isEqualTo(request.requestedChange["id"])
    }

    @Test
    fun `should return auditable content according to CONFIG_CHANGE requestType`() {
        val participantId = "DABASESXGBG"
        val requestedValue = mapOf(
            "id" to participantId,
            "name" to "DABA Bankkk",
            "settlementAccountNo" to "1111",
            "debitCapLimit" to "222",
            "debitCapLimitThresholds" to "333",
            "outputTxnVolume" to "444",
            "outputTxnTimeLimit" to "555"
        )
        val request = ApprovalChangeRequest("CONFIG_CHANGE",
                mapOf("id" to participantId).plus(requestedValue),
                "some message")

        val result = request.auditableContent

        assertThat(result["notes"]).isEqualTo(request.notes)
        assertThat(result["requestedValues"]).isEqualTo(requestedValue)
    }

    @Test
    fun `should ignore null optional fields when serialize ApprovalChangeRequest`() {
        val participantId = "DABASESXGBG"
        val request = ApprovalChangeRequest("CONFIG_CHANGE", mapOf("id" to participantId), null)

        val result = objectMapper.writeValueAsString(request)

        JSONAssert.assertEquals(REQUEST_WITHOUT_OPTIONAL_FIELDS, result, true)
    }
}
