package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.ui.aspects.EventType
import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastDto
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto
import com.vocalink.crossproduct.ui.facade.api.BroadcastsFacade
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class BroadcastsControllerTest: ControllerTest() {

    @MockBean
    private lateinit var facade: BroadcastsFacade

    companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val MIN_EXPECTED_RESPONSE = """
        {
        "totalResults": 0,
        "items": []
        }
        """
        const val EXPECTED_RESPONSE = """
        {
        "totalResults": 1,
        "items": [
            {
                "createdAt": "2021-01-25T00:00:00Z",
                "broadcastId": "123",
                "message": "msg",
                "recipients": [
                    {
                        "participantIdentifier": "123",
                        "name": "name",
                        "participantType": "DIRECT",
                        "status": "ACTIVE",
                        "connectingParticipantId": "id",
                        "schemeCode": "p27"
                    }
                ]
            }
        ]
        }
        """

        var NEW_MESSAGE_REQUEST = """
            {
            "message": "Sample message",
            "recipients": ["NDEASESSXXX"]
            }
        """

        var NEW_BROADCAST_RESPONSE = """
            {
               "createdAt":"2021-01-25T00:00:00Z",
               "broadcastId":"00000100",
               "message":"Sample message",
               "recipients":[
                  {
                     "participantIdentifier":"123",
                     "name":"name",
                     "participantType":"DIRECT",
                     "status": "ACTIVE",
                     "schemeCode":"p27",
                     "connectingParticipantId":"id"
                  }
               ]
            }
        """
    }

    @Test
    fun `should have success on get broadcasts`() {
        `when`(facade.getPaginated(any(), any(), any())).thenReturn(PageDto(0, emptyList<BroadcastDto>()))

        mockMvc.perform(get("/broadcasts")
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(MIN_EXPECTED_RESPONSE, true))

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.VIEW_SENT_BROADCAST)
    }

    @Test
    fun `should have success on get broadcasts with query params`() {
        val queryParams = LinkedMultiValueMap(mapOf(Pair("offset", listOf("0")),
                Pair("limit", listOf("10")),
                Pair("date_From", listOf("2021-01-25T00:00:00Z")),
                Pair("date_To", listOf("2021-01-26T00:00:00Z")),
                Pair("sort", listOf("any")),
                Pair("msg", listOf("any")),
                Pair("recipient", listOf("any"))
        ))
        val ref = ParticipantReferenceDto("123", "name", ParticipantType.DIRECT, ParticipantStatus.ACTIVE,"p27")
        ref.connectingParticipantId = "id"
        val broadcastDto = BroadcastDto(ZonedDateTime.parse("2021-01-25T00:00:00Z"), "123", "msg", listOf(ref))

        val response = PageDto(1, listOf(broadcastDto))

        `when`(facade.getPaginated(any(), any(), any())).thenReturn(response)

        mockMvc.perform(get("/broadcasts")
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .queryParams(queryParams))
                .andExpect(status().isOk)
                .andExpect(content().json(EXPECTED_RESPONSE, true))

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.VIEW_SENT_BROADCAST)
    }

    @Test
    fun `should return newly created broadcast`() {

        val ref = ParticipantReferenceDto("123", "name", ParticipantType.DIRECT, ParticipantStatus.ACTIVE,"p27")
        ref.connectingParticipantId = "id"
        val broadcastDto = BroadcastDto(ZonedDateTime.parse("2021-01-25T00:00:00Z"), "00000100", "Sample message", listOf(ref))

        `when`(facade.create(any(), any(), any(), any())).thenReturn(broadcastDto)

        mockMvc.perform(post("/broadcasts")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(NEW_MESSAGE_REQUEST))
                .andExpect(status().isOk)
                .andExpect(content().json(NEW_BROADCAST_RESPONSE, true))

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.SEND_BROADCAST)
    }

    @Test
    fun `should fail with 400 when dateFrom is earlier than DAYS_LIMIT from today`() {
        val queryParams = LinkedMultiValueMap(
            mapOf(
                Pair("offset", listOf("0")),
                Pair("limit", listOf("10")),
                Pair("date_from", listOf(
                    ZonedDateTime.now(ZoneId.of("UTC"))
                        .minusDays((getDefault(DtoProperties.DAYS_LIMIT).toLong()) + 1)
                            .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))))
        )
        mockMvc.perform(
                get("/broadcasts")
                        .contentType(UTF8_CONTENT_TYPE)
                        .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                        .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                        .queryParams(queryParams))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("date_from can not be earlier than DAYS_LIMIT")))

        verifyNoInteractions(auditFacade)
    }

}
