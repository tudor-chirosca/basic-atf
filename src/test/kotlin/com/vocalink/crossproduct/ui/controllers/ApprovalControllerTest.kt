package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.domain.approval.ApprovalStatus
import com.vocalink.crossproduct.domain.approval.ApprovalUser
import com.vocalink.crossproduct.ui.controllers.api.ApprovalApi
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto
import com.vocalink.crossproduct.ui.facade.api.ApprovalFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

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

        const val VALID_BATCH_CANCELLATION_REQUEST_BODY = """ {
           "requestType" : "BATCH_CANCELLATION",
           "requestedChange" : {
                "batchId": "some_batch_id",
                "status": "some_new_status"
              },
           "notes" : "notes"
        }"""

        const val VALID_STATUS_CHANGE_WITH_NOTES_REQUEST_BODY = """ {
           "requestType" : "STATUS_CHANGE",
           "requestedChange" : {
                "status": "some_new_status"
              },
           "notes" : "notes"
        }"""

        const val VALID_STATUS_CHANGE_REQUEST_BODY = """ {
           "requestType" : "STATUS_CHANGE",
           "requestedChange" : {
                "status": "some_new_status"
              },
           "notes" : "notes"
        }"""

        const val INVALID_CREATE_APPROVAL_REQUEST_BODY = """ {
           "requestType" : "BATCH_CANCELLATION",
           "requestedChange" : {
                "batchId": "some_batch_id",
                "status": "some_new_status"
              }
        }"""

        const val INVALID_MISSING_REQUEST_TYPE_REQUEST_BODY = """ {
           "requestedChange" : {
                "status": "some_new_status"
              },
           "notes" : "notes"
        }"""

        const val INVALID_MISSING_REQUEST_CHANGE_REQUEST_BODY = """ {
           "requestType" : "STATUS_CHANGE",
           "notes" : "notes"
        }"""

        const val VALID_APPROVAL_DETAILS_RESPONSE = """{
            "status": "APPROVED",
            "requestedBy": {
                "name": "John Doe",
                "id": "12a514",
                "participantName": "P27 Scheme"
            },
            "approvedBy": {
                "name": "John Doe",
                "id": "12a514",
                "participantName": "P27 Scheme"
            },
            "createdAt": "2021-01-28T14:55:00Z",
            "jobId": "10000004",
            "requestType": "BATCH_CANCELLATION",
            "participantIdentifier": "ESSESESS",
            "participantName": "SEB Bank",
            "requestComment": "This is the reason that I...",
            "requestedChange": {
                "status": "suspended"
            }
        }"""
    }

    @Test
    fun `should return 200 with path variable and return valid response`() {
        val approvalUser = ApprovalUser("John Doe", "12a514", "P27 Scheme")
        val jobId = "10000004"
        val createdAt = ZonedDateTime.of(LocalDateTime.of(2021, 1, 28, 14, 55),  ZoneId.of("UTC"))
        val requestedChange = mapOf("status" to "suspended")
        val originalData = mapOf("data" to "data")

        val approvalDetailsDto = ApprovalDetailsDto(
                ApprovalStatus.APPROVED,
                approvalUser, approvalUser,
                createdAt, jobId,
                ApprovalRequestType.BATCH_CANCELLATION,
                "ESSESESS",
                "SEB Bank",
                "This is the reason that I...",
                approvalUser, "Notes",
                originalData, requestedChange)

        `when`(approvalFacade.getApprovalDetailsById(TestConstants.CONTEXT, ClientType.UI, jobId))
                .thenReturn(approvalDetailsDto)

        mockMvc.perform(get("/approvals/$jobId")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_APPROVAL_DETAILS_RESPONSE))
    }

    @Test
    fun `should return 200 on valid batch cancellation body for create Approval`() {
        val approvalUser = ApprovalUser("John Doe", "12a514", "P27 Scheme")
        val jobId = "10000004"
        val createdAt = ZonedDateTime.of(LocalDateTime.of(2021, 1, 28, 14, 55),  ZoneId.of("UTC"))
        val requestedChange = mapOf("status" to "suspended")
        val originalData = mapOf("data" to "data")

        val approvalDetailsDto = ApprovalDetailsDto(
                ApprovalStatus.APPROVED,
                approvalUser, approvalUser,
                createdAt, jobId,
                ApprovalRequestType.BATCH_CANCELLATION,
                "ESSESESS",
                "SEB Bank",
                "This is the reason that I...",
                approvalUser, "Notes",
                originalData, requestedChange)

        `when`(approvalFacade.requestApproval(any(), any(), any()))
                .thenReturn(approvalDetailsDto)

        mockMvc.perform(post("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_BATCH_CANCELLATION_REQUEST_BODY))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_APPROVAL_DETAILS_RESPONSE))
    }

    @Test
    fun `should return 200 on valid status change body for create Approval`() {
        val approvalUser = ApprovalUser("John Doe", "12a514", "P27 Scheme")
        val jobId = "10000004"
        val createdAt = ZonedDateTime.of(LocalDateTime.of(2021, 1, 28, 14, 55),  ZoneId.of("UTC"))
        val requestedChange = mapOf("status" to "suspended")
        val originalData = mapOf("data" to "data")

        val approvalDetailsDto = ApprovalDetailsDto(
                ApprovalStatus.APPROVED,
                approvalUser, approvalUser,
                createdAt, jobId,
                ApprovalRequestType.BATCH_CANCELLATION,
                "ESSESESS",
                "SEB Bank",
                "This is the reason that I...",
                approvalUser, "Notes",
                originalData, requestedChange)

        `when`(approvalFacade.requestApproval(any(), any(), any()))
                .thenReturn(approvalDetailsDto)

        mockMvc.perform(post("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_STATUS_CHANGE_REQUEST_BODY))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_APPROVAL_DETAILS_RESPONSE))
    }

    @Test
    fun `should return 200 on valid status change with notes body for create Approval`() {
        val approvalUser = ApprovalUser("John Doe", "12a514", "P27 Scheme")
        val jobId = "10000004"
        val createdAt = ZonedDateTime.of(LocalDateTime.of(2021, 1, 28, 14, 55),  ZoneId.of("UTC"))
        val requestedChange = mapOf("status" to "suspended")
        val originalData = mapOf("data" to "data")

        val approvalDetailsDto = ApprovalDetailsDto(
                ApprovalStatus.APPROVED,
                approvalUser, approvalUser,
                createdAt, jobId,
                ApprovalRequestType.BATCH_CANCELLATION,
                "ESSESESS",
                "SEB Bank",
                "This is the reason that I...",
                approvalUser, "Notes",
                originalData, requestedChange)

        `when`(approvalFacade.requestApproval(any(), any(), any()))
                .thenReturn(approvalDetailsDto)

        mockMvc.perform(post("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_STATUS_CHANGE_WITH_NOTES_REQUEST_BODY))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_APPROVAL_DETAILS_RESPONSE))
    }

    @Test
    fun `should fail with 400 when required notes for BATCH_CANCELLATION is missing`() {
        mockMvc.perform(post("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_CREATE_APPROVAL_REQUEST_BODY))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Notes parameter is mandatory on a BATCH_CANCELLATION")))
    }

    @Test
    fun `should fail with 400 when required requestType is missing`() {
        mockMvc.perform(post("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_MISSING_REQUEST_TYPE_REQUEST_BODY))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Missing required creator property 'requestType'")))
    }

    @Test
    fun `should fail with 400 when required requestedChange is missing`() {
        mockMvc.perform(post("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_MISSING_REQUEST_CHANGE_REQUEST_BODY))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Missing required creator property 'requestedChange'")))
    }
}
