package com.vocalink.crossproduct.ui.presenter

import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.alert.Alert
import com.vocalink.crossproduct.domain.alert.AlertPriorityData
import com.vocalink.crossproduct.domain.alert.AlertPriorityType
import com.vocalink.crossproduct.domain.alert.AlertReferenceData
import com.vocalink.crossproduct.domain.alert.AlertStats
import com.vocalink.crossproduct.domain.alert.AlertStatsData
import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.files.FileReference
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.position.ParticipantPosition
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference
import com.vocalink.crossproduct.domain.reference.ParticipantReference
import com.vocalink.crossproduct.mocks.MockCycles
import com.vocalink.crossproduct.mocks.MockDashboardModels
import com.vocalink.crossproduct.mocks.MockIOData
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.mocks.MockPositions
import com.vocalink.crossproduct.ui.dto.alert.AlertDto
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper
import com.vocalink.crossproduct.ui.presenter.mapper.SelfFundingSettlementDetailsMapper
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [UIPresenter::class])
@TestPropertySource("classpath:application.properties")
class UIPresenterTest {

    @Autowired
    private lateinit var uiPresenter: UIPresenter

    @MockBean
    private lateinit var selfFundingDetailsMapper: SelfFundingSettlementDetailsMapper

    @Test
    fun `should get All participants Settlement Dashboard DTO`() {
        val cycles = MockCycles().cyclesWithPositions
        val participants = MockParticipants().participants

        val result = uiPresenter.presentAllParticipantsSettlement(cycles, participants)

        assertEquals(3, result.positions.size)
        assertEquals("02", result.currentCycle.id)
        assertEquals(CycleStatus.OPEN, result.currentCycle.status)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)

        assertEquals("HANDSESS", result.positions[0].participant.id)
        assertEquals("HANDSESS", result.positions[0].participant.bic)
        assertEquals("Svenska Handelsbanken", result.positions[0].participant.name)
        assertEquals(ParticipantStatus.SUSPENDED, result.positions[0].participant.status)
        assertNotNull(result.positions[0].participant.suspendedTime)

        assertEquals(BigDecimal.TEN, result.positions[0].currentPosition.credit)
        assertEquals(BigDecimal.TEN, result.positions[0].currentPosition.debit)
        assertEquals(BigDecimal.ZERO, result.positions[0].currentPosition.netPosition)
        assertEquals(BigDecimal.TEN, result.positions[0].previousPosition.credit)
        assertEquals(BigDecimal.TEN, result.positions[0].previousPosition.debit)
        assertEquals(BigDecimal.ZERO, result.positions[0].previousPosition.netPosition)

        assertEquals("NDEASESSXXX", result.positions[1].participant.id)
        assertEquals("NDEASESSXXX", result.positions[1].participant.bic)
        assertEquals("Nordea", result.positions[1].participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.positions[1].participant.status)
        assertNull(result.positions[1].participant.suspendedTime)

        assertEquals(BigDecimal.ONE, result.positions[1].currentPosition.credit)
        assertEquals(BigDecimal.TEN, result.positions[1].currentPosition.debit)
        assertEquals(BigDecimal.valueOf(9), result.positions[1].currentPosition.netPosition)
        assertEquals(BigDecimal.ONE, result.positions[1].previousPosition.credit)
        assertEquals(BigDecimal.TEN, result.positions[1].previousPosition.debit)
        assertEquals(BigDecimal.valueOf(9), result.positions[1].previousPosition.netPosition)
    }

    @Test
    fun `should get Settlement Dashboard DTO for paramId with null values if missing IntraDay or Positions`() {
        val cycles = MockCycles().cycles
        val participants = MockParticipants().participants
        val fundingParticipant = MockParticipants().getParticipant(false)

        val result = uiPresenter.presentFundingParticipantSettlement(cycles, participants, fundingParticipant, emptyList())
        assertNotNull(result.fundingParticipant)
        assertEquals("NDEASESSXXX", result.fundingParticipant.bic)
        assertNotNull(result.intraDayPositionTotals)

        assertNotNull(result.intraDayPositionTotals)
        assertNotNull(result.currentPositionTotals)
        assertNotNull(result.previousPositionTotals)

        assertNull(result.positions[0].currentPosition.credit)
        assertNull(result.positions[0].currentPosition.debit)
        assertNull(result.positions[0].previousPosition.credit)
        assertNull(result.positions[0].previousPosition.debit)
        assertNull(result.positions[0].intraDayPositionGross.debitCap)
        assertNull(result.positions[0].intraDayPositionGross.debitPosition)
    }

    @Test
    fun `should calculate totals for debit and credit`() {
        val fundingParticipant = Participant.builder()
                .id("funding")
                .bic("funding")
                .build()

        val currentCredit = BigDecimal.TEN
        val currentDebit = BigDecimal.valueOf(7)
        val currentNet = BigDecimal.valueOf(3)

        val previousCredit = BigDecimal.valueOf(20)
        val previousDebit = BigDecimal.ONE
        val previousNet = BigDecimal.valueOf(19)

        val currentPositions = listOf(
                ParticipantPosition.builder()
                        .participantId("funding")
                        .credit(currentCredit)
                        .debit(currentDebit)
                        .netPosition(currentNet)
                        .build(),
                ParticipantPosition.builder()
                        .participantId("funded_one")
                        .credit(currentCredit)
                        .debit(currentDebit)
                        .netPosition(currentNet)
                        .build(),
                ParticipantPosition.builder()
                        .participantId("funded-two")
                        .credit(currentCredit)
                        .debit(currentDebit)
                        .netPosition(currentNet)
                        .build()
        )

        val previousPositions = listOf(
                ParticipantPosition.builder()
                        .participantId("funding")
                        .credit(previousCredit)
                        .debit(previousDebit)
                        .netPosition(previousNet)
                        .build(),
                ParticipantPosition.builder()
                        .participantId("funded_one")
                        .credit(previousCredit)
                        .debit(previousDebit)
                        .netPosition(previousNet)
                        .build(),
                ParticipantPosition.builder()
                        .participantId("funded-two")
                        .credit(previousCredit)
                        .debit(previousDebit)
                        .netPosition(previousNet)
                        .build()
        )

        val cycles = listOf(
                Cycle.builder()
                        .id("01")
                        .status(CycleStatus.COMPLETED)
                        .totalPositions(previousPositions)
                        .build(),
                Cycle.builder()
                        .id("02")
                        .status(CycleStatus.OPEN)
                        .totalPositions(currentPositions)
                        .build()
        )

        val participants = listOf(
                fundingParticipant,
                Participant.builder()
                        .id("funded_one")
                        .bic("funded_one")
                        .fundingBic("funding")
                        .build(),
                Participant.builder()
                        .id("funded_two")
                        .bic("funded_two")
                        .fundingBic("funding")
                        .build()
        )

        val result = uiPresenter.presentFundingParticipantSettlement(cycles, participants,
                fundingParticipant, emptyList())

        val currentCreditSum = currentCredit.add(currentCredit)
        val currentDebitSum = currentDebit.add(currentDebit)

        assertEquals(currentDebitSum, result.currentPositionTotals.totalDebit)
        assertEquals(currentCreditSum, result.currentPositionTotals.totalCredit)

        val previousCreditSum = previousCredit.add(previousCredit)
        val previousDebitSum = previousDebit.add(previousDebit)

        assertEquals(previousDebitSum, result.previousPositionTotals.totalDebit)
        assertEquals(previousCreditSum, result.previousPositionTotals.totalCredit)
    }

    @Test
    fun `should get Fun ding Participant Settlement Dashboard DTO`() {
        val cycles = MockCycles().cyclesWithPositions
        val participants = MockParticipants().participants
        val fundingParticipant = MockParticipants().getParticipant(false)
        val intraDayPositionsGross = MockPositions().getIntraDaysFor(listOf("NDEASESSXXX", "HANDSESS", "ESSESESS"))

        val result = uiPresenter.presentFundingParticipantSettlement(cycles, participants, fundingParticipant, intraDayPositionsGross)
        assertNotNull(result.fundingParticipant)
        assertEquals("NDEASESSXXX", result.fundingParticipant.bic)
        assertNotNull(result.intraDayPositionTotals)

        assertNotNull(result.intraDayPositionTotals)
        assertNotNull(result.currentPositionTotals)
        assertNotNull(result.previousPositionTotals)

        assertEquals(BigDecimal.valueOf(30), result.intraDayPositionTotals.totalDebitCap)
        assertEquals(BigDecimal.valueOf(3), result.intraDayPositionTotals.totalDebitPosition)
        assertEquals(BigDecimal.valueOf(11), result.currentPositionTotals.totalCredit)
        assertEquals(BigDecimal.valueOf(21), result.currentPositionTotals.totalDebit)
        assertEquals(BigDecimal.valueOf(11), result.previousPositionTotals.totalCredit)
        assertEquals(BigDecimal.valueOf(21), result.previousPositionTotals.totalDebit)
    }

    @Test
    fun `should get Self Funding Settlement Details DTO for 2 cycles`() {
        val cycles = MockCycles().cycles
        val positionDetails = MockPositions().positionDetails
        val participant = MockParticipants().getParticipant(true)

        `when`(selfFundingDetailsMapper
                .presentFullParticipantSettlementDetails(any(), any(), any(), any(), any()))
                .thenReturn(MockDashboardModels().getSelfFundingDetailsDto())

        val result = uiPresenter.presentParticipantSettlementDetails(cycles,
                positionDetails, participant, null, null)
        assertEquals("02", result.currentCycle.id)
        assertEquals(CycleStatus.OPEN, result.currentCycle.status)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)
        assertEquals(BigDecimal.ZERO, result.currentPositionTotals.totalCredit)
        assertEquals(BigDecimal.TEN, result.currentPositionTotals.totalDebit)
        assertEquals(BigDecimal.TEN, result.currentPositionTotals.totalNetPosition)
        assertEquals(BigDecimal.TEN, result.previousPositionTotals.totalCredit)
        assertEquals(BigDecimal.TEN, result.previousPositionTotals.totalDebit)
        assertEquals(BigDecimal.ZERO, result.previousPositionTotals.totalNetPosition)
        assertEquals(BigDecimal.TEN, result.currentPosition.customerCreditTransfer.debit)
        assertEquals(BigDecimal.ONE, result.currentPosition.customerCreditTransfer.credit)
        assertEquals(BigDecimal.valueOf(9), result.currentPosition.customerCreditTransfer.netPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.customerCreditTransfer.debit)
        assertEquals(BigDecimal.ONE, result.previousPosition.customerCreditTransfer.credit)
        assertEquals(BigDecimal.valueOf(9), result.previousPosition.customerCreditTransfer.netPosition)
        assertEquals(BigDecimal.TEN, result.currentPosition.paymentReturn.debit)
        assertEquals(BigDecimal.TEN, result.currentPosition.paymentReturn.credit)
        assertEquals(BigDecimal.ZERO, result.currentPosition.paymentReturn.netPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.debit)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.credit)
        assertEquals(BigDecimal.ZERO, result.previousPosition.paymentReturn.netPosition)
        assertEquals("NDEASESSXXX", result.participant.bic)
        assertEquals("NDEASESSXXX", result.participant.id)
        assertEquals("Nordea", result.participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.participant.status)
        assertNull(result.participant.suspendedTime)

        verify(selfFundingDetailsMapper).presentFullParticipantSettlementDetails(any(), any(), any(), any(), any())
    }

    @Test
    fun `should get Self Funding Settlement Details DTO for 1 cycle`() {
        val cycles = listOf(
                Cycle.builder()
                        .cutOffTime(ZonedDateTime.of(2019, 12, 10, 10, 10, 0, 0, ZoneId.of("UTC")))
                        .settlementTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                        .id("01")
                        .status(CycleStatus.COMPLETED)
                        .build())
        val positionDetails = MockPositions().positionDetails
        val participant = MockParticipants().getParticipant(true)

        `when`(selfFundingDetailsMapper
                .presentOneCycleParticipantSettlementDetails(any(), any(), any(), any(), any()))
                .thenReturn(MockDashboardModels().getSelfFundingDetailsDtoForOneCycle())

        val result = uiPresenter.presentParticipantSettlementDetails(cycles, positionDetails, participant,
                null, null)
        assertNull(result.currentCycle)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)
        assertNull(result.currentPositionTotals)

        assertEquals(BigDecimal.TEN, result.previousPositionTotals.totalCredit)
        assertEquals(BigDecimal.TEN, result.previousPositionTotals.totalDebit)
        assertEquals(BigDecimal.ZERO, result.previousPositionTotals.totalNetPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.customerCreditTransfer.debit)
        assertEquals(BigDecimal.ONE, result.previousPosition.customerCreditTransfer.credit)
        assertEquals(BigDecimal.valueOf(9), result.previousPosition.customerCreditTransfer.netPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.debit)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.credit)
        assertEquals(BigDecimal.ZERO, result.previousPosition.paymentReturn.netPosition)

        assertEquals("NDEASESSXXX", result.participant.bic)
        assertEquals("NDEASESSXXX", result.participant.id)
        assertEquals("Nordea", result.participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.participant.status)
        assertNull(result.participant.suspendedTime)

        verify(selfFundingDetailsMapper).presentOneCycleParticipantSettlementDetails(any(), any(), any(), any(), any())
    }

    @Test
    fun `should get participant IO data DTO`() {
        val date = LocalDate.now()
        val participants = MockParticipants().participants
        val ioData = MockIOData().getParticipantsIOData()

        val result = uiPresenter.presentInputOutput(participants, ioData, date)

        assertEquals("0.67", result.batchesRejected)
        assertEquals("0.67", result.filesRejected)
        assertEquals("0.67", result.transactionsRejected)

        assertEquals(3, result.rows.size)

        assertEquals("ESSESESS", result.rows[0].participant.id)
        assertEquals("ESSESESS", result.rows[0].participant.bic)
        assertEquals("SEB Bank", result.rows[0].participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.rows[0].participant.status)
        assertNull(result.rows[0].participant.suspendedTime)

        assertEquals(1, result.rows[0].batches.submitted)
        assertEquals(1.00, result.rows[0].batches.rejected)
        assertEquals(1, result.rows[0].files.submitted)
        assertEquals(1.00, result.rows[0].files.rejected)
        assertEquals(1, result.rows[0].transactions.submitted)
        assertEquals(1.00, result.rows[0].transactions.rejected)

        assertEquals("HANDSESS", result.rows[1].participant.id)
        assertEquals("HANDSESS", result.rows[1].participant.bic)
        assertEquals("Svenska Handelsbanken", result.rows[1].participant.name)
        assertEquals(ParticipantStatus.SUSPENDED, result.rows[1].participant.status)
        assertNotNull(result.rows[1].participant.suspendedTime)

        assertEquals(10, result.rows[1].batches.submitted)
        assertEquals(1.00, result.rows[1].batches.rejected)
        assertEquals(10, result.rows[1].files.submitted)
        assertEquals(1.00, result.rows[1].files.rejected)
        assertEquals(10, result.rows[1].transactions.submitted)
        assertEquals(1.00, result.rows[1].transactions.rejected)

        assertEquals("NDEASESSXXX", result.rows[2].participant.id)
        assertEquals("NDEASESSXXX", result.rows[2].participant.bic)
        assertEquals("Nordea", result.rows[2].participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.rows[2].participant.status)
        assertNull(result.rows[2].participant.suspendedTime)

        assertEquals(0, result.rows[2].batches.submitted)
        assertEquals(0.00, result.rows[2].batches.rejected)
        assertEquals(0, result.rows[2].files.submitted)
        assertEquals(0.00, result.rows[2].files.rejected)
        assertEquals(0, result.rows[2].transactions.submitted)
        assertEquals(0.00, result.rows[2].transactions.rejected)
    }

    @Test
    fun `should present io Details`() {
        val result = uiPresenter.presentIoDetails(
                MockParticipants().getParticipant(false),
                MockIOData().getIODetails(),
                LocalDate.now()
        )

        assertEquals(3, result.batches.size)
        assertEquals("Pacs.008", result.batches[0].code)
        assertEquals("Customer Credit Transfer", result.batches[0].name)

        assertEquals(10, result.files.submitted)
        assertEquals(10, result.files.accepted)
        assertEquals(1.5, result.files.rejected)
        assertEquals(10, result.files.output)

        assertEquals(10, result.batches[0].data.accepted)
        assertEquals(10, result.batches[0].data.output)
        assertEquals(1.50, result.batches[0].data.rejected)
        assertEquals(10, result.batches[0].data.submitted)

        assertEquals(3, result.transactions.size)
        assertEquals("Pacs.004", result.transactions[1].code)
        assertEquals("Payment Return", result.transactions[1].name)
        assertEquals(10, result.transactions[1].data.accepted)
        assertEquals(10, result.transactions[1].data.output)
        assertEquals(1.50, result.transactions[1].data.rejected)
        assertEquals(10, result.transactions[1].data.submitted)
        assertEquals(10, result.transactions[1].data.amountAccepted)
        assertEquals(10, result.transactions[1].data.amountOutput)
    }

    @Test
    fun `should get alert references`() {
        val priorityName = "Priority1"
        val threshold = 10
        val alertType = "alertType1"
        val priority1 = AlertPriorityData(priorityName, threshold, true)
        val priority2 = AlertPriorityData("Priority2", 100, false)

        val model = AlertReferenceData(
                listOf(priority1, priority2),
                listOf(alertType, "alertType2")
        )
        val result = uiPresenter.presentAlertReference(model)

        assertEquals(2, result.alertTypes.size)
        assertEquals(2, result.priorities.size)
        assertEquals(alertType, result.alertTypes[0])
        assertEquals(priorityName, result.priorities[0].name)
        assertEquals(threshold, result.priorities[0].threshold)

    }

    @Test
    fun `should get alert stats`() {
        val priority = AlertPriorityType.HIGH
        val count = 10
        val total = 100
        val alertData = AlertStatsData(priority, count)
        val model = AlertStats(total, listOf(alertData))
        val result = DTOMapper.MAPPER.toDto(model)

        assertEquals(1, result.items.size)
        assertEquals(total, result.total)
        assertEquals(priority, result.items[0].priority)
        assertEquals(count, result.items[0].count)
    }

    @Test
    fun `should get participant references sorted by name`() {
        val aaa = "Aaa"
        val bbb = "Bbb"
        val ccc = "Ccc"
        val id = "id"
        val participantType = ParticipantType.DIRECT_ONLY
        val model = listOf(
                ParticipantReference(id, ccc, participantType, null, null),
                ParticipantReference(id, aaa, participantType, null, null),
                ParticipantReference(id, bbb, participantType, null, null)
        )

        val result = uiPresenter.presentParticipantReferences(model)

        assertEquals(aaa, result[0].name)
        assertEquals(bbb, result[1].name)
        assertEquals(ccc, result[2].name)
    }

    @Test
    fun `should get P27 as first participant references sorted by name`() {
        val aaa = "Aaa"
        val bbb = "Bbb"
        val p27 = "P27"
        val id = "id"

        val pTypeDirect = ParticipantType.DIRECT_ONLY
        val pTypeFunding = ParticipantType.FUNDING
        val pTypeScheme = ParticipantType.SCHEME

        val model = listOf(
                ParticipantReference(id, aaa, pTypeDirect, null, null),
                ParticipantReference(id, p27, pTypeScheme, null, null),
                ParticipantReference(id, bbb, pTypeFunding, null, null)
        )

        val result = uiPresenter.presentParticipantReferences(model)

        assertEquals(p27, result[0].name)
        assertEquals(aaa, result[1].name)
        assertEquals(bbb, result[2].name)
    }

    @Test
    fun `should get UI ClientType`() {
        val result = uiPresenter.clientType
        assertEquals(ClientType.UI, result)
    }

    @Test
    fun `should map all Message references fields and set isDefault true for sending`() {
        val sending = "sending"
        val receiving = "receiving"
        val model = listOf(
                MessageDirectionReference.builder()
                        .name(sending)
                        .types(emptyList())
                        .build(),
                MessageDirectionReference.builder()
                        .name(receiving)
                        .types(emptyList())
                        .build()
        )
        val result = uiPresenter.presentMessageDirectionReferences(model)

        assertEquals(sending, result[0].name)
        assertTrue(result[0].isDefault)

        assertEquals(receiving, result[1].name)
        assertFalse(result[1].isDefault)
    }

    @Test
    fun `should present alerts`() {
        val id = "NDEASESSXXX"
        val nordea = "Nordea"
        val seb = "SEB Bank"
        val participantType = ParticipantType.DIRECT_ONLY
        val alerts = listOf(
                Alert.builder()
                        .alertId(3141)
                        .priority(AlertPriorityType.HIGH)
                        .dateRaised(ZonedDateTime.now())
                        .type("rejected-central-bank")
                        .entities(listOf(ParticipantReference(id, nordea, participantType, null, null)))
                        .build(),
                Alert.builder()
                        .alertId(3142)
                        .priority(AlertPriorityType.HIGH)
                        .dateRaised(ZonedDateTime.now())
                        .type("rejected-central-bank")
                        .entities(listOf(ParticipantReference(id, seb, participantType, null, null)))
                        .build())

        val alertsResponse = Page<Alert>(2, alerts)

        val result = uiPresenter.presentAlert(alertsResponse)

        assertThat(result).isNotNull

        assertThat((result.items.elementAt(0) as AlertDto).alertId).isEqualTo(3141)
        assertThat((result.items.elementAt(0) as AlertDto).entities[0].name).isEqualTo("Nordea")

        assertThat((result.items.elementAt(1) as AlertDto).alertId).isEqualTo(3142)
        assertThat((result.items.elementAt(1) as AlertDto).entities[0].name).isEqualTo("SEB Bank")
    }

    @Test
    fun `should present filtered File references by enquiryType`() {
        val batchType = "BATCH"
        val fileReferences = listOf(
                FileReference.builder().enquiryType(batchType).build(),
                FileReference.builder().enquiryType("TRANSACTION").build(),
                FileReference.builder().enquiryType("FILES").build()
        )
        val result = uiPresenter.presentFileReferencesFor(fileReferences, batchType)
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].enquiryType).isEqualTo(batchType)
    }
}
