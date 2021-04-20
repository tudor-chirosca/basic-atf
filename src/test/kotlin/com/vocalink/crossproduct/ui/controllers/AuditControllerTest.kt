package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.ui.dto.audit.AuditDetailsDto
import com.vocalink.crossproduct.ui.dto.audit.AuditDto
import com.vocalink.crossproduct.ui.dto.audit.AuditRequestParams
import com.vocalink.crossproduct.ui.dto.audit.UserDetailsDto
import com.vocalink.crossproduct.ui.facade.api.AuditFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Clock.fixed
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC

@WebMvcTest(AuditController::class)
@ContextConfiguration(classes = [TestConfig::class])
class AuditControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var auditFacade: AuditFacade

    private companion object {
        const val VALID_AUDIT_EVENTS_RESPONSE = """["UI"]"""
        const val VALID_USER_AUDIT_RESPONSE = """[{"userName":"John Doe"}]"""
        const val VALID_AUDIT_BY_SERVICE_ID_RESPONSE: String = """
        {
            "id":"20210111002",
            "eventType":"AUDIT_LOG_ENQUIRY",
            "operationType":"REQUEST",
            "timestamp":"2021-04-21T22:48:56Z",
            "entityId":"HANDSESS",
            "submitter":"194869924",
            "submitterOrganisation":"Svenska Handelsbanken",
            "interactionDetails":null
        }"""

        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"
        const val PARTICIPANT_ID_PARAMETER = "participantId"

        const val VALID_GET_AUDIT_RESPONSE = """{"totalResults":0,"items":null}"""
        val clock = fixed(LocalDateTime.of(2021, 4, 21, 22, 48, 56).toInstant(UTC), UTC)!!
    }

    @Test
    fun `should get Audit logs`() {
        val result: Page<AuditDto> = Page(0, null)
        `when`(auditFacade.getAuditLogs(any(), any(), any(AuditRequestParams::class.java))).thenReturn(result)

        mockMvc.perform(MockMvcRequestBuilders.get("/audits")
                .param("limit", "20")
                .param("offset", "0")
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().string(VALID_GET_AUDIT_RESPONSE))

        verify(auditFacade).getAuditLogs(any(), any(), any(AuditRequestParams::class.java))
    }

    @Test
    fun `should get Audit logs by service Id`() {
        val serviceId = "1000"
        val result = AuditDetailsDto.builder()
                .id("20210111002")
                .entityId("HANDSESS")
                .eventType("AUDIT_LOG_ENQUIRY")
                .operationType("REQUEST")
                .submitter("194869924")
                .submitterOrganisation("Svenska Handelsbanken")
                .timestamp(LocalDateTime.now(clock).atZone(UTC))
                .build()
        `when`(auditFacade.getAuditDetails(any(), any(), eq(serviceId))).thenReturn(result)

        mockMvc.perform(MockMvcRequestBuilders.get("/audits/$serviceId")
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_AUDIT_BY_SERVICE_ID_RESPONSE, true))

        verify(auditFacade).getAuditDetails(any(), any(), eq(serviceId))
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
        val result: List<UserDetailsDto> = listOf(UserDetailsDto("John Doe"))
        `when`(auditFacade.getUserDetails(any(), any(), any())).thenReturn(result)

        mockMvc.perform(MockMvcRequestBuilders.get("/reference/audit/users")
                .param(PARTICIPANT_ID_PARAMETER, "HANDSESS")
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().string(VALID_USER_AUDIT_RESPONSE))

        verify(auditFacade).getUserDetails(any(), any(), any())
    }

}
