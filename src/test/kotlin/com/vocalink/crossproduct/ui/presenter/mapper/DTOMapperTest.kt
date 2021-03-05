package com.vocalink.crossproduct.ui.presenter.mapper

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
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.domain.approval.ApprovalStatus
import com.vocalink.crossproduct.domain.approval.ApprovalUser
import com.vocalink.crossproduct.domain.batch.Batch
import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.cycle.DayCycle
import com.vocalink.crossproduct.domain.files.EnquirySenderDetails
import com.vocalink.crossproduct.domain.files.File
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantConfiguration
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross
import com.vocalink.crossproduct.domain.position.ParticipantPosition
import com.vocalink.crossproduct.domain.position.Payment
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference
import com.vocalink.crossproduct.domain.reference.ParticipantReference
import com.vocalink.crossproduct.domain.report.Report
import com.vocalink.crossproduct.domain.routing.RoutingRecord
import com.vocalink.crossproduct.domain.settlement.ParticipantInstruction
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement
import com.vocalink.crossproduct.domain.settlement.SettlementStatus
import com.vocalink.crossproduct.domain.transaction.Transaction
import com.vocalink.crossproduct.ui.dto.alert.AlertDto
import com.vocalink.crossproduct.ui.dto.batch.BatchDto
import com.vocalink.crossproduct.ui.dto.file.FileDto
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantInstructionDto
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class DTOMapperTest {

    @Test
    fun `should map all fields`() {
        val id = "anyId"
        val cutoffTime =
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC"))
        val settlementTime =
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC"))
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
    fun `should map all Message references fields and set isDefault to false`() {
        val sending = "Sending"
        val type = "some_type"
        val model = MessageDirectionReference.builder()
                .name(sending)
                .types(listOf(type))
                .build()

        val result = MAPPER.toDto(model)

        assertEquals(sending, result.name)
        assertEquals(type, result.types[0])
        assertFalse(result.isDefault)
    }

    @Test
    fun `should map Alerts fields`() {
        val dateRaised = ZonedDateTime.now()
        val alert = Alert.builder()
                .alertId(3141)
                .priority(AlertPriorityType.HIGH)
                .dateRaised(dateRaised)
                .type("rejected-central-bank")
                .entities(
                        listOf(
                                ParticipantReference(
                                        "NDEASESSXXX", "Nordea",
                                        ParticipantType.DIRECT, null, null
                                )
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
        assertThat(alertItem.entities[0].participantIdentifier).isEqualTo(alert.entities[0].participantIdentifier)
    }

    @Test
    fun `should map Participant to ParticipantReference`() {
        val participant = Participant.builder()
                .id("participantId")
                .bic("bic")
                .status(ParticipantStatus.ACTIVE)
                .name("name")
                .suspendedTime(null)
                .fundingBic("fundingBic")
                .build()
        val result = MAPPER.toReference(participant)

        assertThat(result).isNotNull
        assertThat(result.name).isEqualTo(participant.name)
        assertThat(result.participantIdentifier).isEqualTo(participant.id)
    }

    @Test
    fun `should map File fields`() {
        val totalResults = 1
        val file = File.builder()
                .createdDate(
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
                .sender(sender.entityBic)
                .status("status")
                .build()

        val page = Page<Batch>(totalResults, listOf(batch))
        val result = MAPPER.toDto(page, BatchDto::class.java)

        assertThat(result).isNotNull

        val resultItem = result.items[0] as BatchDto
        assertThat(result.totalResults).isEqualTo(totalResults)
        assertThat(resultItem.id).isEqualTo(batch.batchId)
        assertThat(resultItem.createdAt).isEqualTo(batch.createdAt)
        assertThat(resultItem.senderBic).isEqualTo(batch.sender)
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
                .sender("sender_bic")
                .status("status")
                .build()

        val participant = Participant.builder().id("id").build()

        val file = File.builder().fileSize(1).build()

        val result = MAPPER.toDetailsDto(batch, participant, file)

        assertThat(result).isNotNull
        assertThat(result.batchId).isEqualTo(batch.batchId)
        assertThat(result.fileName).isEqualTo(batch.fileName)
        assertThat(result.nrOfTransactions).isEqualTo(batch.nrOfTransactions)
        assertThat(result.fileSize).isEqualTo(file.fileSize)
        assertThat(result.settlementDate).isEqualTo(batch.settlementDate)
        assertThat(result.settlementCycleId).isEqualTo(batch.settlementCycleId)
        assertThat(result.createdAt).isEqualTo(batch.createdAt)
        assertThat(result.status).isEqualTo(batch.status)
        assertThat(result.reasonCode).isEqualTo(batch.reasonCode)
        assertThat(result.settlementDate).isEqualTo(batch.settlementDate)
        assertThat(result.messageType).isEqualTo(batch.messageType)
        assertThat(result.sender.entityBic).isEqualTo(participant.bic)
    }

    @Test
    fun `should map ParticipantSettlementDto fields`() {
        val instruction = ParticipantInstruction(
                "cycleId", "participantId", "reference", "ACCEPTED",
                "counterpartyId", "settlementCounterpartyId",
                BigDecimal.TEN, BigDecimal.TEN
        )
        val settlement = ParticipantSettlement(
                "cycleId", ZonedDateTime.of(2020, 10, 10, 10, 10, 10, 0, ZoneId.of("UTC")),
                SettlementStatus.PARTIAL, "participantId", Page(1, listOf(instruction))
        )
        val cycle = Cycle(
                "cycleId",
                ZonedDateTime.of(2020, 10, 10, 10, 10, 10, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, 10, 10, 12, 10, 10, 0, ZoneId.of("UTC")),
                CycleStatus.COMPLETED,
                false,
                ZonedDateTime.of(2020, 10, 10, 12, 10, 10, 0, ZoneId.of("UTC")),
                emptyList()
        )
        val participant = Participant(
                "participantId",
                "participantId",
                "name",
                "fundingBic",
                ParticipantStatus.ACTIVE,
                null,
                ParticipantType.FUNDED,
                null,
                "organizationId",
                null,
                null,
                null,
                null,
                null
        )
        val counterparty = Participant(
                "counterpartyId",
                "counterpartyId",
                "counterpartyName",
                "fundingBic",
                ParticipantStatus.ACTIVE,
                null,
                ParticipantType.FUNDED,
                null,
                "organizationId",
                null,
                null,
                null,
                null,
                null
        )
        val settlementCounterparty = Participant(
                "settlementCounterpartyId",
                "settlementCounterpartyId",
                "settlementCounterpartyName",
                "fundingBic",
                ParticipantStatus.ACTIVE,
                null,
                ParticipantType.FUNDED,
                null,
                "organizationId",
                null,
                null,
                null,
                null,
                null
        )

        val result = MAPPER.toDto(settlement, listOf(participant, counterparty, settlementCounterparty))

        assertThat(result).isNotNull
        assertThat(result.cycleId).isEqualTo(settlement.cycleId)
        assertThat(result.settlementTime).isEqualTo(cycle.settlementTime)
        assertThat(result.status).isEqualTo(settlement.status)
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
    fun `should map TransactionDto fields`() {
        val amount = Amount(BigDecimal.TEN, "SEK")
        val transaction = Transaction(
                "instructionId",
                amount,
                "fileName",
                "batchId",
                "senderEntityBic",
                LocalDate.of(2021, 1, 15),
                "receiverEntityBic",
                LocalDate.of(2021, 1, 15),
                "settlementCycleId",
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                "status",
                "reasonCode",
                "messageType",
                "senderEntityBic"
        )
        val result = MAPPER.toDto(transaction)
        assertThat(result.instructionId).isEqualTo(transaction.instructionId)
        assertThat(result.amount).isEqualTo(transaction.amount.amount)
        assertThat(result.createdAt).isEqualTo(transaction.createdAt)
        assertThat(result.messageType).isEqualTo(transaction.messageType)
        assertThat(result.senderBic).isEqualTo(transaction.senderParticipantIdentifier)
        assertThat(result.status).isEqualTo(transaction.status)
    }

    @Test
    fun `should map TransactionDetailsDto fields`() {
        val amount = Amount(BigDecimal.TEN, "SEK")
        val sender = EnquirySenderDetails(
                "entityName",
                "entityBic",
                "iban",
                null
        )
        val receiver = EnquirySenderDetails(
                "entityName",
                "entityBic",
                "iban",
                null
        )
        val transaction = Transaction(
                "instructionId",
                amount,
                "fileName",
                "batchId",
                "originator",
                LocalDate.of(2021, 1, 15),
                "receiverEntityBic",
                LocalDate.of(2021, 1, 15),
                "settlementCycleId",
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                "status",
                "reasonCode",
                "messageType",
                "senderEntityBic"
        )
        val result = MAPPER.toDetailsDto(transaction, sender, receiver)
        assertThat(result.instructionId).isEqualTo(transaction.instructionId)
        assertThat(result.amount).isEqualTo(transaction.amount.amount)
        assertThat(result.currency).isEqualTo(transaction.amount.currency)
        assertThat(result.fileName).isEqualTo(transaction.fileName)
        assertThat(result.batchId).isEqualTo(transaction.batchId)
        assertThat(result.valueDate).isEqualTo(transaction.valueDate)
        assertThat(result.settlementDate).isEqualTo(transaction.settlementDate)
        assertThat(result.settlementCycleId).isEqualTo(transaction.settlementCycleId)
        assertThat(result.createdAt).isEqualTo(transaction.createdAt)
        assertThat(result.status).isEqualTo(transaction.status)
        assertThat(result.reasonCode).isEqualTo(transaction.reasonCode)
        assertThat(result.messageType).isEqualTo(transaction.messageType)

        assertThat(result.sender.entityName).isEqualTo(sender.entityName)
        assertThat(result.sender.entityBic).isEqualTo(sender.entityBic)
        assertThat(result.sender.iban).isEqualTo(sender.iban)

        assertThat(result.receiver.entityName).isEqualTo(receiver.entityName)
        assertThat(result.receiver.entityBic).isEqualTo(receiver.entityBic)
        assertThat(result.receiver.iban).isEqualTo(receiver.iban)
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
    fun `should map ParticipantDashboardSettlementDetailsDto fields with null threshold`() {
        val previousCycle = Cycle(
                "cycleId",
                ZonedDateTime.of(2020, 10, 10, 10, 10, 10, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, 10, 10, 12, 10, 10, 0, ZoneId.of("UTC")),
                CycleStatus.COMPLETED,
                false,
                ZonedDateTime.of(2020, 10, 10, 12, 10, 10, 0, ZoneId.of("UTC")),
                emptyList()
        )
        val currentCycle = Cycle(
                "cycleId",
                ZonedDateTime.of(2020, 10, 10, 10, 10, 10, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, 10, 10, 12, 10, 10, 0, ZoneId.of("UTC")),
                CycleStatus.OPEN,
                false,
                ZonedDateTime.of(2020, 10, 10, 12, 10, 10, 0, ZoneId.of("UTC")),
                emptyList()
        )
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

        val previousPosition = ParticipantPosition(
                LocalDate.now(),
                "participantId",
                "cycleId",
                "currency",
                paymentSent, paymentReceived, returnSent, returnReceived, netPositionAmount
        )

        val currentPosition = ParticipantPosition(
                LocalDate.now(),
                "participantId",
                "cycleId",
                "currency",
                paymentSent, paymentReceived, returnSent, returnReceived, netPositionAmount
        )

        val debitCapAmount = Amount(BigDecimal(1000), "SEK")
        val debitPositionAmount = Amount(BigDecimal(2000), "SEK")

        val intraDay = IntraDayPositionGross(
                "schemeId",
                "debitParticipantId",
                LocalDate.now(), debitCapAmount, debitPositionAmount
        )

        val participant = Participant(
                "participantId",
                "participantId",
                "name",
                "fundingBic",
                ParticipantStatus.ACTIVE,
                null,
                ParticipantType.FUNDED,
                null,
                "organizationId",
                null,
                null,
                null,
                null,
                null
        )

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
        val approvalUser = ApprovalUser("John Doe", "12a514", "P27 Scheme")
        val approvalId = "10000020"
        val date = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+1"))
        val requestedChange = mapOf("status" to "suspended")
        val originalData = mapOf("data" to "data")

        val approvalDetails = Approval(
                approvalId,
                ApprovalRequestType.STATUS_CHANGE,
                "FORXSES1",
                date, approvalUser,
                ApprovalStatus.APPROVED,
                approvalUser,
                "Forex Bank",
                "This is the reason that I...",
                approvalUser,
                originalData,
                requestedChange,
                "hashed data",
                "hashed data",
                "Notes"
        )

        val result = MAPPER.toDto(approvalDetails)

        assertThat(result.status).isEqualTo(ApprovalStatus.APPROVED)
        assertThat(result.requestedBy.name).isEqualTo("John Doe")
        assertThat(result.requestedBy.id).isEqualTo("12a514")
        assertThat(result.createdAt).isEqualTo(date)
        assertThat(result.jobId).isEqualTo(approvalId)
        assertThat(result.requestType).isEqualTo(ApprovalRequestType.STATUS_CHANGE)
        assertThat(result.participantIdentifier).isEqualTo("FORXSES1")
        assertThat(result.participantName).isEqualTo("Forex Bank")
        assertThat(result.rejectedBy.name).isEqualTo("John Doe")
        assertThat(result.requestedChange).isEqualTo(requestedChange)
    }

    @Test
    fun `should map all fields of Participant to ManagedParticipantDto`() {
        val participant = Participant(
                "FORXSES1", "FORXSES1", "Forex Bank",
                null, ParticipantStatus.ACTIVE, null, ParticipantType.FUNDING, null,
                "00002121", "Nordnet Bank", "475347837892",
                listOf(
                        Participant(
                                "FORXSES1", "FORXSES1", "Forex Bank", null,
                                ParticipantStatus.ACTIVE, null, ParticipantType.FUNDING, null,
                                "00002121", "Nordnet Bank", "475347837892",
                                null, 0, null
                        )
                ), 1, null
        )
        val participants = Page<Participant>(1, listOf(participant))
        val routingRecords = RoutingRecord(
                "reachableBic",
                ZonedDateTime.now(),
                ZonedDateTime.now(),
                "currency"
        )
        participant.reachableBics = listOf(routingRecords)

        val result = MAPPER.toDto(participants, ManagedParticipantDto::class.java)

        assertThat(result).isNotNull

        val managedParticipant: ManagedParticipantDto =
                result.items.elementAt(0) as ManagedParticipantDto

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
                participant.fundedParticipants[0].fundingBic
        )
        assertThat(managedParticipant.fundedParticipants[0].participantIdentifier).isEqualTo(
                participant.fundedParticipants[0].id
        )
        assertThat(managedParticipant.fundedParticipants[0].participantType).isEqualTo(participant.fundedParticipants[0].participantType.description)
        assertThat(managedParticipant.fundedParticipantsCount).isEqualTo(participant.fundedParticipantsCount)
        assertThat(managedParticipant.reachableBics[0].reachableBic).isEqualTo(participant.reachableBics[0].reachableBic)
        assertThat(managedParticipant.reachableBics[0].validFrom).isEqualTo(participant.reachableBics[0].validFrom)
        assertThat(managedParticipant.reachableBics[0].validTo).isEqualTo(participant.reachableBics[0].validTo)
        assertThat(managedParticipant.reachableBics[0].currency).isEqualTo(participant.reachableBics[0].currency)
    }

    @Test
    fun `should map RoutingRecordDto fields`() {
        val entity = RoutingRecord(
                "reachableBic",
                ZonedDateTime.now(),
                ZonedDateTime.now(),
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
        val participant = Participant(
                "FORXSES1", "FORXSES1", "Forex Bank",
                null, ParticipantStatus.ACTIVE, null, ParticipantType.FUNDED, null,
                "00002121", "Nordnet Bank", "475347837892",
                emptyList(), 1, null
        )
        val fundingParticipant = Participant(
                "participantId",
                "participantId",
                "name",
                "fundingBic",
                ParticipantStatus.ACTIVE,
                null,
                ParticipantType.FUNDING,
                null,
                "organizationId",
                null,
                null,
                null,
                null,
                null
        )
        val configuration = ParticipantConfiguration(
                "schemeParticipantIdentifier",
                10,
                10,
                "networkName",
                "gatewayName",
                "requestorDN",
                "responderDN",
                "preSettlementAckType",
                "preSettlementActGenerationLevel",
                "postSettlementAckType",
                "postSettlementAckGenerationLevel",
                BigDecimal.ONE,
                listOf(0.12, 0.25)
        )
        val account = Account(
                "partyCode", 234, "iban"
        )

        val result = MAPPER.toDto(participant, configuration, fundingParticipant, account)

        assertThat(result.bic).isEqualTo(participant.bic)
        assertThat(result.name).isEqualTo(participant.name)
        assertThat(result.id).isEqualTo(participant.id)
        assertThat(result.status).isEqualTo(participant.status)
        assertThat(result.suspendedTime).isEqualTo(participant.suspendedTime)
        assertThat(result.participantType).isEqualTo(participant.participantType.description)
        assertThat(result.organizationId).isEqualTo(participant.organizationId)
        assertThat(result.tpspName).isEqualTo(participant.tpspName)
        assertThat(result.tpspId).isEqualTo(participant.tpspId)

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
                ZonedDateTime.now(),
                ZonedDateTime.now()
        )
        val result = MAPPER.toDto(entity)
        assertThat(result.id).isEqualTo(entity.sessionInstanceId)
        assertThat(result.sessionCode).isEqualTo(entity.sessionCode)
        assertThat(result.status).isEqualTo(entity.status)
    }
}
