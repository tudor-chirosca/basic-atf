package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.domain.approval.ApprovalStatus
import com.vocalink.crossproduct.domain.approval.ApprovalUser
import com.vocalink.crossproduct.domain.approval.RejectionReason
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalUser
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto
import com.vocalink.crossproduct.ui.facade.ApprovalFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(ApprovalApi::class)
@ContextConfiguration(classes=[TestConfig::class])
class ApprovalControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var approvalFacade: ApprovalFacade

    private val UTF8_CONTENT_TYPE: MediaType = MediaType(MediaType.APPLICATION_JSON.type,
            MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

    private companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val VALID_APPROVAL_DETAILS_RESPONSE = """{
            "status": "APPROVED",
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
            "requestType": "BATCH_CANCELLATION",
            "participantIdentifier": "ESSESESS",
            "participantName": "SEB Bank",
            "requestedChange": {
            "status": "suspended"
            }
        }"""
    }

    @Test
    fun `should return 200 with path variable and return valid response`() {
        val approvalUser = ApprovalUser("John Doe", "12a514")
        val jobId = "10000004"
        val createdAt = ZonedDateTime.of(LocalDateTime.of(2021, 1, 28, 14, 55),  ZoneId.of("UTC"))
        val rejectionReason = RejectionReason(approvalUser,
                "Please check ticket number...")
        val requestedChange = mapOf("status" to "suspended")

        val approvalDetailsDto = ApprovalDetailsDto(
                ApprovalStatus.APPROVED,
                approvalUser, approvalUser,
                createdAt, jobId,
                ApprovalRequestType.BATCH_CANCELLATION,
                "ESSESESS",
                "SEB Bank",
                rejectionReason,
                requestedChange)

        `when`(approvalFacade.getApprovalDetailsById(TestConstants.CONTEXT, ClientType.UI, jobId))
                .thenReturn(approvalDetailsDto)

        mockMvc.perform(MockMvcRequestBuilders.get("/approvals/$jobId")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content()
                        .json(VALID_APPROVAL_DETAILS_RESPONSE))
    }
}
