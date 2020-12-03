package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants.CLIENT_TYPE
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto
import com.vocalink.crossproduct.ui.dto.reference.FileStatusesTypeDto
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto
import com.vocalink.crossproduct.ui.facade.ReferencesServiceFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@WebMvcTest(ReferenceController::class)
class ReferenceControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var referencesServiceFacade: ReferencesServiceFacade

    companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"
        const val REQUIRED_TYPE_PARAM = "type"
        const val REQUIRED_DATE_PARAM = "day"

        const val VALID_REF_RESPONSE = """[{
        "status": "Rejected",
        "hasReason": true,
        "reasonCodes": [
            "F01",
            "F02"
        ],
        "enquiryType": "FILES"
    }]"""
    }

    @Test
    fun `should get all participant references`() {
        val participants = listOf(
                ParticipantReferenceDto("ESSESESS", "SEB Bank"),
                ParticipantReferenceDto("HANDSESS", "Svenska Handelsbanken")
        )

        `when`(referencesServiceFacade.getParticipantReferences(CONTEXT, ClientType.UI))
                .thenReturn(participants)
        mockMvc.perform(get("/reference/participants")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)
    }

    @Test
    fun `should get bad request on missing context for reference participants`() {
        val participants = listOf(
                ParticipantReferenceDto("ESSESESS", "SEB Bank"),
                ParticipantReferenceDto("HANDSESS", "Svenska Handelsbanken")
        )

        `when`(referencesServiceFacade.getParticipantReferences(CONTEXT, ClientType.UI))
                .thenReturn(participants)
        mockMvc.perform(get("/reference/participants"))
                .andExpect(status().is5xxServerError)
    }

    @Test
    fun `should get all message direction references`() {
        val sending = "sending"
        val receiving = "receiving"
        val type = "some_type"
        val messages = listOf(
                MessageDirectionReferenceDto.builder()
                        .name(sending)
                        .isDefault(true)
                        .types(listOf(type))
                        .build(),
                MessageDirectionReferenceDto.builder()
                        .name(receiving)
                        .isDefault(false)
                        .types(listOf(type))
                        .build()
        )
        `when`(referencesServiceFacade.getMessageDirectionReferences(CONTEXT, ClientType.UI))
                .thenReturn(messages)
        mockMvc.perform(get("/reference/messages")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().string(containsString(sending)))
                .andExpect(content().string(containsString(receiving)))
                .andExpect(content().string(containsString(type)))
    }

    @Test
    fun `should get bad request on missing context for message direction references`() {
        val messages = listOf(
                MessageDirectionReferenceDto.builder().build(),
                MessageDirectionReferenceDto.builder().build())

        `when`(referencesServiceFacade.getMessageDirectionReferences(CONTEXT, ClientType.UI))
                .thenReturn(messages)
        mockMvc.perform(get("/reference/messages"))
                .andExpect(status().is5xxServerError)
    }

    @Test
    fun `should return 200 for enquiry statuses`() {
        mockMvc.perform(get("/reference/enquiry-statuses")
                .param(REQUIRED_TYPE_PARAM, "files")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)
    }

    @Test
    fun `should return 200 for enquiry statuses and return valid response`() {
        val fileStatusesTypeDto = FileStatusesTypeDto("Rejected", true, listOf("F01", "F02"), "FILES")

        `when`(referencesServiceFacade.getFileReferences(anyString(), any(), anyString()))
                .thenReturn(listOf(fileStatusesTypeDto))

        mockMvc.perform(get("/reference/enquiry-statuses")
                .param(REQUIRED_TYPE_PARAM, "files")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_REF_RESPONSE))
    }

    @Test
    fun `should fail if missing required parameter files`() {
        mockMvc.perform(get("/reference/enquiry-statuses")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().is5xxServerError)
    }

    @Test
    fun `should get 200 for find cycles by date`() {
        val stringDate = "2020-11-03"
        mockMvc.perform(get("/reference/cycles")
                .param(REQUIRED_DATE_PARAM, stringDate)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)
    }

    @Test
    fun `should get 200 for find cycles by date and return valid response`() {
        val stringDate = "2020-11-03"
        val date = LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE)

        val cycles = listOf(
                CycleDto.builder()
                        .id("03")
                        .settlementTime(LocalDateTime.parse("2020-11-03T17:58:19.183"))
                        .cutOffTime(LocalDateTime.parse("2020-11-03T17:58:19.183"))
                        .status(CycleStatus.OPEN)
                        .build())

        `when`(referencesServiceFacade.getCyclesByDate(CONTEXT, ClientType.UI, date))
                .thenReturn(cycles)

        mockMvc.perform(get("/reference/cycles")
                .param(REQUIRED_DATE_PARAM, stringDate)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)
    }

    @Test
    fun `should fail if missing required parameter day`() {
        mockMvc.perform(get("/reference/cycles")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().is5xxServerError)
    }
}
