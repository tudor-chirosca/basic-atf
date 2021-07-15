package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.aspects.EventType
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.audit.AuditDetailsDto
import com.vocalink.crossproduct.ui.dto.audit.AuditDto
import com.vocalink.crossproduct.ui.dto.audit.AuditRequestParams
import com.vocalink.crossproduct.ui.dto.audit.ParticipantDetailsDto
import com.vocalink.crossproduct.ui.dto.audit.UserDetailsDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset.UTC

class AuditControllerTest : ControllerTest() {

    private companion object {
        const val VALID_AUDIT_EVENTS_RESPONSE = """["UI"]"""
        const val VALID_USER_AUDIT_RESPONSE = """[{"username":"e000000","fullName":"John Doe"}]"""
        const val VALID_AUDIT_BY_SERVICE_ID_RESPONSE: String = """
        {
            "serviceId": "SEKB-22",
            "eventType": "TRANSACTION_DETAILS",
            "product": "BPS",
            "entity": {
                "bic": "NDEASESSXXX",
                "participantName": "Nordea"
            },
            "user": {
                "username": "12a521",
                "fullName": "Reuben Barnes"
            },
            "customer": "P27-SEK",
            "requestDate": "2021-04-04T12:00:00Z",
            "responseDate": "2021-04-04T13:00:00Z",
            "request": "20210115FORXSES1B2159",
            "response": "OK"
        }"""

        const val VALID_AUDIT_BY_SERVICE_ID_ARID_RESPONSE: String = """
        {
            "serviceId": "SEKB-22",
            "eventType": "REQ_BATCH_CANCELLATION",
            "product": "BPS",
            "entity": {
                "bic": "NDEASESSXXX",
                "participantName": "Nordea"
            },
            "user": {
                "username": "12a521",
                "fullName": "Reuben Barnes"
            },
            "customer": "P27-SEK",
            "requestDate": "2021-04-04T12:00:00Z",
            "responseDate": "2021-04-04T13:00:00Z",
            "request": "20210115FORXSES1B2159",
            "response": "OK",
            "approvalRequestId": "15f62598-0a49-4754-8081-7f296573cb5d"
        }"""

        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"
        const val PARTICIPANT_ID_PARAMETER = "participantId"

        const val VALID_GET_AUDIT_RESPONSE = """{"totalResults":0,"items":[]}"""
    }

    @Test
    fun `should get Audit logs`() {
        val result: PageDto<AuditDto> = PageDto(0, null)
        `when`(auditFacade.getAuditLogs(any(), any(), any(AuditRequestParams::class.java))).thenReturn(result)

        mockMvc.perform(MockMvcRequestBuilders.get("/audits")
                .param("limit", "20")
                .param("offset", "0")
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().string(VALID_GET_AUDIT_RESPONSE))

        verify(auditFacade).getAuditLogs(any(), any(), any(AuditRequestParams::class.java))

        verify(auditFacade, Mockito.times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.AUDIT_LOG_ENQUIRY)
    }

    @Test
    fun `should get Audit logs by id`() {
        val id = "5b11fb3a-b33a-4869-9cf8-4317285cefde"
        val participantDto = ParticipantDetailsDto(
                "NDEASESSXXX", "Nordea")
        val userDto = UserDetailsDto("12a521","Reuben Barnes")
        val result = AuditDetailsDto.builder()
                .serviceId("SEKB-22")
                .eventType("TRANSACTION_DETAILS")
                .product("BPS")
                .entity(participantDto)
                .user(userDto)
                .customer("P27-SEK")
                .requestDate(LocalDateTime.of(2021, Month.APRIL, 4,12, 0).atZone(UTC))
                .responseDate(LocalDateTime.of(2021, Month.APRIL, 4,13, 0).atZone(UTC))
                .request("20210115FORXSES1B2159")
                .response("OK")
                .build()
        `when`(auditFacade.getAuditDetails(any(), any(), eq(id))).thenReturn(result)

        mockMvc.perform(MockMvcRequestBuilders.get("/audits/$id")
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_AUDIT_BY_SERVICE_ID_RESPONSE, true))

        verify(auditFacade).getAuditDetails(any(), any(), eq(id))
        verify(auditFacade, Mockito.times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.AUDIT_LOG_EVENT_DETAILS)
    }

    @Test
    fun `should get Audit ARID logs by id`() {
        val id = "5b11fb3a-b33a-4869-9cf8-4317285cefde"
        val participantDto = ParticipantDetailsDto(
                "NDEASESSXXX", "Nordea")
        val userDto = UserDetailsDto("12a521","Reuben Barnes")
        val result = AuditDetailsDto.builder()
                .serviceId("SEKB-22")
                .eventType("REQ_BATCH_CANCELLATION")
                .product("BPS")
                .entity(participantDto)
                .user(userDto)
                .customer("P27-SEK")
                .requestDate(LocalDateTime.of(2021, Month.APRIL, 4,12, 0).atZone(UTC))
                .responseDate(LocalDateTime.of(2021, Month.APRIL, 4,13, 0).atZone(UTC))
                .request("20210115FORXSES1B2159")
                .approvalRequestId("15f62598-0a49-4754-8081-7f296573cb5d")
                .response("OK")
                .build()

        `when`(auditFacade.getAuditDetails(any(), any(), eq(id))).thenReturn(result)

        mockMvc.perform(MockMvcRequestBuilders.get("/audits/$id")
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_AUDIT_BY_SERVICE_ID_ARID_RESPONSE, true))

        verify(auditFacade).getAuditDetails(any(), any(), eq(id))
        verify(auditFacade, Mockito.times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.AUDIT_LOG_EVENT_DETAILS)
    }

    @Test
    fun `should get Audit events`() {
        val result: List<String> = listOf(TestConstants.CLIENT_TYPE)
        `when`(auditFacade.getEventTypes(any(), any(ClientType::class.java))).thenReturn(result)

        mockMvc.perform(MockMvcRequestBuilders.get("/reference/audit/events")
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().string(VALID_AUDIT_EVENTS_RESPONSE))

        verify(auditFacade).getEventTypes(any(), any(ClientType::class.java))
    }

    @Test
    fun `should get User audit details`() {
        val result: List<UserDetailsDto> = listOf(UserDetailsDto("e000000","John Doe"))
        `when`(auditFacade.getUserDetails(any(), any(), any())).thenReturn(result)

        mockMvc.perform(MockMvcRequestBuilders.get("/reference/audit/participants/HANDSESS/users")
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().string(VALID_USER_AUDIT_RESPONSE))

        verify(auditFacade).getUserDetails(any(), any(), any())
    }
}
