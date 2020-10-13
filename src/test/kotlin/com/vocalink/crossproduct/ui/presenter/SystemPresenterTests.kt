package com.vocalink.crossproduct.ui.presenter

import com.vocalink.crossproduct.mocks.MockParticipants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
class SystemPresenterTests {

    private val testingModule = SystemPresenter()

    @Test
    fun `should get System ClientType`() {
        val result = testingModule.clientType
        assertEquals(ClientType.SYSTEM, result)
    }

    @Test
    fun `should throw RuntimeException on present Settlement for System`() {
        Assertions.assertThrows(RuntimeException::class.java) {
            testingModule.presentSettlement(emptyList(), emptyList())
        }
    }

    @Test
    fun `should throw RuntimeException on present self funded settlement details for System`() {
        val participant = MockParticipants().getParticipant(true)
        Assertions.assertThrows(RuntimeException::class.java) {
            testingModule.presentParticipantSettlementDetails(emptyList(), emptyList(), participant, null, null)
        }
    }

    @Test
    fun `should throw RuntimeException on present input output for System`() {
        Assertions.assertThrows(RuntimeException::class.java) {
            testingModule.presentInputOutput(emptyList(), emptyList(), LocalDate.now())
        }
    }
}