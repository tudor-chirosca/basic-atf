package com.vocalink.crossproduct.ui.presenter.mapper

import com.vocalink.crossproduct.TestConstants.FIXED_CLOCK
import com.vocalink.crossproduct.domain.Amount
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.account.Account
import com.vocalink.crossproduct.domain.alert.Alert
import com.vocalink.crossproduct.domain.alert.AlertPriorityData
import com.vocalink.crossproduct.domain.alert.AlertPriorityType
import com.vocalink.crossproduct.domain.alert.AlertReferenceData
import com.vocalink.crossproduct.domain.alert.AlertStats
import com.vocalink.crossproduct.domain.alert.AlertStatsData
import com.vocalink.crossproduct.domain.approval.Approval
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationResponse
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType.PARTICIPANT_SUSPEND
import com.vocalink.crossproduct.domain.approval.ApprovalStatus.APPROVED
import com.vocalink.crossproduct.domain.approval.ApprovalStatus.PENDING
import com.vocalink.crossproduct.domain.audit.AuditDetails
import com.vocalink.crossproduct.domain.audit.UserDetails
import com.vocalink.crossproduct.domain.batch.Batch
import com.vocalink.crossproduct.domain.configuration.Configuration
import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.cycle.DayCycle
import com.vocalink.crossproduct.domain.files.EnquirySenderDetails
import com.vocalink.crossproduct.domain.files.File
import com.vocalink.crossproduct.domain.io.IODashboard
import com.vocalink.crossproduct.domain.io.IOData
import com.vocalink.crossproduct.domain.io.ParticipantIOData
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantConfiguration
import com.vocalink.crossproduct.domain.participant.ParticipantStatus.ACTIVE
import com.vocalink.crossproduct.domain.participant.ParticipantStatus.SUSPENDED
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.participant.ParticipantType.DIRECT
import com.vocalink.crossproduct.domain.participant.ParticipantType.FUNDED
import com.vocalink.crossproduct.domain.participant.ParticipantType.FUNDING
import com.vocalink.crossproduct.domain.participant.SuspensionLevel.SCHEME
import com.vocalink.crossproduct.domain.participant.SuspensionLevel.SELF
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross
import com.vocalink.crossproduct.domain.position.ParticipantPosition
import com.vocalink.crossproduct.domain.position.Payment
import com.vocalink.crossproduct.domain.report.Report
import com.vocalink.crossproduct.domain.routing.RoutingRecord
import com.vocalink.crossproduct.domain.settlement.InstructionStatus
import com.vocalink.crossproduct.domain.settlement.SettlementCycleSchedule
import com.vocalink.crossproduct.domain.settlement.SettlementDetails
import com.vocalink.crossproduct.domain.transaction.Transaction
import com.vocalink.crossproduct.ui.dto.alert.AlertDto
import com.vocalink.crossproduct.ui.dto.batch.BatchDto
import com.vocalink.crossproduct.ui.dto.file.FileDto
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantInstructionDto
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class DTOMapperTest {

    private companion object {
        @JvmStatic
        var clock = FIXED_CLOCK
    }

    @Test
    fun `should map all fields`() {
        val id = "anyId"
        val cutoffTime = ZonedDateTime.now(clock).plusMinutes(1)
        val settlementTime = ZonedDateTime.now(clock)
        val status = CycleStatus.COMPLETED

        val cycle = Cycle.builder()
                .id(id)
                .cutOffTime(cutoffTime)
                .settlementTime(settlementTime)
                .status(status)
                .build()

        val dto = MAPPER.toDto(cycle)

        assertThat(dto.id).isEqualTo(id)
        assertThat(dto.cutOffTime).isEqualTo(cutoffTime)
        assertThat(dto.settlementTime).isEqualTo(settlementTime)
        assertThat(dto.status).isEqualTo(status)
    }

    @Test
    fun `should map all Alert Reference fields`() {
        val priorityName = "Priority1"
        val threshold = 10
        val alertType = "alertType1"
        val alertPriority = AlertPriorityData(priorityName, threshold, true)
        val model = AlertReferenceData(
                listOf(alertPriority),
                listOf(alertType)
        )
        val result = MAPPER.toDto(model)

        assertThat(result.alertTypes.size).isEqualTo(1)
        assertThat(result.priorities.size).isEqualTo(1)
        assertThat(result.alertTypes[0]).isEqualTo(alertType)
        assertThat(result.priorities[0].name).isEqualTo(priorityName)
        assertThat(result.priorities[0].threshold).isEqualTo(threshold)
        assertThat(result.priorities[0].highlight).isEqualTo(true)
    }

    @Test
    fun `should map all Alert stats fields`() {
        val priority = AlertPriorityType.HIGH
        val count = 10
        val total = 100
        val alertData = AlertStatsData(priority, count)
        val model = AlertStats(total, listOf(alertData))
        val result = MAPPER.toDto(model)

        assertThat(result.items.size).isEqualTo(1)
        assertThat(result.total).isEqualTo(total)
        assertThat(result.items[0].priority).isEqualTo(priority)
        assertThat(result.items[0].count).isEqualTo(count)
    }

    @Test
    fun `should map Alerts fields`() {
        val dateRaised = ZonedDateTime.now(ZoneId.of("UTC"))
        val alert = Alert.builder()
                .alertId(3141)
                .priority(AlertPriorityType.HIGH)
                .dateRaised(dateRaised)
                .type("rejected-central-bank")
                .entities(
                        listOf(Participant.builder()
                                .id("NDEASESSXXX")
                                .name("Nordea")
                                .participantType(DIRECT)
                                .build()
                                )
                        )
                .build()

        val alerts = Page<Alert>(1, listOf(alert))

        val result = MAPPER.toDto(alerts, AlertDto::class.java)

        assertThat(result).isNotNull
        val alertItem: AlertDto = result.items.elementAt(0) as AlertDto
        assertThat(alertItem.alertId).isEqualTo(alert.alertId)
        assertThat(alertItem.priority).isEqualTo(alert.priority)
        assertThat(alertItem.dateRaised).isEqualTo(dateRaised)
        assertThat(alertItem.type).isEqualTo(alert.type)
        assertThat(alertItem.entities[0].name).isEqualTo(alert.entities[0].name)
        assertThat(alertItem.entities[0].participantIdentifier).isEqualTo(alert.entities[0].id)
    }

    @Test
    fun `should map Participant to ParticipantReference`() {
        val participant = Participant.builder()
                .id("participantId")
                .bic("bic")
                .status(ACTIVE)
                .name("name")
                .suspendedTime(null)
                .fundingBic("fundingBic")
                .participantType(DIRECT)
                .build()
        val result = MAPPER.toReferenceDto(participant)

        assertThat(result).isNotNull
        assertThat(result.name).isEqualTo(participant.name)
        assertThat(result.participantIdentifier).isEqualTo(participant.id)
        assertThat(result.connectingParticipantId).isEqualTo(participant.fundingBic)
        assertThat(result.status).isEqualTo(participant.status)
        assertThat(result.participantType).isEqualTo(participant.participantType.description)
    }

    @Test
    fun `should map File fields`() {
        val totalResults = 1
        val file = File.builder()
                .createdDate( ZonedDateTime.now(clock))
                .messageType("message_type")
                .fileName("name")
                .nrOfBatches(12)
                .originator("sender_bic")
                .status("status")
                .build()

        val page = Page<File>(totalResults, listOf(file))
        val result = MAPPER.toDto(page, FileDto::class.java)

        assertThat(result).isNotNull

        val resultItem = result.items[0] as FileDto
        assertThat(result.totalResults).isEqualTo(totalResults)
        assertThat(resultItem.createdAt).isEqualTo(file.createdDate)
        assertThat(resultItem.messageType).isEqualTo(file.messageType)
        assertThat(resultItem.name).isEqualTo(file.fileName)
        assertThat(resultItem.nrOfBatches).isEqualTo(file.nrOfBatches)
        assertThat(resultItem.senderBic).isEqualTo(file.originator)
        assertThat(resultItem.status).isEqualTo(file.status)
    }

    @Test
    fun `should map Batch fields`() {
        val totalResults = 1
        val sender = EnquirySenderDetails.builder()
                .entityBic("sender_bic")
                .build()
        val batch = Batch.builder()
                .fileName("filename")
                .createdAt(
                        ZonedDateTime.of(
                                2020,
                                Month.AUGUST.value,
                                12,
                                12,
                                12,
                                0,
                                0,
                                ZoneId.of("UTC")
                        )
                )

                .messageType("message_type")
                .batchId("id")
                .nrOfTransactions(12)
                .senderBic(sender.entityBic)
                .status("status")
                .build()

        val page = Page<Batch>(totalResults, listOf(batch))
        val result = MAPPER.toDto(page, BatchDto::class.java)

        assertThat(result).isNotNull

        val resultItem = result.items[0] as BatchDto
        assertThat(result.totalResults).isEqualTo(totalResults)
        assertThat(resultItem.id).isEqualTo(batch.batchId)
        assertThat(resultItem.createdAt).isEqualTo(batch.createdAt)
        assertThat(resultItem.senderBic).isEqualTo(batch.senderBic)
        assertThat(resultItem.messageType).isEqualTo(batch.messageType)
        assertThat(resultItem.nrOfTransactions).isEqualTo(batch.nrOfTransactions)
        assertThat(resultItem.status).isEqualTo(batch.status)
    }

    @Test
    fun `should map Batch Details fields`() {
        val batch = Batch.builder()
                .fileName("filename")
                .createdAt(
                        ZonedDateTime.of(
                                2020,
                                Month.AUGUST.value,
                                12,
                                12,
                                12,
                                0,
                                0,
                                ZoneId.of("UTC")
                        )
                )
                .messageType("message_type")
                .batchId("id")
                .nrOfTransactions(12)
                .senderBic("sender_bic")
                .status("status")
                .build()

        val result = MAPPER.toDetailsDto(batch)

        assertThat(result).isNotNull
        assertThat(result.batchId).isEqualTo(batch.batchId)
        assertThat(result.fileName).isEqualTo(batch.fileName)
        assertThat(result.nrOfTransactions).isEqualTo(batch.nrOfTransactions)
        assertThat(result.settlementDate).isEqualTo(batch.settlementDate)
        assertThat(result.settlementCycleId).isEqualTo(batch.settlementCycleId)
        assertThat(result.createdAt).isEqualTo(batch.createdAt)
        assertThat(result.status).isEqualTo(batch.status)
        assertThat(result.reasonCode).isEqualTo(batch.reasonCode)
        assertThat(result.settlementDate).isEqualTo(batch.settlementDate)
        assertThat(result.messageType).isEqualTo(batch.messageType)
        assertThat(result.sender.entityBic).isEqualTo(batch.senderBic)
    }

    @Test
    fun `should map ParticipantSettlementDto fields with no funding Bic`() {
        val settlementDetails = SettlementDetails("participantId", "FORXSES1", "20210322001",
                ZonedDateTime.now(clock), CycleStatus.NO_RESPONSE, 2342667, InstructionStatus.CREATED,
                "counterpartyId", "settlementCounterpartyId", Amount(10.toBigDecimal(), "SEK"),
                Amount(10.toBigDecimal(), "SEK"))
        val cycle = Cycle.builder()
            .id("cycleId")
            .settlementTime(ZonedDateTime.now(clock))
            .cutOffTime(ZonedDateTime.now(clock).plusHours(2))
            .status(CycleStatus.COMPLETED)
            .isNextDayCycle(false)
            .settlementConfirmationTime(ZonedDateTime.now(clock).plusHours(2))
            .totalPositions(emptyList())
            .build()
        val participant = Participant.builder()
            .id("participantId")
            .bic("participantId")
            .name("name")
            .status(ACTIVE)
            .participantType(FUNDED)
            .organizationId("organizationId")
            .build()
        val counterparty = Participant.builder()
            .id("counterpartyId")
            .bic("counterpartyId")
            .name("counterpartyName")
            .fundingBic("fundingBic")
            .status(ACTIVE)
            .participantType(FUNDED)
            .organizationId("organizationId")
            .build()
        val settlementCounterparty = Participant.builder()
            .id("settlementCounterpartyId")
            .bic("settlementCounterpartyId")
            .name("settlementCounterpartyName")
            .fundingBic("fundingBic")
            .status(ACTIVE)
            .participantType(FUNDED)
            .organizationId("organizationId")
            .build()

        val result = MAPPER.toDto(Page(1, listOf(settlementDetails)), listOf(participant, counterparty, settlementCounterparty), participant)

        assertThat(result).isNotNull
        assertThat(result.cycleId).isEqualTo(settlementDetails.cycleId)
        assertThat(result.settlementTime).isEqualTo(cycle.settlementTime)
        assertThat(result.status).isEqualTo(settlementDetails.status)
        assertThat(result.participant.participantIdentifier).isEqualTo(participant.bic)
        assertThat(result.participant.name).isEqualTo(participant.name)
        assertThat(result.participant.connectingParticipantId).isEqualTo(participant.fundingBic)
        assertThat(result.participant.participantType).isEqualTo(participant.participantType.description)
        assertThat(result.instructions.totalResults).isEqualTo(1)

        val instructionResult = result.instructions.items[0] as ParticipantInstructionDto
        assertThat(instructionResult.counterparty.participantIdentifier).isEqualTo(counterparty.bic)
        assertThat(instructionResult.counterparty.name).isEqualTo(counterparty.name)
        assertThat(instructionResult.counterparty.connectingParticipantId).isEqualTo(counterparty.fundingBic)
        assertThat(instructionResult.counterparty.participantType).isEqualTo(counterparty.participantType.description)

        assertThat(instructionResult.settlementCounterparty.participantIdentifier).isEqualTo(
                settlementCounterparty.bic
        )
        assertThat(instructionResult.settlementCounterparty.name).isEqualTo(settlementCounterparty.name)
        assertThat(instructionResult.settlementCounterparty.connectingParticipantId).isEqualTo(
                settlementCounterparty.fundingBic
        )
        assertThat(instructionResult.settlementCounterparty.participantType).isEqualTo(
                settlementCounterparty.participantType.description
        )
    }

    @Test
    fun `should map ParticipantSettlementDto fields with funding Bic`() {
        val settlementDetails = SettlementDetails("participantId", "FORXSES1", "20210322001",
                ZonedDateTime.now(clock), CycleStatus.NO_RESPONSE, 2342667, InstructionStatus.CREATED,
                "counterpartyId", "settlementCounterpartyId", Amount(10.toBigDecimal(), "SEK"),
                Amount(10.toBigDecimal(), "SEK"))

        val cycle = Cycle.builder()
            .id("cycleId")
            .settlementTime(ZonedDateTime.now(clock))
            .cutOffTime(ZonedDateTime.now(clock).plusHours(2))
            .status(CycleStatus.COMPLETED)
            .isNextDayCycle(false)
            .settlementConfirmationTime(ZonedDateTime.now(clock).plusHours(2))
            .totalPositions(emptyList())
            .build()
        val settlementBank = Participant.builder()
            .id("participantId")
            .bic("participantId")
            .name("name")
            .fundingBic("fundingBic")
            .status(ACTIVE)
            .participantType(FUNDING)
            .organizationId("organizationId")
            .build()
        val fundedParticipant = Participant.builder()
            .id("fundingBic")
            .bic("fundingBic")
            .name("name")
            .fundingBic("fundingBic")
            .status(ACTIVE)
            .participantType(FUNDED)
            .organizationId("organizationId")
            .build()
        val counterparty = Participant.builder()
            .id("counterpartyId")
            .bic("counterpartyId")
            .name("counterpartyName")
            .fundingBic("fundingBic")
            .status(ACTIVE)
            .participantType(FUNDED)
            .organizationId("organizationId")
            .build()
        val settlementCounterparty = Participant.builder()
            .id("settlementCounterpartyId")
           .bic("settlementCounterpartyId")
           .name("settlementCounterpartyName")
           .fundingBic("fundingBic")
           .status(ACTIVE)
           .participantType(FUNDED)
           .organizationId("organizationId")
           .build()

        val result = MAPPER.toDto(Page(1, listOf(settlementDetails)),
                listOf(settlementBank, counterparty, settlementCounterparty, fundedParticipant),
                fundedParticipant, settlementBank)

        assertThat(result).isNotNull
        assertThat(result.cycleId).isEqualTo(settlementDetails.cycleId)
        assertThat(result.settlementTime).isEqualTo(cycle.settlementTime)
        assertThat(result.status).isEqualTo(settlementDetails.status)
        assertThat(result.participant.participantIdentifier).isEqualTo(fundedParticipant.bic)
        assertThat(result.participant.name).isEqualTo(fundedParticipant.name)
        assertThat(result.participant.connectingParticipantId).isEqualTo(fundedParticipant.fundingBic)
        assertThat(result.participant.participantType).isEqualTo(fundedParticipant.participantType.description)
        assertThat(result.settlementBank.participantIdentifier).isEqualTo(settlementBank.bic)
        assertThat(result.settlementBank.name).isEqualTo(settlementBank.name)
        assertThat(result.settlementBank.connectingParticipantId).isEqualTo(settlementBank.fundingBic)
        assertThat(result.settlementBank.participantType).isEqualTo(settlementBank.participantType.description)
        assertThat(result.instructions.totalResults).isEqualTo(1)

        val instructionResult = result.instructions.items[0] as ParticipantInstructionDto
        assertThat(instructionResult.counterparty.participantIdentifier).isEqualTo(counterparty.bic)
        assertThat(instructionResult.counterparty.name).isEqualTo(counterparty.name)
        assertThat(instructionResult.counterparty.connectingParticipantId).isEqualTo(counterparty.fundingBic)
        assertThat(instructionResult.counterparty.participantType).isEqualTo(counterparty.participantType.description)

        assertThat(instructionResult.settlementCounterparty.participantIdentifier).isEqualTo(
                settlementCounterparty.bic
        )
        assertThat(instructionResult.settlementCounterparty.name).isEqualTo(settlementCounterparty.name)
        assertThat(instructionResult.settlementCounterparty.connectingParticipantId).isEqualTo(
                settlementCounterparty.fundingBic
        )
        assertThat(instructionResult.settlementCounterparty.participantType).isEqualTo(
                settlementCounterparty.participantType.description
        )
    }

    @Test
    fun `should map TransactionDto fields`() {
        val amount = Amount(BigDecimal.TEN, "SEK")
        val sender = EnquirySenderDetails.builder()
            .entityName("entityName")
            .entityBic("entityBic")
            .iban("iban")
            .fullName("Mark Twain")
            .build()
        val receiver = EnquirySenderDetails.builder()
            .entityName("entityName")
            .entityBic("entityBic")
            .iban("iban")
            .fullName("Bob Sinclair")
            .build()
        val transaction = Transaction(
                "instructionId",
                ZonedDateTime.now(clock),
                "messageType",
                amount,
                "status",
                "fileName",
                "batchId",
                LocalDate.now(clock).plusDays(1),
                "settlementCycleId",
                "reasonCode",
                "senderEntityName",
                "senderEntityBic",
                "senderIban",
                "Mark Twain",
                "receiverEntityName",
                "receiverEntityBic",
                "receiverIban",
                "Tom Hawk"
        )
        val result = MAPPER.toDto(transaction)
        assertThat(result.instructionId).isEqualTo(transaction.instructionId)
        assertThat(result.amount).isEqualTo(transaction.amount.amount)
        assertThat(result.createdAt).isEqualTo(transaction.createdAt)
        assertThat(result.messageType).isEqualTo(transaction.messageType)
        assertThat(result.senderBic).isEqualTo(transaction.senderBic)
        assertThat(result.status).isEqualTo(transaction.status)
    }

    @Test
    fun `should map TransactionDetailsDto fields`() {
        val amount = Amount(BigDecimal.TEN, "SEK")
        val sender = EnquirySenderDetails.builder()
            .entityName("entityName")
            .entityBic("entityBic")
            .iban("iban")
            .fullName("fullName")
            .build()
        val receiver = EnquirySenderDetails.builder()
            .entityName("entityName")
            .entityBic("entityBic")
            .iban("iban")
            .fullName("fullName")
            .build()
        val transaction = Transaction(
                "instructionId",
                ZonedDateTime.now(clock),
                "messageType",
                amount,
                "status",
                "fileName",
                "batchId",
                LocalDate.of(2021, 1, 15),
                "settlementCycleId",
                "reasonCode",
                "senderEntityName",
                "senderEntityBic",
                "senderIban",
                "Mark Twain",
                "receiverEntityName",
                "receiverEntityBic",
                "receiverIban",
                "Tom Hawk"
        )
        val result = MAPPER.toDetailsDto(transaction)
        assertThat(result.instructionId).isEqualTo(transaction.instructionId)
        assertThat(result.amount).isEqualTo(transaction.amount.amount)
        assertThat(result.currency).isEqualTo(transaction.amount.currency)
        assertThat(result.fileName).isEqualTo(transaction.fileName)
        assertThat(result.batchId).isEqualTo(transaction.batchId)
        assertThat(result.settlementDate).isEqualTo(transaction.settlementDate)
        assertThat(result.settlementCycleId).isEqualTo(transaction.settlementCycleId)
        assertThat(result.createdAt).isEqualTo(transaction.createdAt)
        assertThat(result.status).isEqualTo(transaction.status)
        assertThat(result.reasonCode).isEqualTo(transaction.reasonCode)
        assertThat(result.messageType).isEqualTo(transaction.messageType)

        assertThat(result.sender.entityName).isEqualTo(transaction.senderBank)
        assertThat(result.sender.entityBic).isEqualTo(transaction.senderBic)
        assertThat(result.sender.iban).isEqualTo(transaction.senderIBAN)
        assertThat(result.sender.fullName).isEqualTo(transaction.senderFullName)

        assertThat(result.receiver.entityName).isEqualTo(transaction.receiverBank)
        assertThat(result.receiver.entityBic).isEqualTo(transaction.receiverBic)
        assertThat(result.receiver.iban).isEqualTo(transaction.receiverIBAN)
        assertThat(result.receiver.fullName).isEqualTo(transaction.receiverFullName)
    }

    @Test
    fun `should map AlertPriorityDataDto fields`() {
        val entity = AlertPriorityData("name", 234234, true)
        val result = MAPPER.toDto(entity)
        assertThat(result.name).isEqualTo(entity.name)
        assertThat(result.threshold).isEqualTo(entity.threshold)
        assertThat(result.highlight).isEqualTo(entity.highlight)
    }

    @Test
    fun `should map AlertPriorityDataDto fields with null threshold`() {
        val entity = AlertPriorityData("name", null, true)
        val result = MAPPER.toDto(entity)
        assertNull(result.threshold)
    }

    @Test
    fun `should map ParticipantDashboardSettlementDetailsDto with empty settlement position`() {
        val previousCycle = Cycle.builder()
            .id("cycleId")
            .build()
        val currentCycle = Cycle.builder().build()
        val count: Long = 100
        val paymentAmount = Amount(BigDecimal(100), "SEK")
        val payment = Payment(count, paymentAmount)
        val previousPosition = ParticipantPosition.builder()
            .settlementDate(LocalDate.now())
            .participantId("participantId")
            .cycleId("cycleId")
            .currency("currency")
            .paymentSent(payment)
            .paymentReceived(payment)
            .returnReceived(payment)
            .returnSent(payment)
            .netPositionAmount(paymentAmount)
            .build()
        val currentPosition = ParticipantPosition.builder().build()
        val participant = Participant.builder().build()
        val debitCapAmount = Amount(BigDecimal(1000), "SEK")
        val debitPositionAmount = Amount(BigDecimal(2000), "SEK")
        val intraDay = IntraDayPositionGross(
            "schemeId",
            "debitParticipantId",
            LocalDate.now(), debitCapAmount, debitPositionAmount
        )

        val result = MAPPER.toDto(currentCycle, previousCycle, currentPosition, previousPosition,
            participant, participant, intraDay)

        assertThat(result.previousCycle.id).isNotNull()
        assertThat(result.previousPosition.customerCreditTransfer).isNotNull()
        assertThat(result.previousPosition.paymentReturn).isNotNull()
        assertThat(result.previousPositionTotals.totalCredit).isNotNull()
        assertThat(result.previousPositionTotals.totalDebit).isNotNull()
        assertThat(result.previousPositionTotals.totalNetPosition).isNotNull()
        assertThat(result.currentCycle.id).isNull()
        assertThat(result.currentPositionTotals.totalCredit).isNull()
        assertThat(result.currentPositionTotals.totalDebit).isNull()
        assertThat(result.currentPositionTotals.totalNetPosition).isNull()
        assertThat(result.currentPosition.customerCreditTransfer).isNull()
        assertThat(result.currentPosition.paymentReturn).isNull()
    }

    @Test
    fun `should map ParticipantDashboardSettlementDetailsDto fields with null threshold`() {
        val previousCycle = Cycle.builder()
            .id("cycleId")
            .settlementTime(ZonedDateTime.now(clock))
            .cutOffTime(ZonedDateTime.now(clock).plusHours(2))
            .status(CycleStatus.COMPLETED)
            .isNextDayCycle(false)
            .settlementConfirmationTime(ZonedDateTime.now(clock).plusHours(2))
            .totalPositions(emptyList())
            .build()
        val currentCycle = Cycle.builder()
            .id("cycleId")
            .settlementTime(ZonedDateTime.now(clock))
            .cutOffTime(ZonedDateTime.now(clock).plusHours(2))
            .status(CycleStatus.OPEN)
            .isNextDayCycle(false)
            .settlementConfirmationTime(ZonedDateTime.now(clock).plusHours(2))
            .totalPositions(emptyList())
            .build()
        val count: Long = 100
        val paymentSentAmount = Amount(BigDecimal(100), "SEK")
        val paymentSent = Payment(
                count, paymentSentAmount
        )
        val paymentReceivedAmount = Amount(BigDecimal(500), "SEK")
        val paymentReceived = Payment(
                count, paymentReceivedAmount
        )
        val returnSentAmount = Amount(BigDecimal(300), "SEK")
        val returnSent = Payment(
                count, returnSentAmount
        )
        val returnReceivedAmount = Amount(BigDecimal(700), "SEK")
        val returnReceived = Payment(
                count, returnReceivedAmount
        )
        val netPositionAmount = Amount(BigDecimal(5000), "SEK")
        val previousPosition = ParticipantPosition.builder()
            .settlementDate(LocalDate.now(clock))
            .participantId("participantId")
            .cycleId("cycleId")
            .currency("currency")
            .paymentSent(paymentSent)
            .paymentReceived(paymentReceived)
            .returnSent(returnSent)
            .returnReceived(returnReceived)
            .netPositionAmount(netPositionAmount)
            .build()
        val currentPosition = ParticipantPosition.builder()
            .settlementDate(LocalDate.now(clock))
            .participantId("participantId")
            .cycleId("cycleId")
            .currency("currency")
            .paymentSent(paymentSent)
            .paymentReceived(paymentReceived)
            .returnSent(returnSent)
            .returnReceived(returnReceived)
            .netPositionAmount(netPositionAmount)
            .build()

        val debitCapAmount = Amount(BigDecimal(1000), "SEK")
        val debitPositionAmount = Amount(BigDecimal(2000), "SEK")
        val intraDay = IntraDayPositionGross(
                "schemeId",
                "debitParticipantId",
                LocalDate.now(), debitCapAmount, debitPositionAmount
        )
        val participant = Participant.builder()
            .id("participantId")
            .bic("participantId")
            .name("name")
            .fundingBic("fundingBic")
            .status(ACTIVE)
            .participantType(FUNDED)
            .organizationId("organizationId")
            .build()
        val result = MAPPER.toDto(
                currentCycle, previousCycle, currentPosition, previousPosition,
                participant, participant, intraDay
        )
        assertThat(result.participant.bic).isEqualTo(participant.bic)
        assertThat(result.participant.id).isEqualTo(participant.id)
        assertThat(result.participant.name).isEqualTo(participant.name)
        assertThat(result.participant.fundingBic).isEqualTo(participant.fundingBic)
        assertThat(result.participant.suspendedTime).isEqualTo(participant.suspendedTime)
        assertThat(result.participant.participantType).isEqualTo(participant.participantType.description)

        assertThat(result.settlementBank.bic).isEqualTo(participant.bic)
        assertThat(result.settlementBank.id).isEqualTo(participant.id)
        assertThat(result.settlementBank.name).isEqualTo(participant.name)
        assertThat(result.settlementBank.fundingBic).isEqualTo(participant.fundingBic)
        assertThat(result.settlementBank.suspendedTime).isEqualTo(participant.suspendedTime)
        assertThat(result.settlementBank.participantType).isEqualTo(participant.participantType.description)

        assertThat(result.intraDayPositionGross.debitPosition).isEqualTo(intraDay.debitPositionAmount.amount)
        assertThat(result.intraDayPositionGross.debitCap).isEqualTo(intraDay.debitCapAmount.amount)

        assertThat(result.currentCycle.id).isEqualTo(currentCycle.id)
        assertThat(result.currentCycle.settlementTime).isEqualTo(currentCycle.settlementTime)
        assertThat(result.currentCycle.cutOffTime).isEqualTo(currentCycle.cutOffTime)
        assertThat(result.currentCycle.status).isEqualTo(currentCycle.status)

        assertThat(result.previousCycle.id).isEqualTo(previousCycle.id)
        assertThat(result.previousCycle.settlementTime).isEqualTo(previousCycle.settlementTime)
        assertThat(result.previousCycle.cutOffTime).isEqualTo(previousCycle.cutOffTime)
        assertThat(result.previousCycle.status).isEqualTo(previousCycle.status)

        // Current Customer Credit Transfer
        val currentDebitCCT = currentPosition.paymentSent.amount.amount
        assertThat(result.currentPosition.customerCreditTransfer.debit).isEqualTo(currentDebitCCT)

        val currentCreditCCT = currentPosition.paymentReceived.amount.amount
        assertThat(result.currentPosition.customerCreditTransfer.credit).isEqualTo(currentCreditCCT)

        val currentNetCCT = currentCreditCCT.subtract(currentDebitCCT)
        assertThat(result.currentPosition.customerCreditTransfer.netPosition).isEqualTo(
                currentNetCCT
        )

        // Current Payment Return
        val currentDebitPR = currentPosition.returnSent.amount.amount
        assertThat(result.currentPosition.paymentReturn.debit).isEqualTo(currentDebitPR)

        val currentCreditPR = currentPosition.returnReceived.amount.amount
        assertThat(result.currentPosition.paymentReturn.credit).isEqualTo(currentCreditPR)

        val currentNetPR = currentCreditPR.subtract(currentDebitPR)
        assertThat(result.currentPosition.paymentReturn.netPosition).isEqualTo(currentNetPR)

        // Previous Customer Credit Transfer
        val prevDebitCCT = currentPosition.paymentSent.amount.amount
        assertThat(result.currentPosition.customerCreditTransfer.debit).isEqualTo(prevDebitCCT)

        val prevCreditCCT = currentPosition.paymentReceived.amount.amount
        assertThat(result.currentPosition.customerCreditTransfer.credit).isEqualTo(prevCreditCCT)

        val prevNetCCT = prevCreditCCT.subtract(prevDebitCCT)
        assertThat(result.currentPosition.customerCreditTransfer.netPosition).isEqualTo(prevNetCCT)

        // Previous Payment Return
        val prevDebitPR = currentPosition.returnSent.amount.amount
        assertThat(result.currentPosition.paymentReturn.debit).isEqualTo(prevDebitPR)

        val prevCreditPR = currentPosition.returnReceived.amount.amount
        assertThat(result.currentPosition.paymentReturn.credit).isEqualTo(prevCreditPR)

        val prevNetPR = prevCreditPR.subtract(prevDebitPR)
        assertThat(result.currentPosition.paymentReturn.netPosition).isEqualTo(prevNetPR)

        // Current Totals
        val currPaymentCredit = currentPosition.paymentReceived.amount.amount
        val currReturnCredit = currentPosition.returnReceived.amount.amount
        val currTotalCredit = currPaymentCredit.add(currReturnCredit)
        assertThat(result.currentPositionTotals.totalCredit).isEqualTo(currTotalCredit)

        val currPaymentDebit = currentPosition.paymentSent.amount.amount
        val currReturnDebit = currentPosition.returnSent.amount.amount
        val currTotalDebit = currPaymentDebit.add(currReturnDebit)
        assertThat(result.currentPositionTotals.totalDebit).isEqualTo(currTotalDebit)

        assertThat(result.currentPositionTotals.totalNetPosition).isEqualTo(currentPosition.netPositionAmount.amount)

        // Prev Totals
        val prevPaymentCredit = previousPosition.paymentReceived.amount.amount
        val prevReturnCredit = previousPosition.returnReceived.amount.amount
        val prevTotalCredit = prevPaymentCredit.add(prevReturnCredit)
        assertThat(result.previousPositionTotals.totalCredit).isEqualTo(prevTotalCredit)

        val prevPaymentDebit = previousPosition.paymentSent.amount.amount
        val prevReturnDebit = previousPosition.returnSent.amount.amount
        val prevTotalDebit = prevPaymentDebit.add(prevReturnDebit)
        assertThat(result.previousPositionTotals.totalDebit).isEqualTo(prevTotalDebit)

        assertThat(result.previousPositionTotals.totalNetPosition).isEqualTo(previousPosition.netPositionAmount.amount)
    }

    @Test
    fun `should map ApprovalDetails to ApprovalDetailsDto`() {
        val approvalUser = UserDetails.builder()
            .userId("12a514")
            .firstName("John")
            .lastName("Doe")
            .participantId("P27")
            .build()
        val approvalId = "10000020"
        val date = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+1"))
        val requestedChange = mapOf("status" to "suspended")
        val originalData = mapOf("data" to "data")
        val participant = Participant.builder()
                .id("FORXSES1")
                .name("Forex Bank")
                .participantType(FUNDING)
                .schemeCode("P27-SEK")
                .build()
        val approvalDetails = Approval.builder()
            .approvalId(approvalId)
            .requestType(PARTICIPANT_SUSPEND)
            .participantIds(listOf(participant.id))
            .date(date)
            .requestedBy(approvalUser)
            .approvedAt(date)
            .status(APPROVED)
            .approvedBy(approvalUser)
            .requestComment("This is the reason that I...")
            .rejectedBy(approvalUser)
            .rejectedAt(date)
            .originalData(originalData)
            .requestedChange(requestedChange)
            .oldData("hashed data")
            .newData("hashed data")
            .notes("Notes")
            .build()

        val result = MAPPER.toDto(approvalDetails, listOf(participant))

        assertThat(result.status).isEqualTo(APPROVED)
        assertThat(result.requestedBy.name).isEqualTo(approvalUser.firstName + " " + approvalUser.lastName)
        assertThat(result.requestedBy.id).isEqualTo(approvalUser.userId)
        assertThat(result.createdAt).isEqualTo(date)
        assertThat(result.approvedAt).isEqualTo(date)
        assertThat(result.rejectedAt).isEqualTo(date)
        assertThat(result.jobId).isEqualTo(approvalId)
        assertThat(result.requestType).isEqualTo(PARTICIPANT_SUSPEND)
        assertThat(result.participants[0].participantIdentifier).isEqualTo(participant.id)
        assertThat(result.participants[0].name).isEqualTo(participant.name)
        assertThat(result.participants[0].participantType.toString()).isEqualTo(participant.participantType.toString())
        assertThat(result.participants[0].schemeCode).isEqualTo(participant.schemeCode)
        assertThat(result.rejectedBy.name).isEqualTo(approvalUser.firstName + " " + approvalUser.lastName)
        assertThat(result.requestedChange).isEqualTo(requestedChange)
    }

    @ParameterizedTest
    @EnumSource(value = ParticipantType::class, names = ["FUNDING", "DIRECT_FUNDING"])
    fun `should map to ApprovalDetailsDto and sort reference participants by participant type`(
        fundingParticipantType: ParticipantType
    ) {
        val approval = Approval.builder()
            .approvalId("9900000")
            .requestType(PARTICIPANT_SUSPEND)
            .participantIds(listOf("funded1", "funding", "funded2", "funded3"))
            .build()

        val participants = listOf(
                Participant.builder()
                        .id("funded1")
                        .name("aaa")
                        .participantType(FUNDED)
                        .build(),
                Participant.builder()
                        .id("funded2")
                        .name("bbb")
                        .participantType(FUNDED)
                        .build(),
                Participant.builder()
                        .id("funding")
                        .name("ccc")
                        .participantType(fundingParticipantType)
                        .build(),
                Participant.builder()
                        .id("funded3")
                        .name("ddd")
                        .participantType(FUNDED)
                        .build())

        val approvalDto = MAPPER.toDto(approval, participants)

        assertThat(approvalDto.participants.map{e -> e.name}.toList())
            .containsExactly("ccc", "aaa", "bbb", "ddd")
        assertThat(approvalDto.participants.map{e -> e.participantIdentifier}.toList())
            .containsExactly("funding", "funded1", "funded2", "funded3")
        assertThat(approvalDto.participants.map{e -> e.participantType}.toList())
            .containsExactly(fundingParticipantType.description, FUNDED.description,
                FUNDED.description, FUNDED.description)
    }

    @ParameterizedTest
    @EnumSource(value = ParticipantType::class, names = ["FUNDING", "DIRECT_FUNDING"])
    fun `should map to ApprovalDetailsDto and return only funded participant if suspension level is SELF`(
        participantType: ParticipantType
    ) {
        val approval = Approval.builder()
            .approvalId("9900000")
            .requestType(PARTICIPANT_SUSPEND)
            .participantIds(listOf("funding", "funded0", "funded1"))
            .build()

        val participants = listOf(
            Participant.builder()
                .id("funded")
                .name("Funded participant0")
                .participantType(FUNDED)
                .build(),
            Participant.builder()
                .id("funding")
                .name("Funding participant")
                .participantType(participantType)
                .suspensionLevel(SELF)
                .build(),
            Participant.builder()
                .id("funded1")
                .name("Funded participant1")
                .participantType(FUNDED)
                .build()
        )

        val approvalDto = MAPPER.toDto(approval, participants)

        assertThat(approvalDto.suspensionLevel).isEqualTo(SELF)
        assertThat(approvalDto.participants.size).isEqualTo(1)
        assertThat(approvalDto.participants[0].name).isEqualTo("Funding participant")
        assertThat(approvalDto.participants[0].participantIdentifier).isEqualTo("funding")
        assertThat(approvalDto.participants[0].participantType).isEqualTo(participantType.description)
    }

    @Test
    fun `should map all fields of Participant to ManagedParticipantDto`() {
        val date = ZonedDateTime.now()
        val fundedParticipant = Participant.builder()
                .id("ELLFSESS")
                .bic("ELLFSESS")
                .fundingBic("FORXSES1")
                .name("Lansfosakringar Bank")
                .participantType(FUNDED)
                .build()
        val participant = Participant.builder()
                .id("FORXSES1")
                .bic("FORXSES1")
                .name("Forex Bank")
                .fundingBic("NA")
                .status(ACTIVE)
                .participantType(FUNDING)
                .organizationId("00002121")
                .suspensionLevel(SCHEME)
                .tpspName("Nordnet Bank")
                .tpspId("475347837892")
                .fundedParticipants(listOf(fundedParticipant))
                .fundedParticipantsCount(1)
                .build()
        val participants = Page<Participant>(1, listOf(participant, fundedParticipant))
        val routingRecords = RoutingRecord(
                "reachableBic",
                ZonedDateTime.now(ZoneId.of("UTC")),
                ZonedDateTime.now(ZoneId.of("UTC")),
                "currency"
        )
        participant.reachableBics = listOf(routingRecords)

        val userDetails = UserDetails.builder()
                .firstName("John")
                .lastName("Doe")
                .build()

        val approval = Approval.builder()
                .approvalId("1000015")
                .requestType(PARTICIPANT_SUSPEND)
                .date(date)
                .requestedBy(userDetails)
                .build()

        val result = MAPPER.toDto(participants, mapOf("FORXSES1" to approval), mapOf("FORXSES1" to "Forex Bank"))

        assertThat(result).isNotNull

        val managedParticipant: ManagedParticipantDto =
                result.items.elementAt(0) as ManagedParticipantDto

        val fundedManagedParticipant: ManagedParticipantDto =
                result.items.elementAt(1) as ManagedParticipantDto

        assertThat(managedParticipant.bic).isEqualTo(participant.bic)
        assertThat(managedParticipant.name).isEqualTo(participant.name)
        assertThat(managedParticipant.id).isEqualTo(participant.id)
        assertThat(managedParticipant.status).isEqualTo(participant.status)
        assertThat(managedParticipant.suspendedTime).isEqualTo(participant.suspendedTime)
        assertThat(managedParticipant.participantType).isEqualTo(participant.participantType.description)
        assertThat(managedParticipant.organizationId).isEqualTo(participant.organizationId)
        assertThat(managedParticipant.tpspName).isEqualTo(participant.tpspName)
        assertThat(managedParticipant.tpspId).isEqualTo(participant.tpspId)
        assertThat(managedParticipant.fundedParticipants[0].name).isEqualTo(participant.fundedParticipants[0].name)
        assertThat(managedParticipant.fundedParticipants[0].schemeCode).isEqualTo(participant.fundedParticipants[0].schemeCode)
        assertThat(managedParticipant.fundedParticipants[0].connectingParticipantId).isEqualTo(
                participant.fundedParticipants[0].fundingBic)
        assertThat(managedParticipant.fundedParticipants[0].participantIdentifier).isEqualTo(
                participant.fundedParticipants[0].id)
        assertThat(managedParticipant.fundedParticipants[0].participantType).isEqualTo(
                participant.fundedParticipants[0].participantType.description)
        assertThat(managedParticipant.fundedParticipantsCount).isEqualTo(participant.fundedParticipantsCount)
        assertThat(managedParticipant.reachableBics[0].reachableBic).isEqualTo(participant.reachableBics[0].reachableBic)
        assertThat(managedParticipant.reachableBics[0].validFrom).isEqualTo(participant.reachableBics[0].validFrom)
        assertThat(managedParticipant.reachableBics[0].validTo).isEqualTo(participant.reachableBics[0].validTo)
        assertThat(managedParticipant.reachableBics[0].currency).isEqualTo(participant.reachableBics[0].currency)
        assertThat(managedParticipant.approvalReference).isNotNull
        assertThat(managedParticipant.approvalReference.jobId).isEqualTo(approval.approvalId)
        assertThat(managedParticipant.approvalReference.requestType).isEqualTo(approval.requestType)
        assertThat(managedParticipant.approvalReference.requestedAt).isEqualTo(approval.date)
        assertThat(managedParticipant.approvalReference.requestedBy).isEqualTo(approval.requestedBy.fullName)

        assertThat(fundedManagedParticipant.bic).isEqualTo(fundedParticipant.bic)
        assertThat(fundedManagedParticipant.fundingBic).isEqualTo(participant.bic)
        assertThat(fundedManagedParticipant.fundingName).isEqualTo(participant.name)
        assertThat(fundedManagedParticipant.id).isEqualTo(fundedParticipant.id)
        assertThat(fundedManagedParticipant.participantType).isEqualTo(FUNDED.description)
    }

    @Test
    fun `should map RoutingRecordDto fields`() {
        val entity = RoutingRecord(
                "reachableBic",
                ZonedDateTime.now(clock),
                ZonedDateTime.now(clock),
                "currency"
        )
        val result = MAPPER.toDto(entity)
        assertThat(result.reachableBic).isEqualTo(entity.reachableBic)
        assertThat(result.validFrom).isEqualTo(entity.validFrom)
        assertThat(result.validTo).isEqualTo(entity.validTo)
        assertThat(result.currency).isEqualTo(entity.currency)
    }

    @Test
    fun `should map to ManagedParticipantDetailsDto fields`() {
        val participantId = "FORXSES1"

        val participant = Participant.builder()
                .id(participantId)
                .bic(participantId)
                .name("Forex Bank")
                .status(ACTIVE)
                .participantType(FUNDED)
                .organizationId("00002121")
                .tpspName("Nordnet Bank")
                .tpspId("475347837892")
                .fundedParticipants(emptyList())
                .fundedParticipantsCount(1)
                .build()

        val approvalUser = UserDetails.builder()
            .userId("E23423")
            .firstName("John")
            .lastName("Doe")
            .participantId(participantId)
            .build()

        val approval = Approval.builder()
                .approvalId("approvalId")
                .requestType(PARTICIPANT_SUSPEND)
                .participantIds(listOf(participantId))
                .date(ZonedDateTime.now(ZoneId.of("UTC")))
                .requestedBy(approvalUser)
                .status(PENDING)
                .requestComment("comment")
                .notes("notes")
                .build()

        val approvals = mapOf(participantId to approval)

        participant.fundedParticipants = listOf(participant)
        val fundingParticipant = Participant.builder()
            .id("participantId")
            .bic("participantId")
            .name("name")
            .fundingBic("fundingBic")
            .status(ACTIVE)
            .participantType(FUNDING)
            .organizationId("organizationId")
            .build()
        val configuration = ParticipantConfiguration.builder()
            .schemeParticipantIdentifier("schemeParticipantIdentifier")
            .txnVolume(10)
            .outputFileTimeLimit(10)
            .networkName("networkName")
            .gatewayName("gatewayName")
            .requestorDN("requestorDN")
            .responderDN("responderDN")
            .preSettlementAckType("preSettlementAckType")
            .preSettlementActGenerationLevel("preSettlementActGenerationLevel")
            .postSettlementAckType("postSettlementAckType")
            .postSettlementAckGenerationLevel("postSettlementAckGenerationLevel")
            .debitCapLimit(BigDecimal.ONE)
            .debitCapLimitThresholds(listOf(0.12, 0.25))
            .updatedAt(ZonedDateTime.now(clock))
            .updatedBy(approvalUser)
            .build()
        val account = Account("partyCode", 234, "iban")

        val result = MAPPER.toDto(participant, configuration, fundingParticipant, account, approvals)

        assertThat(result.bic).isEqualTo(participant.bic)
        assertThat(result.name).isEqualTo(participant.name)
        assertThat(result.id).isEqualTo(participant.id)
        assertThat(result.status).isEqualTo(participant.status)
        assertThat(result.suspendedTime).isEqualTo(participant.suspendedTime)
        assertThat(result.participantType).isEqualTo(participant.participantType.description)
        assertThat(result.organizationId).isEqualTo(participant.organizationId)
        assertThat(result.hasActiveSuspensionRequests).isEqualTo(true)
        assertThat(result.tpspName).isEqualTo(participant.tpspName)
        assertThat(result.tpspId).isEqualTo(participant.tpspId)

        assertThat(result.fundedParticipants[0].name).isEqualTo(participant.fundedParticipants[0].name)
        assertThat(result.fundedParticipants[0].schemeCode).isEqualTo(participant.fundedParticipants[0].schemeCode)
        assertThat(result.fundedParticipants[0].connectingParticipantId).isEqualTo(
                participant.fundedParticipants[0].fundingBic
        )
        assertThat(result.fundedParticipants[0].participantIdentifier).isEqualTo(
                participant.fundedParticipants[0].id
        )
        assertThat(result.fundedParticipants[0].participantType).isEqualTo(
                participant.fundedParticipants[0].participantType.description
        )

        assertThat(result.fundingParticipant.connectingParticipantId).isEqualTo(fundingParticipant.fundingBic)
        assertThat(result.fundingParticipant.participantIdentifier).isEqualTo(fundingParticipant.bic)
        assertThat(result.fundingParticipant.name).isEqualTo(fundingParticipant.name)
        assertThat(result.fundingParticipant.participantType).isEqualTo(fundingParticipant.participantType.description)
        assertThat(result.fundingParticipant.schemeCode).isEqualTo(fundingParticipant.schemeCode)

        assertThat(result.outputTxnVolume).isEqualTo(configuration.txnVolume)
        assertThat(result.outputTxnTimeLimit).isEqualTo(configuration.outputFileTimeLimit)
        assertThat(result.debitCapLimit).isEqualTo(configuration.debitCapLimit)
        assertThat(result.debitCapLimitThresholds).isEqualTo(configuration.debitCapLimitThresholds)
        assertThat(result.settlementAccountNo).isEqualTo(account.accountNo.toString())

        assertThat(result.updatedAt).isEqualTo(configuration.updatedAt)
        assertThat(result.updatedBy.id).isEqualTo(approvalUser.userId)
        assertThat(result.updatedBy.name).isEqualTo(approvalUser.firstName + " " + approvalUser.lastName)
        assertThat(result.updatedBy.participantName).isEqualTo(approvalUser.participantId)

        assertThat(result.approvalReference.requestType).isEqualTo(approval.requestType)
        assertThat(result.approvalReference.requestedBy).isEqualTo(approval.requestedBy.fullName)
        assertThat(result.approvalReference.requestedAt).isEqualTo(approval.date)
        assertThat(result.approvalReference.jobId).isEqualTo(approval.approvalId)
    }

    @Test
    fun `should map ReportDto fields`() {
        val report = Report(
                "10000000006",
                "PRE-SETTLEMENT_ADVICE",
                ZonedDateTime.parse("2021-02-14T00:00:00Z"),
                "20201231002",
                "IBCASES1",
                "ICA Banken"
        )
        val result = MAPPER.toDto(report)
        assertThat(result.reportId).isEqualTo(report.reportId)
        assertThat(result.reportType).isEqualTo(report.reportType)
        assertThat(result.createdAt).isEqualTo(report.createdAt)
        assertThat(result.cycleId).isEqualTo(report.cycleId)
        assertThat(result.participantIdentifier).isEqualTo(report.participantIdentifier)
        assertThat(result.participantName).isEqualTo(report.participantName)
    }

    @Test
    fun `should map ApprovalConfirmationResponseDto fields`() {
        val entity = ApprovalConfirmationResponse("response")
        val result = MAPPER.toDto(entity)
        assertThat(result.responseMessage).isEqualTo(entity.responseMessage)
    }

    @Test
    fun `should map DayCycleDto fields`() {
        val entity = DayCycle(
                "cycleCode",
                "sessionCode",
                "sessionInstanceId",
                CycleStatus.COMPLETED,
                ZonedDateTime.now(ZoneId.of("UTC")),
                ZonedDateTime.now(ZoneId.of("UTC"))
        )
        val result = MAPPER.toDto(entity)
        assertThat(result.id).isEqualTo(entity.sessionInstanceId)
        assertThat(result.sessionCode).isEqualTo(entity.sessionCode)
        assertThat(result.status).isEqualTo(entity.status)
    }

    @Test
    fun `should map ConfigurationDto fields`() {
        val entity = Configuration("P27-SEK", "SEK", 2)
        val dataRetentionDays = 30
        val timeZone = "CET"

        val result = MAPPER.toDto(entity, dataRetentionDays, timeZone)

        assertThat(result.scheme).isEqualTo(entity.scheme)
        assertThat(result.schemeCurrency).isEqualTo(entity.schemeCurrency)
        assertThat(result.dataRetentionDays).isEqualTo(dataRetentionDays)
        assertThat(result.ioDetailsThreshold).isEqualTo(entity.ioDetailsThreshold)
        assertThat(result.timeZone).isEqualTo(timeZone)
    }

    @Test
    fun `should map SettlementCycleScheduleDto fields`() {
        val entity = SettlementCycleSchedule(
                "cycleName",
                "startTime",
                "cutOffTime",
                "settlementStartTime"
        )
        val dto = MAPPER.toDto(entity)
        assertThat(dto.cycleName).isEqualTo(entity.cycleName)
        assertThat(dto.startTime).isEqualTo(entity.startTime)
        assertThat(dto.cutOffTime).isEqualTo(entity.cutOffTime)
        assertThat(dto.settlementStartTime).isEqualTo(entity.settlementStartTime)
    }

    @Test
    fun `should map UserPermissionDto from participant user and permissions list`() {
        val userId = "12a511"
        val userFirstName = "Peter"
        val userLastName = "Brooks"
        val participantId = "HANDSESS"
        val fundingBic = "NDEASESSXXX"
        val participantName = "Svenska Handelsbanken"
        val dateTime = ZonedDateTime.now(ZoneId.of("UTC"))
        val permission = "read.alerts-dashboard"
        val participant = Participant.builder()
                .id(participantId)
                .bic(participantId)
                .name(participantName)
                .fundingBic(fundingBic)
                .status(SUSPENDED)
                .suspendedTime(dateTime)
                .participantType(FUNDED)
                .build()
        val auditDetails = AuditDetails.builder()
                .username(userId)
                .firstName(userFirstName)
                .lastName(userLastName)
                .build()

        val entity = MAPPER.toDto(participant, listOf(permission), auditDetails)

        assertThat(entity).isNotNull
        assertThat(entity.permissions.size).isEqualTo(1)
        assertThat(entity.permissions[0]).isEqualTo(permission)
        assertThat(entity.participation.id).isEqualTo(participantId)
        assertThat(entity.participation.bic).isEqualTo(participantId)
        assertThat(entity.participation.name).isEqualTo(participantName)
        assertThat(entity.participation.fundingBic).isEqualTo(fundingBic)
        assertThat(entity.participation.status).isEqualTo(SUSPENDED)
        assertThat(entity.participation.suspendedTime).isEqualTo(dateTime)
        assertThat(entity.user.userId).isEqualTo(userId)
        assertThat(entity.user.name).isEqualTo("$userFirstName $userLastName")
    }

    @Test
    fun `should map all fields from Participant to ManagedParticipantDto`() {
        val userDetail = UserDetails.builder()
                .firstName("John")
                .lastName("Doe")
                .build()
        val approval = Approval.builder()
                .requestType(PARTICIPANT_SUSPEND)
                .requestedBy(userDetail)
                .date(ZonedDateTime.now())
                .approvalId("10000000")
                .build()
        val routingRecord = RoutingRecord(
                "reachableBic",
                ZonedDateTime.now(ZoneId.of("UTC")),
                ZonedDateTime.now(ZoneId.of("UTC")),
                "currency")
        val fundedParticipant = Participant.builder()
                .id("FORXSES1")
                .bic("FORXSES1")
                .build()
        val participant = Participant.builder()
                .id("FORXSES1")
                .bic("FORXSES1")
                .name("Forex Bank")
                .fundingBic("NA")
                .status(ACTIVE)
                .suspendedTime(ZonedDateTime.now())
                .participantType(FUNDING)
                .schemeCode("P27-SEK")
                .organizationId("00002121")
                .suspensionLevel(SCHEME)
                .tpspName("Nordnet Bank")
                .tpspId("475347837892")
                .fundedParticipants(listOf(fundedParticipant))
                .fundedParticipantsCount(1)
                .reachableBics(listOf(routingRecord))
                .build()

        val result = MAPPER.toDto(participant, mapOf(participant.id to approval), mapOf("FORXSES1" to "Forex Bank"))

        assertThat(result.bic).isEqualTo(participant.bic)
        assertThat(result.fundingBic).isEqualTo(participant.fundingBic)
        assertThat(result.id).isEqualTo(participant.id)
        assertThat(result.name).isEqualTo(participant.name)
        assertThat(result.status).isEqualTo(participant.status)
        assertThat(result.suspendedTime).isEqualTo(participant.suspendedTime)
        assertThat(result.participantType).isEqualTo(participant.participantType.description)
        assertThat(result.organizationId).isEqualTo(participant.organizationId)
        assertThat(result.suspensionLevel).isEqualTo(participant.suspensionLevel)
        assertThat(result.tpspName).isEqualTo(participant.tpspName)
        assertThat(result.tpspId).isEqualTo(participant.tpspId)
        assertThat(result.fundedParticipantsCount).isEqualTo(participant.fundedParticipantsCount)
        assertThat(result.reachableBics).isEqualTo(participant.reachableBics)
        assertThat(result.approvalReference.jobId).isEqualTo(approval.approvalId)
        assertThat(result.approvalReference.requestedAt).isEqualTo(approval.date)
        assertThat(result.approvalReference.requestType).isEqualTo(approval.requestType)
        assertThat(result.approvalReference.requestedBy).isEqualTo(approval.requestedBy.fullName)
    }

    @Test
    fun `should map to IODashboardDto, populating with empty if no rows for participant`() {
        val forex = Participant.builder()
                .id("FORXSES1")
                .bic("FORXSES1")
                .name("Forex Bank")
                .fundingBic("NA")
                .status(ACTIVE)
                .suspendedTime(ZonedDateTime.now())
                .participantType(FUNDING)
                .schemeCode("P27-SEK")
                .organizationId("00002121")
                .suspensionLevel(SCHEME)
                .build()
        val ndeases = Participant.builder()
                .id("NDEASESXXX")
                .bic("NDEASESXXX")
                .name("NDEA Bank")
                .fundingBic("NA")
                .status(ACTIVE)
                .suspendedTime(ZonedDateTime.now())
                .participantType(FUNDING)
                .schemeCode("P27-SEK")
                .organizationId("00002121")
                .suspensionLevel(SCHEME)
                .build()
        val ioData = IOData.builder()
                .submitted(1)
                .rejected("1.0")
                .output(100)
                .build()
        val participantIoData = listOf(
                ParticipantIOData.builder()
                        .batches(ioData)
                        .transactions(ioData)
                        .files(ioData)
                        .schemeParticipantIdentifier(forex.bic)
                        .build(),
                ParticipantIOData.builder()
                        .batches(ioData)
                        .transactions(ioData)
                        .files(ioData)
                        .schemeParticipantIdentifier("-")
                        .build()
        )
        val ioDashboard = IODashboard(
                "file_rej", "batch_rej", "transaction_rej", participantIoData
        )

        val result = MAPPER.toDto(ioDashboard, listOf(forex, ndeases), LocalDate.now())

        assertThat(result.rows.size).isEqualTo(2)
        assertThat(result.batchesRejected).isEqualTo("batch_rej")
        assertThat(result.filesRejected).isEqualTo("file_rej")
        assertThat(result.transactionsRejected).isEqualTo("transaction_rej")
        assertThat(result.rows[0].batches.output).isEqualTo(ioData.output)
        assertThat(result.rows[0].batches.rejected).isEqualTo(1.0)
        assertThat(result.rows[0].batches.submitted).isEqualTo(ioData.submitted)
        assertThat(result.rows[0].files.output).isEqualTo(ioData.output)
        assertThat(result.rows[0].files.rejected).isEqualTo(1.0)
        assertThat(result.rows[0].files.submitted).isEqualTo(ioData.submitted)
        assertThat(result.rows[0].transactions.output).isEqualTo(ioData.output)
        assertThat(result.rows[0].transactions.rejected).isEqualTo(1.0)
        assertThat(result.rows[0].transactions.submitted).isEqualTo(ioData.submitted)
        assertThat(result.rows[1].batches).isNull()
        assertThat(result.rows[1].files).isNull()
        assertThat(result.rows[1].transactions).isNull()
    }

    @Test
    fun `should map all FileDetailsDto fields`() {
        val file = File(
                "name",
                "fileName",
                234234,
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                "originator",
                "msgType",
                10,
                "status",
                "reasonCode",
                "cycleId",
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                "P27"
        )
        val participant = Participant.builder()
                .id("participantId")
                .bic("participantId")
                .name("name")
                .fundingBic("fundingBic")
                .status(ACTIVE)
                .participantType(FUNDING)
                .organizationId("organizationId")
                .build()

        val dto = MAPPER.toDto(file, participant)
        assertThat(dto.fileSize).isEqualTo(file.fileSize)
        assertThat(dto.createdAt).isEqualTo(file.createdDate)
        assertThat(dto.fileName).isEqualTo(file.fileName)
        assertThat(dto.messageType).isEqualTo(file.messageType)
        assertThat(dto.nrOfBatches).isEqualTo(file.nrOfBatches)
        assertThat(dto.reasonCode).isEqualTo(file.reasonCode)
        assertThat(dto.settlementCycleId).isEqualTo(file.settlementCycle)
        assertThat(dto.status).isEqualTo(file.status)
        assertThat(dto.sender.entityName).isEqualTo(participant.name)
        assertThat(dto.sender.entityBic).isEqualTo(participant.bic)
    }
}

