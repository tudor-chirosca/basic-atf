package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.domain.approval.ApprovalStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType.FUNDED
import com.vocalink.crossproduct.domain.participant.SuspensionLevel
import com.vocalink.crossproduct.ui.aspects.EventType
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationResponseDto
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto
import com.vocalink.crossproduct.ui.dto.participant.ApprovalUserDto
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto
import com.vocalink.crossproduct.ui.facade.api.ApprovalFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class ApprovalControllerTest : ControllerTest() {

    @MockBean
    private lateinit var approvalFacade: ApprovalFacade

    private companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val VALID_APPROVAL_CONFIRMATION_REQUEST_BODY = """ {
           "action" : "APPROVE",
           "message" : "notes"
        }"""

        const val INVALID_APPROVAL_CONFIRMATION_REQUEST_BODY = """ {
           "message" : "notes"
        }"""

        const val INVALID_APPROVAL_CONFIRMATION_TYPE_REQUEST_BODY = """ {
           "action" : "WRONG_CONFIRMATION_TYPE",
           "message" : "notes"
        }"""

        const val VALID_BATCH_CANCELLATION_REQUEST_BODY = """ {
           "requestType" : "BATCH_CANCELLATION",
           "requestedChange" : {
                "batchId": "some_batch_id",
                "status": "some_new_status"
              },
           "notes" : "notes"
        }"""

        const val VALID_STATUS_CHANGE_WITH_NOTES_REQUEST_BODY = """ {
           "requestType" : "PARTICIPANT_SUSPEND",
           "requestedChange" : {
                "id": "CITISESXXXX",
                "status": "some_new_status"
              },
           "notes" : "notes"
        }"""

        const val VALID_STATUS_CHANGE_REQUEST_BODY = """ {
           "requestType" : "PARTICIPANT_SUSPEND",
           "requestedChange" : {
                "id": "CITISESXXXX",
                "status": "some_new_status"
              },
           "notes" : "notes"
        }"""

        const val INVALID_CREATE_APPROVAL_REQUEST_BODY_BIGGER_THAN_99_BILLIONS = """ {
           "requestType" : "BATCH_CANCELLATION",
           "requestedChange" : {
                "debitCapLimit": 99000000001,
                "name": "some_new_name"
              },
           "notes" : "notes"
        }"""

        const val INVALID_CREATE_APPROVAL_REQUEST_BODY_0_VALUE = """ {
           "requestType" : "BATCH_CANCELLATION",
           "requestedChange" : {
                "debitCapLimit": 0,
                "name": "some_new_name"
              },
           "notes" : "notes"
        }"""

        const val INVALID_CREATE_APPROVAL_REQUEST_BODY_DECIMAL_VALUE = """ {
           "requestType" : "BATCH_CANCELLATION",
           "requestedChange" : {
                "debitCapLimit": 100.99,
                "name": "some_new_name"
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

        const val VALID_APPROVAL_CONFIRMATION_RESPONSE = """ {
           "responseMessage" : "response_msg"
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
            "participants": [{
                "participantIdentifier": "ESSESESS",
                "name": "SEB Bank",
                "participantType": "FUNDED",
                "status": null,
                "schemeCode": null
            }],
            "requestComment": "This is the reason that I...",
            "requestedChange": {
                "status": "suspended"
            },
            "suspensionLevel": "SCHEME_FUNDING"
        }"""

        const val VALID_APPROVAL_RESPONSE = """{
                "totalResults": 26,
                "items": [
               {
                   "status": "PENDING",
                   "requestedBy": {
                       "name": "John Doe",
                       "id": "12a514",
                       "participantName": "P27-SEK"
                   },
                   "createdAt": "2021-02-08T14:55:00Z",
                   "jobId": "10000004",
                   "requestType": "PARTICIPANT_SUSPEND",
                   "participants": [{
                        "participantIdentifier": "ESSESESS",
                        "name": "SEB Bank",
                        "participantType": "FUNDED",
                        "status": null,
                        "schemeCode": null
                    }],
                   "requestComment": "This is the reason that I have requested this change to be made.",
                   "notes": "Please check ticket number 342 and resubmit the change.",
                   "originalData": {
                       "status": "WAITING-FORAPPROVAL"
                   },
                   "requestedChange": {
                       "status": "REJECTED"
                   }
               }
            ]
        }
        """
        const val VALID_APPROVAL_REQUEST_BY_RESPONSE: String = """
        [
            {
                 "name": "John Doe",
                 "id": "12a514",
                 "participantName": "P27"
            }
        ]
        """
        const val VALID_APPROVAL_REQUEST_TYPE_RESPONSE: String = """
        [
            "BATCH_CANCELLATION",
            "CONFIG_CHANGE",
            "PARTICIPANT_SUSPEND",
            "PARTICIPANT_UNSUSPEND"
        ]
        """
    }

    @Test
    fun `should return 200 with path variable and return valid response`() {
        val approvalUser = ApprovalUserDto("John Doe", "12a514", "P27 Scheme")
        val jobId = "10000004"
        val createdAt = ZonedDateTime.of(LocalDateTime.of(2021, 1, 28, 14, 55), ZoneId.of("UTC"))
        val requestedChange = mapOf("status" to "suspended")
        val originalData = mapOf("data" to "data")
        val participantReferenceDto = ParticipantReferenceDto("ESSESESS", "SEB Bank", FUNDED, null, null)

        val approvalDetailsDto = ApprovalDetailsDto(
            ApprovalStatus.APPROVED,
            approvalUser, approvalUser, createdAt,
            createdAt, jobId,
            ApprovalRequestType.BATCH_CANCELLATION,
            listOf(participantReferenceDto),
            "This is the reason that I...",
            approvalUser, createdAt, "Notes",
            originalData, requestedChange, SuspensionLevel.SCHEME_FUNDING
        )

        `when`(approvalFacade.getApprovalDetailsById(TestConstants.CONTEXT, ClientType.UI, jobId))
            .thenReturn(approvalDetailsDto)

        mockMvc.perform(
            get("/approvals/$jobId")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(VALID_APPROVAL_DETAILS_RESPONSE))
    }

    @Test
    fun `should return 200 on valid batch cancellation body for create Approval`() {
        val approvalUser = ApprovalUserDto("John Doe", "12a514", "P27 Scheme")
        val jobId = "10000004"
        val createdAt = ZonedDateTime.of(LocalDateTime.of(2021, 1, 28, 14, 55), ZoneId.of("UTC"))
        val requestedChange = mapOf("status" to "suspended")
        val originalData = mapOf("data" to "data")
        val participantReferenceDto = ParticipantReferenceDto("ESSESESS", "SEB Bank", FUNDED, null, null)

        val approvalDetailsDto = ApprovalDetailsDto(
            ApprovalStatus.APPROVED,
            approvalUser, approvalUser, createdAt,
            createdAt, jobId,
            ApprovalRequestType.BATCH_CANCELLATION,
            listOf(participantReferenceDto),
            "This is the reason that I...",
            approvalUser, createdAt, "Notes",
            originalData, requestedChange, SuspensionLevel.SCHEME_FUNDING
        )

        `when`(approvalFacade.requestApproval(any(), any(), any()))
            .thenReturn(approvalDetailsDto)

        mockMvc.perform(
            post("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_BATCH_CANCELLATION_REQUEST_BODY)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(VALID_APPROVAL_DETAILS_RESPONSE))
    }

    @Test
    fun `should return 200 on valid status change body for create Approval`() {
        val approvalUser = ApprovalUserDto("John Doe", "12a514", "P27 Scheme")
        val jobId = "10000004"
        val createdAt = ZonedDateTime.of(LocalDateTime.of(2021, 1, 28, 14, 55), ZoneId.of("UTC"))
        val requestedChange = mapOf("status" to "suspended")
        val originalData = mapOf("data" to "data")
        val participantReferenceDto = ParticipantReferenceDto("ESSESESS", "SEB Bank", FUNDED, null, null)

        val approvalDetailsDto = ApprovalDetailsDto(
            ApprovalStatus.APPROVED,
            approvalUser, approvalUser, createdAt,
            createdAt, jobId,
            ApprovalRequestType.BATCH_CANCELLATION,
            listOf(participantReferenceDto),
            "This is the reason that I...",
            approvalUser, createdAt, "Notes",
            originalData, requestedChange, SuspensionLevel.SCHEME_FUNDING
        )

        `when`(approvalFacade.requestApproval(any(), any(), any()))
            .thenReturn(approvalDetailsDto)

        mockMvc.perform(
            post("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_STATUS_CHANGE_REQUEST_BODY)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(VALID_APPROVAL_DETAILS_RESPONSE))
    }

    @Test
    fun `should return 200 on valid status change with notes body for create Approval`() {
        val approvalUser = ApprovalUserDto("John Doe", "12a514", "P27 Scheme")
        val jobId = "10000004"
        val createdAt = ZonedDateTime.of(LocalDateTime.of(2021, 1, 28, 14, 55), ZoneId.of("UTC"))
        val requestedChange = mapOf("status" to "suspended")
        val originalData = mapOf("data" to "data")
        val participantReferenceDto = ParticipantReferenceDto("ESSESESS", "SEB Bank", FUNDED, null, null)

        val approvalDetailsDto = ApprovalDetailsDto(
            ApprovalStatus.APPROVED,
            approvalUser, approvalUser, createdAt,
            createdAt, jobId,
            ApprovalRequestType.BATCH_CANCELLATION,
            listOf(participantReferenceDto),
            "This is the reason that I...",
            approvalUser, createdAt, "Notes",
            originalData, requestedChange, SuspensionLevel.SCHEME_FUNDING
        )

        `when`(approvalFacade.requestApproval(any(), any(), any()))
            .thenReturn(approvalDetailsDto)

        mockMvc.perform(
            post("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_STATUS_CHANGE_WITH_NOTES_REQUEST_BODY)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(VALID_APPROVAL_DETAILS_RESPONSE))
    }

    @Test
    fun `should fail with 400 when required notes for BATCH_CANCELLATION is missing`() {
        mockMvc.perform(
            post("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_CREATE_APPROVAL_REQUEST_BODY)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("Notes parameter is mandatory on a BATCH_CANCELLATION")))
    }

    @Test
    fun `should fail with 400 when required requestType is missing`() {
        mockMvc.perform(
            post("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_MISSING_REQUEST_TYPE_REQUEST_BODY)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("Missing required creator property 'requestType'")))
    }

    @Test
    fun `should fail with 400 when required requestedChange is missing`() {
        mockMvc.perform(
            post("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_MISSING_REQUEST_CHANGE_REQUEST_BODY)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("Missing required creator property 'requestedChange'")))
    }

    @Test
    fun `should fail with 400 on wrong sorting param`() {
        mockMvc.perform(get("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("limit", "20")
                .param("offset", "0")
                .param("sort", "-wrong_param"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Wrong sorting parameter")))
    }

    @Test
    fun `should fail with 400 on debitCapLimit bigger than 99 billions`() {
        mockMvc.perform(
                post("/approvals")
                        .contentType(UTF8_CONTENT_TYPE)
                        .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                        .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                        .content(INVALID_CREATE_APPROVAL_REQUEST_BODY_BIGGER_THAN_99_BILLIONS)
        )
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Please enter a value between 1 and 99 billion")))
    }

    @Test
    fun `should fail with 400 on debitCapLimit 0 value`() {
        mockMvc.perform(
                post("/approvals")
                        .contentType(UTF8_CONTENT_TYPE)
                        .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                        .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                        .content(INVALID_CREATE_APPROVAL_REQUEST_BODY_0_VALUE)
        )
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Please enter a value between 1 and 99 billion")))
    }

    @Test
    fun `should fail with 400 on debitCapLimit decimal value`() {
        mockMvc.perform(
                post("/approvals")
                        .contentType(UTF8_CONTENT_TYPE)
                        .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                        .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                        .content(INVALID_CREATE_APPROVAL_REQUEST_BODY_DECIMAL_VALUE)
        )
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Inserted number should not be Decimal")))
    }

    @Test
    fun `should return 200 if no criteria specified in request`() {
        val approvalUser = ApprovalUserDto("John Doe", "12a514", "P27-SEK")
        val jobId = "10000004"
        val createdAt = ZonedDateTime.parse("2021-02-08T14:55:00Z")
        val requestedChange = mapOf("status" to "REJECTED")
        val originalData = mapOf("status" to "WAITING-FORAPPROVAL")
        val participantReferenceDto = ParticipantReferenceDto("ESSESESS", "SEB Bank", FUNDED, null, null)

        val approvalDetailsDto = ApprovalDetailsDto(
            ApprovalStatus.PENDING,
            approvalUser, approvalUser, createdAt,
            createdAt, jobId,
            ApprovalRequestType.PARTICIPANT_SUSPEND,
            listOf(participantReferenceDto),
            "This is the reason that I have requested this change to be made.",
            approvalUser, createdAt, "Please check ticket number 342 and resubmit the change.",
            originalData, requestedChange,SuspensionLevel.SCHEME_FUNDING
        )

        `when`(approvalFacade.getApprovals(any(), any(), any()))
            .thenReturn(PageDto(26, listOf(approvalDetailsDto)))

        mockMvc.perform(
            get("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(VALID_APPROVAL_RESPONSE))
    }

    @Test
    fun `should return 200 approve or reject Approval request`() {
        val approvalDetailsDto = ApprovalConfirmationResponseDto(
                "response_msg"
        )
        `when`(approvalFacade.submitApprovalConfirmation(any(), any(), any(), any()))
                .thenReturn(approvalDetailsDto)
        mockMvc.perform(
                post("/approvals/10000004")
                        .contentType(UTF8_CONTENT_TYPE)
                        .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                        .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                        .content(VALID_APPROVAL_CONFIRMATION_REQUEST_BODY)
        )
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_APPROVAL_CONFIRMATION_RESPONSE))
    }

    @Test
    fun `should fail with 400 when required action is missing`() {
        mockMvc.perform(
                post("/approvals/10000004")
                        .contentType(UTF8_CONTENT_TYPE)
                        .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                        .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                        .content(INVALID_APPROVAL_CONFIRMATION_REQUEST_BODY)
        )
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Missing required creator property 'action'")))
    }

    @Test
    fun `should fail with 400 on wrong confirmation type`() {
        mockMvc.perform(
                post("/approvals/10000004")
                        .contentType(UTF8_CONTENT_TYPE)
                        .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                        .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                        .content(INVALID_APPROVAL_CONFIRMATION_TYPE_REQUEST_BODY)
        )
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("not one of the values accepted for Enum class")))
    }

    @Test
    fun `should fail with 400 on date older than DAYS_LIMIT`() {
        mockMvc.perform(get("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("from_date", "2020-02-15T00:00:00Z"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("date_from can not be earlier than DAYS_LIMIT")))

        verifyNoInteractions(approvalFacade)
    }

    @Test
    fun `should fail with 200 with all parameters specified in request`() {
        val fromDate = ZonedDateTime.now(ZoneId.of("UTC")).toString()
        val toDate = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5).toString()

        `when`(approvalFacade.getApprovals(any(), any(), any()))
                .thenReturn(PageDto(0, null))

        mockMvc.perform(get("/approvals")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("offset", "0")
                .param("limit", "20")
                .param("jobId", "9900000")
                .param("from_date", fromDate)
                .param("to_date", toDate)
                .param("participant_ids", "NDEASESSXXX")
                .param("request_types", "BATCH_CANCELLATION")
                .param("requested_by", "23451sdf")
                .param("statuses", "PENDING")
                .param("sort", "status"))
                .andExpect(status().isOk)

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.VIEW_APPROVAL_DASHBOARD)
    }

    @Test
    fun `should return 200 on Approval request By references`() {
        val approvalUser = ApprovalUserDto("John Doe", "12a514", "P27")
        `when`(approvalFacade.findRequestedDetails(any(), any()))
                .thenReturn(listOf(approvalUser))

        mockMvc.perform(
                get("/reference/approvals/request-by")
                        .contentType(UTF8_CONTENT_TYPE)
                        .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                        .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
        )
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_APPROVAL_REQUEST_BY_RESPONSE))
    }

    @Test
    fun `should return 200 on Approval request types references`() {
        val requestTypes = listOf(
                ApprovalRequestType.BATCH_CANCELLATION,
                ApprovalRequestType.CONFIG_CHANGE,
                ApprovalRequestType.PARTICIPANT_SUSPEND,
                ApprovalRequestType.PARTICIPANT_UNSUSPEND
        )
        `when`(approvalFacade.findApprovalRequestTypes(any(), any()))
                .thenReturn(requestTypes)

        mockMvc.perform(
                get("/reference/approvals/request-types")
                        .contentType(UTF8_CONTENT_TYPE)
                        .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                        .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
        )
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_APPROVAL_REQUEST_TYPE_RESPONSE))
    }
}
