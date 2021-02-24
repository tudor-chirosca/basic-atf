package com.vocalink.crossproduct.ui.presenter

import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.alert.Alert
import com.vocalink.crossproduct.domain.alert.AlertReferenceData
import com.vocalink.crossproduct.domain.alert.AlertStats
import com.vocalink.crossproduct.domain.batch.Batch
import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.files.File
import com.vocalink.crossproduct.mocks.MockIOData
import com.vocalink.crossproduct.mocks.MockParticipants
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class SystemPresenterTests {

    private val systemPresenter = SystemPresenter()

    @Test
    fun `should get System ClientType`() {
        val result = systemPresenter.clientType
        assertEquals(ClientType.SYSTEM, result)
    }

    @Test
    fun `should throw RuntimeException on present All Participants Settlement for System`() {
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentAllParticipantsSettlement(emptyList(), emptyList())
        }
    }

    @Test
    fun `should throw RuntimeException on present Funding Participants Settlement for System`() {
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentFundingParticipantSettlement(emptyList(), emptyList(), null, emptyList())
        }
    }

    @Test
    fun `should throw RuntimeException on present self funded settlement details for System`() {
        val participant = MockParticipants().getParticipant(true)
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentParticipantSettlementDetails(emptyList(), emptyList(), participant)
        }
    }

    @Test
    fun `should throw RuntimeException on present input output for System`() {
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentInputOutput(emptyList(), emptyList(), LocalDate.now())
        }
    }

    @Test
    fun `should throw RuntimeException on present alert references for System`() {
        val alertRef = AlertReferenceData(null, null)
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentAlertReference(alertRef)
        }
    }

    @Test
    fun `should throw RuntimeException on present input output Details for System`() {
        val participant = MockParticipants().getParticipant(false)
        val ioData = MockIOData().getIODetails()
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentIoDetails(participant, ioData, LocalDate.now())
        }
    }

    @Test
    fun `should throw RuntimeException on present participant references for System`() {
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentParticipantReferences(emptyList())
        }
    }

    @Test
    fun `should throw RuntimeException on present present file references with type for System`() {
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentFileReferencesFor(emptyList(), "")
        }
    }

    @Test
    fun `should throw RuntimeException on present alerts for System`() {
        val alerts = Page<Alert>(0, emptyList())
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentAlert(alerts)
        }
    }

    @Test
    fun `should throw RuntimeException on present cycle date references for System`() {
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentCycleDateReferences(null)
        }
    }

    @Test
    fun `should throw RuntimeException on present files for System`() {
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentFiles(0, emptyList())
        }
    }

    @Test
    fun `should throw RuntimeException on present file details for System`() {
        val file = File.builder().build()
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentFileDetails(file, null, null)
        }
    }

    @Test
    fun `should throw RuntimeException on present batches for System`() {
        val batches = Page<Batch>(0, emptyList())
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentBatches(batches)
        }
    }

    @Test
    fun `should throw RuntimeException on present alert stats for System`() {
        val alertStats = AlertStats(0, null)
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentAlertStats(alertStats)
        }
    }

    @Test
    fun `should throw RuntimeException on present message direction refs for System`() {
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentMessageDirectionReferences(emptyList())
        }
    }

    @Test
    fun `should throw RuntimeException on present batch details for System`() {
        val batch = Batch.builder().build()
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentBatchDetails(batch)
        }
    }

    @Test
    fun `should throw RuntimeException on present transactions for System`() {
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentTransactions(null)
        }
    }

    @Test
    fun `should throw RuntimeException on present reports for System`() {
        assertThrows(RuntimeException::class.java) {
            systemPresenter.presentReports(null)
        }
    }
}