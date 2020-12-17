package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.mocks.MockDashboardModels
import com.vocalink.crossproduct.ui.facade.SettlementServiceFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(SettlementController::class)
open class SettlementControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var settlementServiceFacade: SettlementServiceFacade

    @Test
    @Throws(Exception::class)
    fun `should get settlement for scheme operator`() {
        `when`(settlementServiceFacade.getSettlement(TestConstants.CONTEXT, ClientType.UI))
                .thenReturn(MockDashboardModels().getAllParticipantsSettlementDashboardDto())
        mockMvc.perform(MockMvcRequestBuilders.get("/settlement")
                .header("context", TestConstants.CONTEXT)
                .header("client-type", TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.previousCycle.id").value("01"))
                .andExpect(jsonPath("$.previousCycle.status").value("COMPLETED"))
                .andExpect(jsonPath("$.currentCycle.id").value("02"))
                .andExpect(jsonPath("$.currentCycle.status").value("OPEN"))
                .andExpect(jsonPath("$.positions[0].participant.id").value("NDEASESSXXX"))
                .andExpect(jsonPath("$.positions[0].participant.bic").value("NDEASESSXXX"))
                .andExpect(jsonPath("$.positions[0].participant.name").value("Nordea"))
                .andExpect(jsonPath("$.positions[0].participant.status").value("ACTIVE"))
                .andExpect(jsonPath("$.positions[0].currentPosition.credit").value("1"))
                .andExpect(jsonPath("$.positions[0].currentPosition.debit").value("10"))
                .andExpect(jsonPath("$.positions[0].currentPosition.netPosition").value("9"))
                .andExpect(jsonPath("$.positions[0].previousPosition.credit").value("10"))
                .andExpect(jsonPath("$.positions[0].previousPosition.debit").value("10"))
                .andExpect(jsonPath("$.positions[0].previousPosition.netPosition").value("0"))
                .andExpect(jsonPath("$.positions[1].participant.id").value("HANDSESS"))
                .andExpect(jsonPath("$.positions[1].participant.bic").value("HANDSESS"))
                .andExpect(jsonPath("$.positions[1].participant.name").value("Svenska Handelsbanken"))
                .andExpect(jsonPath("$.positions[1].participant.status").value("SUSPENDED"))
                .andExpect(jsonPath("$.positions[1].currentPosition.credit").value("10"))
                .andExpect(jsonPath("$.positions[1].currentPosition.debit").value("10"))
                .andExpect(jsonPath("$.positions[1].currentPosition.netPosition").value("0"))
                .andExpect(jsonPath("$.positions[1].previousPosition.credit").value("1"))
                .andExpect(jsonPath("$.positions[1].previousPosition.debit").value("10"))
                .andExpect(jsonPath("$.positions[1].previousPosition.netPosition").value("9"))
    }

    @Test
    @Throws(Exception::class)
    fun `should get self funding settlement details for given participant id`() {
        val participantId = "NDEASESSXXX"
        `when`(settlementServiceFacade.getParticipantSettlementDetails(TestConstants.CONTEXT, ClientType.UI, participantId))
                .thenReturn(MockDashboardModels().getSelfFundingDetailsDto())
        mockMvc.perform(MockMvcRequestBuilders.get("/settlementDetails/$participantId")
                .header("context", TestConstants.CONTEXT)
                .header("client-type", TestConstants.CLIENT_TYPE))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.previousCycle.id").value("01"))
                .andExpect(jsonPath("$.previousCycle.status").value("COMPLETED"))
                .andExpect(jsonPath("$.currentCycle.id").value("02"))
                .andExpect(jsonPath("$.currentCycle.status").value("OPEN"))
                .andExpect(jsonPath("$.participant.id").value("NDEASESSXXX"))
                .andExpect(jsonPath("$.participant.bic").value("NDEASESSXXX"))
                .andExpect(jsonPath("$.participant.name").value("Nordea"))
                .andExpect(jsonPath("$.participant.status").value("ACTIVE"))
                .andExpect(jsonPath("$.participant.participantType").value("DIRECT+ONLY"))
                .andExpect(jsonPath("$.previousPosition.customerCreditTransfer.credit").value("1"))
                .andExpect(jsonPath("$.previousPosition.customerCreditTransfer.debit").value("10"))
                .andExpect(jsonPath("$.previousPosition.customerCreditTransfer.netPosition").value("9"))
                .andExpect(jsonPath("$.currentPosition.customerCreditTransfer.credit").value("1"))
                .andExpect(jsonPath("$.currentPosition.customerCreditTransfer.debit").value("10"))
                .andExpect(jsonPath("$.currentPosition.customerCreditTransfer.netPosition").value("9"))
                .andExpect(jsonPath("$.previousPosition.paymentReturn.credit").value("10"))
                .andExpect(jsonPath("$.previousPosition.paymentReturn.debit").value("10"))
                .andExpect(jsonPath("$.previousPosition.paymentReturn.netPosition").value("0"))
                .andExpect(jsonPath("$.currentPosition.paymentReturn.credit").value("10"))
                .andExpect(jsonPath("$.currentPosition.paymentReturn.debit").value("10"))
                .andExpect(jsonPath("$.currentPosition.paymentReturn.netPosition").value("0"))
                .andExpect(jsonPath("$.previousPositionTotals.totalCredit").value("10"))
                .andExpect(jsonPath("$.previousPositionTotals.totalDebit").value("10"))
                .andExpect(jsonPath("$.previousPositionTotals.totalNetPosition").value("0"))
                .andExpect(jsonPath("$.currentPositionTotals.totalCredit").value("0"))
                .andExpect(jsonPath("$.currentPositionTotals.totalDebit").value("10"))
                .andExpect(jsonPath("$.currentPositionTotals.totalNetPosition").value("10"))
    }
}
