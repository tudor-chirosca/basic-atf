package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants.CLIENT_TYPE
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.ui.dto.cycle.DayCycleDto
import com.vocalink.crossproduct.ui.dto.reference.ReasonCodeReferenceDto
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto
import com.vocalink.crossproduct.ui.facade.api.ReferencesServiceFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ReferenceController::class)
@ContextConfiguration(classes=[TestConfig::class])
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

        const val VALID_RESPONSE_WITH_PARTICIPANT_TYPE = """
       [{
        "participantIdentifier": "ESSESESS",
        "name": "SEB Bank",
        "participantType": "DIRECT",
        "status": "ACTIVE",
        "schemeCode": "P27"
        },
        {
        "participantIdentifier": "HANDSESS",
        "name": "Svenska Handelsbanken",
        "participantType": "DIRECT",
        "status": "ACTIVE",
        "schemeCode": "P27"
        }]
        """

        const val VALID_RESPONSE_WITH_CON_PARTICIPANT = """
       [{
        "participantIdentifier": "ESSESESS",
        "name": "SEB Bank",
        "participantType": "DIRECT",
        "status": "ACTIVE",
        "connectingParticipantId": "any id",
        "schemeCode": null
        },
        {
        "participantIdentifier": "HANDSESS",
        "name": "Svenska Handelsbanken",
        "participantType": "DIRECT",
        "status": "ACTIVE",
        "connectingParticipantId": "any id_2",
        "schemeCode": null
        }]
        """

        const val VALID_RESPONSE_WITH_SCHEME_CODE = """
       [{
        "participantIdentifier": "ESSESESS",
        "name": "SEB Bank",
        "participantType": "DIRECT",
        "status": "ACTIVE",
        "connectingParticipantId": "any id",
        "schemeCode": "P27"
        },
        {
        "participantIdentifier": "HANDSESS",
        "name": "Svenska Handelsbanken",
        "participantType": "DIRECT",
        "status": "ACTIVE",
        "connectingParticipantId": "any id_2",
        "schemeCode": "P27"
        }]
        """

        const val VALID_CYCLE_DAY_RESPONSE_WITH_SCHEME_CODE = """
       [{
        "id": "20201103003",
        "sessionCode": "03",
        "status": "OPEN"
        }]
        """
    }

    @Test
    fun `should get all participant references`() {
        val directOnly = ParticipantType.DIRECT
        val active = ParticipantStatus.ACTIVE
        val participants = listOf(
                ParticipantReferenceDto("ESSESESS", "SEB Bank", directOnly, active, "P27"),
                ParticipantReferenceDto("HANDSESS", "Svenska Handelsbanken", directOnly, active, "P27")
        )

        `when`(referencesServiceFacade.getParticipantReferences(CONTEXT, ClientType.UI))
                .thenReturn(participants)
        mockMvc.perform(get("/reference/participants")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_RESPONSE_WITH_PARTICIPANT_TYPE, true))
    }

    @Test
    fun `should get all participant references with connectingParticipantId`() {
        val directOnly = ParticipantType.DIRECT
        val active = ParticipantStatus.ACTIVE
        val participantReferenceDto = ParticipantReferenceDto("ESSESESS", "SEB Bank", directOnly, active, null)
        val participantReferenceDto2 = ParticipantReferenceDto("HANDSESS", "Svenska Handelsbanken", directOnly, active, null)

        participantReferenceDto.connectingParticipantId="any id"
        participantReferenceDto2.connectingParticipantId="any id_2"

        val participants = listOf(participantReferenceDto, participantReferenceDto2)

        `when`(referencesServiceFacade.getParticipantReferences(CONTEXT, ClientType.UI))
                .thenReturn(participants)
        mockMvc.perform(get("/reference/participants")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_RESPONSE_WITH_CON_PARTICIPANT, true))
    }

    @Test
    fun `should get all participant references with schemeCode`() {
        val directOnly = ParticipantType.DIRECT
        val active = ParticipantStatus.ACTIVE
        val participantReferenceDto = ParticipantReferenceDto("ESSESESS", "SEB Bank", directOnly, active, "P27")
        val participantReferenceDto2 = ParticipantReferenceDto("HANDSESS", "Svenska Handelsbanken", directOnly, active, "P27")

        participantReferenceDto.connectingParticipantId="any id"
        participantReferenceDto2.connectingParticipantId="any id_2"

        val participants = listOf(participantReferenceDto, participantReferenceDto2)

        `when`(referencesServiceFacade.getParticipantReferences(CONTEXT, ClientType.UI))
                .thenReturn(participants)
        mockMvc.perform(get("/reference/participants")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_RESPONSE_WITH_SCHEME_CODE, true))

    }

    @Test
    fun `should get bad request on missing context for reference participants`() {
        val directOnly = ParticipantType.DIRECT
        val active = ParticipantStatus.ACTIVE
        val participants = listOf(
                ParticipantReferenceDto("ESSESESS", "SEB Bank", directOnly, active, "P27"),
                ParticipantReferenceDto("HANDSESS", "Svenska Handelsbanken", directOnly, active,"P27")
        )

        `when`(referencesServiceFacade.getParticipantReferences(CONTEXT, ClientType.UI))
                .thenReturn(participants)
        mockMvc.perform(get("/reference/participants"))
                .andExpect(status().is4xxClientError)
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
                .andExpect(status().is4xxClientError)
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
        val fileStatusesTypeDto = ReasonCodeReferenceDto("Rejected", true, listOf("F01", "F02"), "FILES")

        `when`(referencesServiceFacade.getReasonCodeReferences(anyString(), any(), anyString()))
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
        val stringDate = "2020-11-03T00:00:00Z"
        mockMvc.perform(get("/reference/cycles")
                .param(REQUIRED_DATE_PARAM, stringDate)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)
    }

    @Test
    fun `should get 200 for find cycles by date and return valid response`() {
        val date = ZonedDateTime.of(LocalDate.of(2021, 11, 3), LocalTime.MIN, ZoneId.of("UTC"))

        val cycles = listOf(
                DayCycleDto(
                        "20201103003",
                        "03",
                        CycleStatus.OPEN
                ))

        `when`(referencesServiceFacade.getDayCyclesByDate(CONTEXT, ClientType.UI, date, false))
                .thenReturn(cycles)

        mockMvc.perform(get("/reference/cycles")
                .param(REQUIRED_DATE_PARAM, date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_CYCLE_DAY_RESPONSE_WITH_SCHEME_CODE))
    }

    @Test
    fun `should fail cycle day request if missing required parameter day`() {
        mockMvc.perform(get("/reference/cycles")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().is5xxServerError)
    }
}
