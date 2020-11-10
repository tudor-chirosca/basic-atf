package com.vocalink.crossproduct.ui.presenter

import com.vocalink.crossproduct.domain.alert.Alert
import com.vocalink.crossproduct.domain.alert.AlertReferenceData
import com.vocalink.crossproduct.mocks.MockIOData
import com.vocalink.crossproduct.mocks.MockParticipants
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class SystemPresenterTests {

    private val testingModule = SystemPresenter()

    @Test
    fun `should get System ClientType`() {
        val result = testingModule.clientType
        assertEquals(ClientType.SYSTEM, result)
    }

    @Test
    fun `should throw RuntimeException on present All Participants Settlement for System`() {
        assertThrows(RuntimeException::class.java) {
            testingModule.presentAllParticipantsSettlement(emptyList(), emptyList())
        }
    }

    @Test
    fun `should throw RuntimeException on present Funding Participants Settlement for System`() {
        assertThrows(RuntimeException::class.java) {
            testingModule.presentFundingParticipantSettlement(emptyList(), emptyList(), null, emptyList())
        }
    }

    @Test
    fun `should throw RuntimeException on present self funded settlement details for System`() {
        val participant = MockParticipants().getParticipant(true)
        assertThrows(RuntimeException::class.java) {
            testingModule.presentParticipantSettlementDetails(emptyList(), emptyList(), participant, null, null)
        }
    }

    @Test
    fun `should throw RuntimeException on present input output for System`() {
        assertThrows(RuntimeException::class.java) {
            testingModule.presentInputOutput(emptyList(), emptyList(), LocalDate.now())
        }
    }

    @Test
    fun `should throw RuntimeException on present alert references for System`() {
        val alertRef = AlertReferenceData.builder().build()
        assertThrows(RuntimeException::class.java) {
            testingModule.presentAlertReference(alertRef)
        }
    }

    @Test
    fun `should throw RuntimeException on present input output Details for System`() {
        val participant = MockParticipants().getParticipant(false)
        val ioData = MockIOData().getIODetails()
        assertThrows(RuntimeException::class.java) {
            testingModule.presentIoDetails(participant, ioData, LocalDate.now())
        }
    }

    @Test
    fun `should throw RuntimeException on present participant references for System`() {
        assertThrows(RuntimeException::class.java) {
            testingModule.presentParticipantReferences(emptyList())
        }
    }

    @Test
    fun `should throw RuntimeException on present alerts for System`() {
        val alerts = listOf(Alert.builder().build())
        assertThrows(RuntimeException::class.java) {
            testingModule.presentAlert(alerts)
        }
    }
}