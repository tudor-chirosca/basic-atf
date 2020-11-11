package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.mocks.MockIOData
import com.vocalink.crossproduct.ui.facade.InputOutputFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate

@WebMvcTest(InputOutputController::class)
class InputOutputControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var inputOutputFacade: InputOutputFacade

    @Test
    @Throws(Exception::class)
    fun `should get IO data`() {
        `when`(inputOutputFacade.getInputOutputDashboard(TestConstants.CONTEXT, ClientType.UI, LocalDate.now()))
                .thenReturn(MockIOData().ioDashboardDto)
        mockMvc.perform(MockMvcRequestBuilders.get("/io")
                .header("context", TestConstants.CONTEXT)
                .header("client-type", TestConstants.CLIENT_TYPE))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.filesRejected").value("2.00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.batchesRejected").value("2.00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionsRejected").value("2.00"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].participant.id").value("ESSESESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].participant.bic").value("ESSESESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].participant.name").value("SEB Bank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].participant.status").value("ACTIVE"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].files.submitted").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].files.rejected").value("1.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].batches.submitted").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].batches.rejected").value("1.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].transactions.submitted").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[0].transactions.rejected").value("1.0"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[1].participant.id").value("HANDSESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[1].participant.bic").value("HANDSESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[1].participant.name").value("Svenska Handelsbanken"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[1].participant.status").value("SUSPENDED"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[1].files.submitted").value("10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[1].files.rejected").value("1.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[1].batches.submitted").value("10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[1].batches.rejected").value("1.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[1].transactions.submitted").value("10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[1].transactions.rejected").value("1.0"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[2].participant.id").value("NDEASESSXXX"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[2].participant.bic").value("NDEASESSXXX"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[2].participant.name").value("Nordea"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[2].participant.status").value("ACTIVE"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[2].files.submitted").value("0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[2].files.rejected").value("0.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[2].batches.submitted").value("0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[2].batches.rejected").value("0.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[2].transactions.submitted").value("0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rows[2].transactions.rejected").value("0.0"))
    }

    @Test
    @Throws(Exception::class)
    fun `should get IO Details`() {
        val participantId = "NDEASESSXXX"
        `when`(inputOutputFacade
                .getInputOutputDetails(TestConstants.CONTEXT, ClientType.UI, LocalDate.now(), participantId))
                .thenReturn(MockIOData().getIODetailsDto())
        mockMvc.perform(MockMvcRequestBuilders.get("/io-details/" + participantId)
                .header("context", TestConstants.CONTEXT)
                .header("client-type", TestConstants.CLIENT_TYPE))
                .andExpect(MockMvcResultMatchers.status().isOk)

        verify(inputOutputFacade).getInputOutputDetails(any(), any(), any(), any())
    }
}
