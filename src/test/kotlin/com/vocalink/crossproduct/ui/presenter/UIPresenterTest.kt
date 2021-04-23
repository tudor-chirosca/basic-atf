package com.vocalink.crossproduct.ui.presenter

import com.fasterxml.jackson.databind.ObjectMapper
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.alert.Alert
import com.vocalink.crossproduct.domain.alert.AlertPriorityData
import com.vocalink.crossproduct.domain.alert.AlertPriorityType
import com.vocalink.crossproduct.domain.alert.AlertReferenceData
import com.vocalink.crossproduct.domain.alert.AlertStats
import com.vocalink.crossproduct.domain.alert.AlertStatsData
import com.vocalink.crossproduct.domain.audit.AuditDetails
import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.io.IODashboard
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.reference.EnquiryType
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference
import com.vocalink.crossproduct.domain.reference.ReasonCodeValidation
import com.vocalink.crossproduct.mocks.MockIOData
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.ui.aspects.ContentUtils
import com.vocalink.crossproduct.ui.dto.alert.AlertDto
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [UIPresenter::class, ContentUtils::class, ObjectMapper::class])
@TestPropertySource("classpath:application.properties")
class UIPresenterTest {

    @Autowired
    private lateinit var uiPresenter: UIPresenter

    @Test
    fun `should get Settlement Dashboard DTO for paramId with null values if missing IntraDay or Positions`() {
        val cycles = listOf(Cycle.builder()
                .cutOffTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                .settlementTime(ZonedDateTime.of(2019, 12, 10, 15, 10, 0, 0, ZoneId.of("UTC")))
                .id("02")
                .status(CycleStatus.OPEN)
                .build(),
                Cycle.builder()
                        .cutOffTime(ZonedDateTime.of(2019, 12, 10, 10, 10, 0, 0, ZoneId.of("UTC")))
                        .settlementTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                        .id("01")
                        .status(CycleStatus.COMPLETED)
                        .build()
        )
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
    fun `should get participant IO data DTO`() {
        val date = LocalDate.now()
        val participants = MockParticipants().participants
        val filesRejected = "1.98%"
        val batchesRejected = "2.09%"
        val transactionsRejected = "1.90%"
        val ioData = IODashboard(filesRejected, batchesRejected, transactionsRejected, MockIOData().getParticipantsIOData())

        val result = uiPresenter.presentInputOutput(participants, ioData, date)

        assertEquals("2.09", result.batchesRejected)
        assertEquals("1.98", result.filesRejected)
        assertEquals("1.90", result.transactionsRejected)

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
        val participantType = ParticipantType.DIRECT
        val model = listOf(
                Participant.builder()
                        .id(id)
                        .name(ccc)
                        .participantType(participantType)
                        .build(),
                Participant.builder()
                        .id(id)
                        .name(aaa)
                        .participantType(participantType)
                        .build(),
                Participant.builder()
                        .id(id)
                        .name(bbb)
                        .participantType(participantType)
                        .build()
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

        val pTypeDirect = ParticipantType.DIRECT
        val pTypeFunding = ParticipantType.FUNDING
        val pTypeScheme = ParticipantType.SCHEME_OPERATOR

        val model = listOf(
                Participant.builder()
                        .id(id)
                        .name(aaa)
                        .participantType(pTypeDirect)
                        .build(),
                Participant.builder()
                        .id(id)
                        .name(p27)
                        .participantType(pTypeScheme)
                        .build(),
                Participant.builder()
                        .id(id)
                        .name(bbb)
                        .participantType(pTypeFunding)
                        .build()
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
        val participantType = ParticipantType.DIRECT
        val alerts = listOf(
                Alert.builder()
                        .alertId(3141)
                        .priority(AlertPriorityType.HIGH)
                        .dateRaised(ZonedDateTime.now())
                        .type("rejected-central-bank")
                        .entities(listOf(
                                Participant.builder()
                                        .id(id)
                                        .name(nordea)
                                        .participantType(participantType)
                                        .build()
                        ))
                        .build(),
                Alert.builder()
                        .alertId(3142)
                        .priority(AlertPriorityType.HIGH)
                        .dateRaised(ZonedDateTime.now())
                        .type("rejected-central-bank")
                        .entities(listOf(
                                Participant.builder()
                                        .id(id)
                                        .name(seb)
                                        .participantType(participantType)
                                        .build()
                        ))
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
        val reasonCode = ReasonCodeValidation.ReasonCode(
                "F01",
                "description",
                true
        )
        val validation = ReasonCodeValidation(
                EnquiryType.FILES,
                listOf(reasonCode)
        )
        val statuses = listOf("NAK", "ACK")
        val result = uiPresenter.presentReasonCodeReferences(validation, statuses)
        assertThat(result.size).isEqualTo(2)
        assertThat(result[0].enquiryType).isEqualTo(validation.validationLevel.name)
        assertThat(result[0].isHasReason).isEqualTo(true)
        assertThat(result[0].status).isEqualTo(statuses[0])
        assertThat(result[0].reasonCodes[0]).isEqualTo(reasonCode.reasonCode)

        assertThat(result[1].enquiryType).isEqualTo(validation.validationLevel.name)
        assertThat(result[1].isHasReason).isEqualTo(false)
        assertThat(result[1].status).isEqualTo(statuses[1])
        assertThat(result[1].reasonCodes).isEmpty()
    }

    @Test
    fun `should present user details`() {
        val auditDetails = listOf(AuditDetails.builder()
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .build())
        val userDetails = uiPresenter.presentUserDetails(auditDetails)

        assertThat(userDetails[0].username).isEqualTo(auditDetails[0].username)
        assertThat(userDetails[0].fullName).contains(auditDetails[0].firstName)
        assertThat(userDetails[0].fullName).contains(auditDetails[0].lastName)
    }
}
