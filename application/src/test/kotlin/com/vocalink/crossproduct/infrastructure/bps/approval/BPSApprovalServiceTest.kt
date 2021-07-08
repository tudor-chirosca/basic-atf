package com.vocalink.crossproduct.infrastructure.bps.approval

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.put
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmation
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationType
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@BPSTestConfiguration
@Import(BPSApprovalService::class)
class BPSApprovalServiceTest @Autowired constructor(var approvalService: BPSApprovalService,
                                                    var mockServer: WireMockServer){

    companion object {
        const val VALID_APPROVAL_CONFIRMATION_REQUEST: String = """
            {
                "approvalId": "10000004",
                "notes": "notes",
                "isApproved": true
            }"""

        const val VALID_APPROVAL_CONFIRMATION_RESPONSE: String = """
            {
                "responseMessage": "response_msg"
            }"""
    }

    @Test
    fun `should return approval response on approval confirmation request`() {
        mockServer.stubFor(
                put(urlEqualTo("/approvals/P27-SEK"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_APPROVAL_CONFIRMATION_RESPONSE))
                        .withRequestBody(equalToJson(VALID_APPROVAL_CONFIRMATION_REQUEST)))
        val approvalConfirmation = ApprovalConfirmation(
                "10000004", ApprovalConfirmationType.APPROVE, "notes"
        )
        val result = approvalService.submitApprovalConfirmation(approvalConfirmation)
        assertThat(result.responseMessage).isEqualTo("response_msg")
    }

    @Test
    fun `should get request types`() {
        val requestTypes = approvalService.requestTypes

        assertThat(requestTypes).isNotEmpty
        assertThat(requestTypes[0]).isInstanceOf(ApprovalRequestType::class.java)
    }
}