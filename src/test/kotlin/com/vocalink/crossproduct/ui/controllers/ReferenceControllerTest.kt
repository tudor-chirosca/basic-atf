package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto
import com.vocalink.crossproduct.ui.facade.ReferenceServiceFacade
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ReferenceController::class)
class ReferenceControllerTest {

    @Autowired
    private val mockMvc: MockMvc? = null

    @MockBean
    private val referenceServiceFacade: ReferenceServiceFacade? = null

    @Test
    fun `should get all participants`() {
        val participants = listOf(ParticipantReferenceDto.builder()
                .participantIdentifier("ESSESESS")
                .name("SEB Bank")
                .build(),
                ParticipantReferenceDto.builder()
                        .participantIdentifier("HANDSESS")
                        .name("Svenska Handelsbanken")
                        .build())

        Mockito.`when`(referenceServiceFacade!!.getParticipants(TestConstants.CONTEXT))
                .thenReturn(participants)
        mockMvc!!.perform(MockMvcRequestBuilders.get("/reference/participants")
                .header("context", TestConstants.CONTEXT))
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

        Mockito.`when`(referenceServiceFacade!!.getParticipants(TestConstants.CONTEXT))
                .thenReturn(participants)
        mockMvc!!.perform(MockMvcRequestBuilders.get("/reference/participants"))
                .andExpect(status().isBadRequest)
    }

}