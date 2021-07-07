package com.vocalink.crossproduct.ui.presenter

import com.fasterxml.jackson.databind.ObjectMapper
import com.vocalink.crossproduct.TestConstants.FIXED_CLOCK
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
import com.vocalink.crossproduct.domain.permission.UIPermission
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference
import com.vocalink.crossproduct.domain.reference.MessageReferenceDirection.*
import com.vocalink.crossproduct.domain.reference.MessageReferenceLevel.*
import com.vocalink.crossproduct.domain.reference.MessageReferenceType.*
import com.vocalink.crossproduct.domain.reference.ReasonCodeReference
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfig
import com.vocalink.crossproduct.mocks.MockIOData
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.ui.aspects.ContentUtils
import com.vocalink.crossproduct.ui.dto.alert.AlertDto
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.stream.Stream

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [UIPresenter::class, ContentUtils::class, ObjectMapper::class, BPSTestConfig::class])
@TestPropertySource("classpath:application.properties")
class UIPresenterTest {

    @Autowired
    private lateinit var uiPresenter: UIPresenter

    companion object {
        val clock = FIXED_CLOCK

        class CycleDashboardTestCase (val currentCycle: Cycle?, val previousCycle: Cycle?,
                                      val showCurrent: Boolean, val showPrevious: Boolean)

        @JvmStatic
        fun getEodSodTestData() = Stream.of(
            Arguments.of(
                "During the day from 7 AM till 4 PM, before SOD/EOD",
                CycleDashboardTestCase(
                    currentCycle =  Cycle.builder()
                        .id("002")
                        .status(CycleStatus.OPEN)
                        .isNextDayCycle(false)
                        .settlementTime(ZonedDateTime.now(clock))
                        .build(),
                    previousCycle = Cycle.builder()
                        .id("001")
                        .status(CycleStatus.CLOSED)
                        .settlementTime(ZonedDateTime.now(clock))
                        .build(),
                    showCurrent = true,
                    showPrevious = true
                )
            ),
            Arguments.of(
                "SOD/EOD when cycles are in different dates",
                CycleDashboardTestCase(
                    currentCycle = Cycle.builder()
                        .id("002")
                        .status(CycleStatus.NOT_STARTED)
                        .isNextDayCycle(true)
                        .settlementTime(ZonedDateTime.now(clock).plusDays(1))
                        .build(),
                    previousCycle = Cycle.builder()
                        .id("001")
                        .status(CycleStatus.CLOSED)
                        .settlementTime(ZonedDateTime.now(clock))
                        .build(),
                    showCurrent = false,
                    showPrevious = true
                )
            ),
            Arguments.of(
                "BPS is in a first cycle of the next day, no previous cycle",
                CycleDashboardTestCase(
                    currentCycle = Cycle.builder()
                        .id("002")
                        .status(CycleStatus.OPEN)
                        .isNextDayCycle(true)
                        .build(),
                    previousCycle = null,
                    showCurrent = true,
                    showPrevious = false
                )
            ),
            Arguments.of(
                "BPS is in a first cycle of the current day, no previous cycle",
                CycleDashboardTestCase(
                    currentCycle = Cycle.builder()
                        .id("002")
                        .status(CycleStatus.OPEN)
                        .isNextDayCycle(false)
                        .build(),
                    previousCycle = null,
                    showCurrent = true,
                    showPrevious = false
                )
            ),
            Arguments.of(
                "From 4 PM till SOD completion",
                CycleDashboardTestCase(
                    currentCycle = Cycle.builder()
                        .id("002")
                        .status(CycleStatus.NOT_STARTED)
                        .settlementTime(ZonedDateTime.now(clock))
                        .isNextDayCycle(false)
                        .build(),
                    previousCycle = Cycle.builder()
                        .id("001")
                        .status(CycleStatus.CLOSED)
                        .settlementTime(ZonedDateTime.now(clock))
                        .build(),
                    showCurrent = false,
                    showPrevious = true
                )
            ),
            Arguments.of(
                "SOD completion till 7 AM",
                CycleDashboardTestCase(
                    currentCycle = Cycle.builder()
                        .id("002")
                        .status(CycleStatus.OPEN)
                        .settlementTime(ZonedDateTime.now(clock))
                        .build(),
                    previousCycle = null,
                    showCurrent = true,
                    showPrevious = false
                )
            ),
            Arguments.of(
                "7 AM on the Go-Live Day",
                CycleDashboardTestCase(
                    currentCycle = Cycle.builder()
                        .id("002")
                        .status(CycleStatus.OPEN)
                        .settlementTime(ZonedDateTime.now(clock))
                        .isNextDayCycle(false)
                        .build(),
                    previousCycle = null,
                    showCurrent = true,
                    showPrevious = false
                )
            )
        )
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getEodSodTestData")
    fun `should return current settlement cycle`(description: String, testCase: CycleDashboardTestCase) {
        val cycles = listOf(testCase.currentCycle, testCase.previousCycle)

        val currentCycle = uiPresenter.getCurrentCycle(cycles)

        assertThat(currentCycle.isEmpty).isEqualTo(!testCase.showCurrent)
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getEodSodTestData")
    fun `should return previous settlement cycle`(description: String, testCase: CycleDashboardTestCase) {
        val cycles = listOf(testCase.currentCycle, testCase.previousCycle)

        val previousCycle = uiPresenter.getPreviousCycle(cycles)

        assertThat(previousCycle.isEmpty).isEqualTo(!testCase.showPrevious)
    }

    @Test
    fun `should get Settlement Dashboard DTO for all participants if cycles list is empty`() {
        val cycles = emptyList<Cycle>()
        val participants = MockParticipants().participants

        val result = uiPresenter.presentAllParticipantsSettlement(cycles, participants)

        assertThat(result.currentCycle).extracting("id", "settlementTime", "cutOffTime", "status").containsOnlyNulls()
        assertThat(result.previousCycle).extracting("id", "settlementTime", "cutOffTime", "status").containsOnlyNulls()
    }

    @Test
    fun `should get Settlement Dashboard DTO for all participants if cycles contain only current cycle`() {
        val cycles = listOf(Cycle.builder()
                .settlementTime(ZonedDateTime.now(clock).plusDays(1))
                .id("03")
                .build())
        val participants = MockParticipants().participants

        val result = uiPresenter.presentAllParticipantsSettlement(cycles, participants)

        assertThat(result.previousCycle).extracting("id", "settlementTime", "cutOffTime", "status").containsOnlyNulls()
        assertThat(result.currentCycle.id).isEqualTo("03")
        assertThat(result.currentCycle.settlementTime).isNotNull()
    }

    @Test
    fun `should get Settlement Dashboard DTO for all participants if cycles contain current and previous`() {
        val cycles = listOf(
            Cycle.builder()
                .settlementTime(ZonedDateTime.now(clock))
                .id("03")
                .build(),
            Cycle.builder()
                .settlementTime(ZonedDateTime.now(clock))
                .id("02")
                .build()
        )
        val participants = MockParticipants().participants

        val result = uiPresenter.presentAllParticipantsSettlement(cycles, participants)

        assertThat(result.previousCycle.id).isEqualTo("02")
        assertThat(result.currentCycle.id).isEqualTo("03")
    }

    @Test
    fun `should get Settlement Dashboard DTO if cycles contain next day cycle and previous cycle`() {
        val cycles = listOf(
            Cycle.builder()
                .settlementTime(ZonedDateTime.now(clock))
                .isNextDayCycle(true)
                .id("03")
                .build(),
            Cycle.builder()
                .settlementTime(ZonedDateTime.now(clock))
                .id("02")
                .build()
        )
        val participants = MockParticipants().participants

        val result = uiPresenter.presentAllParticipantsSettlement(cycles, participants)

        assertThat(result.previousCycle.id).isEqualTo("02")
        assertThat(result.currentCycle.id).isEqualTo("03")
    }

    @Test
    fun `should get Settlement Dashboard DTO if cycles contain next day settlement cycle and previous cycle`() {
        val cycles = listOf(
            Cycle.builder()
                .settlementTime(ZonedDateTime.now(clock).plusDays(1))
                .status(CycleStatus.NOT_STARTED)
                .isNextDayCycle(false)
                .id("03")
                .build(),
            Cycle.builder()
                .settlementTime(ZonedDateTime.now(clock))
                .status(CycleStatus.CLOSED)
                .id("02")
                .build()
        )
        val participants = MockParticipants().participants

        val result = uiPresenter.presentAllParticipantsSettlement(cycles, participants)

        assertThat(result.currentCycle).extracting("id", "settlementTime", "cutOffTime", "status").containsOnlyNulls()
        assertThat(result.previousCycle.id).isEqualTo("02")
    }

    @Test
    fun `should get Settlement Dashboard DTO for paramId with null values if missing IntraDay or Positions`() {
        val cycles = listOf(
            Cycle.builder()
                .id("02")
                .build(),
            Cycle.builder()
                .id("01")
                .build()
        )
        val participants = MockParticipants().participants
        val fundingParticipant = MockParticipants().getParticipant(false)
        val result = uiPresenter.presentFundingParticipantSettlement(cycles, participants, fundingParticipant, emptyList())
        assertThat(result.fundingParticipant).isNotNull()
        assertThat(result.fundingParticipant.bic).isEqualTo("NDEASESSXXX")
        assertThat(result.intraDayPositionTotals).isNotNull()

        assertThat(result.intraDayPositionTotals).isNotNull()
        assertThat(result.currentPositionTotals).isNotNull()
        assertThat(result.previousPositionTotals).isNotNull()

        assertThat(result.positions[0].currentPosition.credit).isNull()
        assertThat(result.positions[0].currentPosition.debit).isNull()
        assertThat(result.positions[0].previousPosition.credit).isNull()
        assertThat(result.positions[0].previousPosition.debit).isNull()
        assertThat(result.positions[0].intraDayPositionGross.debitCap).isNull()
        assertThat(result.positions[0].intraDayPositionGross.debitPosition).isNull()
    }

    @Test
    fun `should get participant IO data DTO`() {
        val date = LocalDate.now()
        val participants = listOf(
                Participant.builder()
                        .id("ESSESESS")
                        .bic("ESSESESS")
                        .fundingBic("NA")
                        .name("SEB Bank")
                        .suspendedTime(null)
                        .status(ParticipantStatus.ACTIVE)
                        .build(),
                Participant.builder()
                        .id("HANDSESS")
                        .bic("HANDSESS")
                        .fundingBic("NDEASESSXXX")
                        .name("Svenska Handelsbanken")
                        .suspendedTime(ZonedDateTime.now(ZoneId.of("UTC")).plusDays(15))
                        .status(ParticipantStatus.SUSPENDED)
                        .participantType(ParticipantType.DIRECT)
                        .build(),
                Participant.builder()
                        .id("NDEASESSXXX")
                        .bic("NDEASESSXXX")
                        .fundingBic("NA")
                        .name("Nordea")
                        .suspendedTime(null)
                        .status(ParticipantStatus.ACTIVE)
                        .participantType(ParticipantType.DIRECT)
                        .build()
        )

        val filesRejected = "1.98%"
        val batchesRejected = "2.09%"
        val transactionsRejected = "1.90%"
        val ioData = IODashboard(filesRejected, batchesRejected, transactionsRejected, MockIOData().getParticipantsIOData())

        val result = uiPresenter.presentInputOutput(participants, ioData, date)

        assertThat(result.batchesRejected).isEqualTo("2.09")
        assertThat(result.filesRejected).isEqualTo("1.98")
        assertThat(result.transactionsRejected).isEqualTo("1.90")
        assertThat(result.rows.size).isEqualTo(3)
        assertThat(result.rows.map { e -> e.transactions.submitted }).containsExactly(1, 10, 0)
        assertThat(result.rows.map { e -> e.transactions.rejected }).containsExactly(1.00, 1.00, 0.00)
        assertThat(result.rows.map { e -> e.batches.submitted }).containsExactly(1, 10, 0)
        assertThat(result.rows.map { e -> e.batches.rejected }).containsExactly(1.00, 1.00, 0.00)
        assertThat(result.rows.map { e -> e.files.submitted }).containsExactly(1, 10, 0)
        assertThat(result.rows.map { e -> e.files.rejected }).containsExactly(1.00, 1.00, 0.00)
        assertThat(result.rows.map { e -> e.participant.id }).containsExactly("ESSESESS", "HANDSESS", "NDEASESSXXX")
        assertThat(result.rows.map { e -> e.participant.bic }) .containsExactly("ESSESESS", "HANDSESS", "NDEASESSXXX")
        assertThat(result.rows.map { e -> e.participant.name })
            .containsExactly("SEB Bank", "Svenska Handelsbanken", "Nordea")
        assertThat(result.rows.map { e -> e.participant.status })
            .containsExactly(ParticipantStatus.ACTIVE, ParticipantStatus.SUSPENDED, ParticipantStatus.ACTIVE)
        assertThat(result.rows[0].participant.suspendedTime).isNull()
        assertThat(result.rows[1].participant.suspendedTime).isNotNull()
        assertThat(result.rows[2].participant.suspendedTime).isNull()
    }

    @Test
    fun `should present io Details`() {
        val result = uiPresenter.presentIoDetails(
                MockParticipants().getParticipant(false),
                MockIOData().getIODetails(),
                LocalDate.now()
        )

        assertThat(result.batches.size).isEqualTo(3)
        assertThat(result.transactions.size).isEqualTo(3)
        assertThat(result.files.submitted).isEqualTo(10)
        assertThat(result.files.accepted).isEqualTo(10)
        assertThat(result.files.rejected).isEqualTo(1.5)
        assertThat(result.files.output).isEqualTo(10)
        assertThat(result.transactions.map { e -> e.data.submitted }).containsExactly(10, 10, 10)
        assertThat(result.transactions.map { e -> e.data.rejected }).containsExactly(1.50, 1.50, 1.50)
        assertThat(result.transactions.map { e -> e.data.amountAccepted }).containsExactly(10.0, 10.0, 10.0)
        assertThat(result.transactions.map { e -> e.data.amountOutput }).containsExactly(10.0, 10.0, 10.0)
        assertThat(result.batches.map { e -> e.data.submitted }).containsExactly(10, 10, 10)
        assertThat(result.batches.map { e -> e.data.accepted }).containsExactly(10, 10, 10)
        assertThat(result.batches.map { e -> e.data.rejected }).containsExactly(1.50, 1.50, 1.50)
        assertThat(result.batches.map { e -> e.code }).containsExactly("Pacs.008", "Pacs.004", "Pacs.002")
        assertThat(result.batches.map { e -> e.name })
            .containsExactly("Customer Credit Transfer", "Payment Return", "Payment Reversal")
        assertThat(result.transactions.map { e -> e.code }).containsExactly("Pacs.008", "Pacs.004", "Pacs.002")
        assertThat(result.transactions.map { e -> e.name })
            .containsExactly("Customer Credit Transfer", "Payment Return", "Payment Reversal")
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

        assertThat(result.alertTypes.size).isEqualTo(2)
        assertThat(result.priorities.size).isEqualTo(2)
        assertThat(result.alertTypes[0]).isEqualTo(alertType)
        assertThat(result.priorities[0].name).isEqualTo(priorityName)
        assertThat(result.priorities[0].threshold).isEqualTo(threshold)

    }

    @Test
    fun `should get alert stats`() {
        val priority = AlertPriorityType.HIGH
        val count = 10
        val total = 100
        val alertData = AlertStatsData(priority, count)
        val model = AlertStats(total, listOf(alertData))
        val result = DTOMapper.MAPPER.toDto(model)

        assertThat(result.items.size).isEqualTo(1)
        assertThat(result.total).isEqualTo(total)
        assertThat(result.items[0].priority).isEqualTo(priority)
        assertThat(result.items[0].count).isEqualTo(count)
    }

    @Test
    fun `should get participant references sorted by name`() {
        val name0 = "Aaa"
        val name1 = "Bbb"
        val name2 = "Ccc"
        val id = "id"
        val participantType = ParticipantType.DIRECT
        val model = listOf(
                Participant.builder()
                        .id(id)
                        .name(name2)
                        .participantType(participantType)
                        .build(),
                Participant.builder()
                        .id(id)
                        .name(name0)
                        .participantType(participantType)
                        .build(),
                Participant.builder()
                        .id(id)
                        .name(name1)
                        .participantType(participantType)
                        .build()
        )

        val result = uiPresenter.presentParticipantReferences(model)

        assertThat(result.map { e -> e.name }).containsExactly(name0, name1, name2)
    }

    @Test
    fun `should get P27 as first participant references sorted by name`() {
        val name1 = "Aaa"
        val name2 = "Bbb"
        val name0 = "P27"
        val id = "id"

        val pTypeDirect = ParticipantType.DIRECT
        val pTypeFunding = ParticipantType.FUNDING
        val pTypeScheme = ParticipantType.SCHEME_OPERATOR

        val model = listOf(
                Participant.builder()
                        .id(id)
                        .name(name1)
                        .participantType(pTypeDirect)
                        .build(),
                Participant.builder()
                        .id(id)
                        .name(name0)
                        .participantType(pTypeScheme)
                        .build(),
                Participant.builder()
                        .id(id)
                        .name(name2)
                        .participantType(pTypeFunding)
                        .build()
        )

        val result = uiPresenter.presentParticipantReferences(model)

        assertThat(result.map { e -> e.name }).containsExactly(name0, name1, name2)
    }

    @Test
    fun `should get UI ClientType`() {
        val result = uiPresenter.clientType
        assertThat(result).isEqualTo(ClientType.UI)
    }

    @Test
    fun `should present all Message references`() {
        val messageType = "camt.029.001.08"
        val formatName = "camt.029.08"
        val sending = SENDING
        val receiving = RECEIVING
        val model = listOf(
            MessageDirectionReference.builder()
                .messageType(messageType)
                .formatName(formatName)
                .direction(listOf(sending))
                .level(listOf(FILE))
                .subType(listOf(PAYMENT))
                .build(),
            MessageDirectionReference.builder()
                .messageType(messageType)
                .formatName(formatName)
                .direction(listOf(receiving))
                .level(listOf(TRANSACTION))
                .subType(listOf(NON_PAYMENT))
                .build()
        )
        val result = uiPresenter.presentMessageDirectionReferences(model)

        assertThat(result.size).isNotNull()
        assertThat(result[0].messageType).isEqualTo(messageType)
        assertThat(result[0].formatName).isEqualTo(formatName)

        assertThat(result[0].direction).isEqualTo(listOf(sending))
        assertThat(result[0].level).isEqualTo(listOf(FILE))
        assertThat(result[0].subType).isEqualTo(listOf(PAYMENT))

        assertThat(result[1].direction).isEqualTo(listOf(receiving))
        assertThat(result[1].level).isEqualTo(listOf(TRANSACTION))
        assertThat(result[1].subType).isEqualTo(listOf(NON_PAYMENT))
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
        val reasonCode1 = ReasonCodeReference.ReasonCode(
                "F01",
                "description 1",
                true
        )
        val reasonCode2 = ReasonCodeReference.ReasonCode(
                "F02",
                "description 2",
                false
        )
        val validation = ReasonCodeReference.Validation(
                "FILE",
                listOf(reasonCode1, reasonCode2)
        )
        val status0 = "ACK"
        val status1 = "NAK"

        val result = uiPresenter.presentReasonCodeReferences(validation, listOf(status0, status1))

        assertThat(result.size).isEqualTo(2)
        assertThat(result[0].reasonCodes).isEmpty()
        assertThat(result[1].reasonCodes.size).isEqualTo(2)
        assertThat(result.map { e -> e.enquiryType }).containsOnly(validation.validationLevel)
        assertThat(result.map { e -> e.hasReason }).containsExactly(false, true)
        assertThat(result.map { e -> e.status }).containsExactly(status0, status1)
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

    @Test
    fun `should present current user info`() {
        val participantId = "HANDSESS"
        val participantName = "Svenska Handelsbanken"
        val permissionId = "11111"
        val permission = "read.participant-config"
        val userId = "12a543"
        val userFirstName = "John"
        val userLastName = "Doe"

        val participant = Participant.builder()
            .id(participantId)
            .bic(participantId)
            .name(participantName)
            .build()
        val uiPermission = UIPermission.builder()
            .id(permissionId)
            .key(permission)
            .build()
        val auditDetails =  AuditDetails.builder()
            .username(userId)
            .firstName(userFirstName)
            .lastName(userLastName)
            .build()

        val userInfo = uiPresenter.presentCurrentUserInfo(participant, listOf(uiPermission), auditDetails)

        assertThat(userInfo).isNotNull
        assertThat(userInfo.permissions).isNotEmpty
        assertThat(userInfo.participation).isNotNull
        assertThat(userInfo.user).isNotNull

        assertThat(userInfo.permissions.size).isEqualTo(1)
        assertThat(userInfo.permissions[0]).isEqualTo(permission)

        assertThat(userInfo.participation.id).isEqualTo(participantId)
        assertThat(userInfo.participation.bic).isEqualTo(participantId)
        assertThat(userInfo.participation.name).isEqualTo(participantName)

        assertThat(userInfo.user.userId).isEqualTo(userId)
        assertThat(userInfo.user.name).isEqualTo(userFirstName.plus(" ").plus(userLastName))
    }
}
