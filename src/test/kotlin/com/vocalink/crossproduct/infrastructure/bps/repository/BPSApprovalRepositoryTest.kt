package com.vocalink.crossproduct.infrastructure.bps.repository

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.domain.approval.ApprovalStatus
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalRepository
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@BPSTestConfiguration
@Import(BPSApprovalRepository::class)
class BPSApprovalRepositoryTest @Autowired constructor(var approvalRepository: BPSApprovalRepository,
                                                       var mockServer: WireMockServer){

    companion object {
        const val VALID_APPROVAL_DETAILS_REQUEST: String = """
            {
                "id": "10000006"
            }"""

        const val VALID_APPROVAL_DETAILS_RESPONSE: String = """
            {
                "status": "approved",
                "requestedBy": {
                    "name": "John Doe",
                    "id": "12a514"
                },
                "approvedBy": {
                    "name": "John Doe",
                    "id": "12a514"
                },
                "createdAt": "2021-01-28T14:55:00Z",
                "jobId": "10000004",
                "requestType": "batch-cancellation",
                "participantIdentifier": "ESSESESS",
                "participantName": "SEB Bank",
                "requestedChange": {
                    "status": "suspended"
                }
            }"""
    }

    @Test
    fun `should return approval details by id`() {
        val createdAt = ZonedDateTime.of(LocalDateTime.of(2021, 1, 28, 14, 55,0),  ZoneId.of("UTC"))
        mockServer.stubFor(
                post(urlEqualTo("/approval/details"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_APPROVAL_DETAILS_RESPONSE))
                        .withRequestBody(equalToJson(VALID_APPROVAL_DETAILS_REQUEST)))

        val result = approvalRepository.findByJobId("10000006")

        assertThat(result.status).isEqualTo(ApprovalStatus.APPROVED)
        assertThat(result.requestedBy.name).isEqualTo("John Doe")
        assertThat(result.createdAt).isEqualTo(createdAt)
        assertThat(result.jobId).isEqualTo("10000004")
        assertThat(result.requestType).isEqualTo(ApprovalRequestType.BATCH_CANCELLATION)
        assertThat(result.participantIdentifier).isEqualTo("ESSESESS")
        assertThat(result.participantName).isEqualTo("SEB Bank")
        assertThat(result.requestedChange["status"]).isEqualTo("suspended")
    }
}