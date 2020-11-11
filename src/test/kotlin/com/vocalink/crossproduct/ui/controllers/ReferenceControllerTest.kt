package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto
import com.vocalink.crossproduct.ui.facade.ReferencesServiceFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ReferenceController::class)
class ReferenceControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var referencesServiceFacade: ReferencesServiceFacade

    @Test
    fun `should get all participant references`() {
        val participants = listOf(ParticipantReferenceDto.builder()
                .participantIdentifier("ESSESESS")
                .name("SEB Bank")
                .build(),
                ParticipantReferenceDto.builder()
                        .participantIdentifier("HANDSESS")
                        .name("Svenska Handelsbanken")
                        .build())

        `when`(referencesServiceFacade.getParticipantReferences(TestConstants.CONTEXT, ClientType.UI))
                .thenReturn(participants)
        mockMvc.perform(get("/reference/participants")
                .header("context", TestConstants.CONTEXT)
                .header("client-type", TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
    }

    @Test
    fun `should get bad request on missing context for reference participants`() {
        val participants = listOf(ParticipantReferenceDto.builder()
                .participantIdentifier("ESSESESS")
                .name("SEB Bank")
                .build(),
                ParticipantReferenceDto.builder()
                        .participantIdentifier("HANDSESS")
                        .name("Svenska Handelsbanken")
                        .build())

        `when`(referencesServiceFacade.getParticipantReferences(TestConstants.CONTEXT, ClientType.UI))
                .thenReturn(participants)
        mockMvc.perform(get("/reference/participants"))
                .andExpect(status().is5xxServerError)
    }

    @Test
    fun `should get all message direction references`() {
        val sending = "Sending"
        val receiving = "Receiving"
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
        `when`(referencesServiceFacade.getMessageDirectionReferences(TestConstants.CONTEXT, ClientType.UI))
                .thenReturn(messages)
        mockMvc.perform(get("/reference/messages")
                .header("context", TestConstants.CONTEXT)
                .header("client-type", TestConstants.CLIENT_TYPE))
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

        `when`(referencesServiceFacade.getMessageDirectionReferences(TestConstants.CONTEXT, ClientType.UI))
                .thenReturn(messages)
        mockMvc.perform(get("/reference/messages"))
                .andExpect(status().is5xxServerError)
    }

}