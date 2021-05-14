package com.vocalink.crossproduct.infrastructure.bps.mappers

import com.vocalink.crossproduct.domain.account.Account
import com.vocalink.crossproduct.domain.alert.AlertPriorityType
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationType
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.domain.approval.ApprovalStatus
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.participant.SuspensionLevel
import com.vocalink.crossproduct.infrastructure.bps.account.BPSAccount
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlert
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertPriority
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertReferenceData
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertStats
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertStatsData
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApproval
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalConfirmationResponse
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalRequestType
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalStatus
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatchDetailed
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSCycle
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSCycleStatus
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSDayCycle
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSPayment
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSSettlementPosition
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFile
import com.vocalink.crossproduct.infrastructure.bps.file.BPSSenderDetails
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOData
import com.vocalink.crossproduct.infrastructure.bps.io.BPSParticipantIOData
import com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSManagedParticipant
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipant
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipantConfiguration
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSUserDetails
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSEnquiryType
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSReasonCodeReference
import com.vocalink.crossproduct.infrastructure.bps.report.BPSReport
import com.vocalink.crossproduct.infrastructure.bps.routing.BPSRoutingRecord
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSScheduleDayDetails
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSSettlementCycleSchedule
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSSettlementSchedule
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransaction
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransactionDetails
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest
import com.vocalink.crossproduct.ui.dto.approval.ApprovalChangeRequest
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationRequest
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest
import com.vocalink.crossproduct.ui.dto.report.ReportsSearchRequest
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordRequest
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class EntityMapperTest {

    @Test
    fun `should map Cycle fields`() {
        val bps = BPSCycle(
                "cycleId",
                BPSCycleStatus.PARTIALLY_COMPLETE,
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                true,
                ZonedDateTime.of(2020, Month.JUNE.value, 12, 12, 12, 0, 0, ZoneId.of("UTC"))
        )
        val entity = MAPPER.toEntity(bps)
        assertThat(entity.id).isEqualTo(bps.cycleId)
        assertThat(entity.cutOffTime).isEqualTo(bps.fileSubmissionCutOffTime)
        assertThat(entity.isNextDayCycle).isEqualTo(bps.isNextDayCycle)
        assertThat(entity.settlementConfirmationTime).isEqualTo(bps.settlementConfirmationTime)
        assertThat(entity.settlementTime).isEqualTo(bps.settlementTime)
        assertThat(entity.status).isEqualTo(CycleStatus.PARTIALLY_COMPLETE)
    }

    @Test
    fun `should map Cycle fields with total positions`() {
        val amount10 = BPSAmount(BigDecimal.TEN, "SEK")
        val amount1 = BPSAmount(BigDecimal.ONE, "SEK")
        val amount100 = BPSAmount(BigDecimal.valueOf(100), "SEK")
        val amount0 = BPSAmount(BigDecimal.ZERO, "SEK")
        val netAmount = BPSAmount(BigDecimal.valueOf(50), "SEK")

        val paymentReceived = BPSPayment (234, amount10)
        val paymentSent = BPSPayment(234, amount1)
        val returnReceived = BPSPayment(234, amount100)
        val returnSent = BPSPayment(234, amount0)

        val bpsCycle = BPSCycle(
                "cycleId",
                BPSCycleStatus.COMPLETED,
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                true,
                ZonedDateTime.of(2020, Month.JUNE.value, 12, 12, 12, 0, 0, ZoneId.of("UTC"))
        )
        val bpsPositionMatchingCycleId = BPSSettlementPosition(
                LocalDate.now(),
                "participantId",
                "cycleId",
                "SEK",
                paymentSent, paymentReceived, returnSent, returnReceived, netAmount
        )

        val bpsPositionNotMatching= BPSSettlementPosition(
                LocalDate.now(),
                "participantId",
                "not_matching",
                "SEK",
                paymentSent, paymentReceived, returnSent, returnReceived, netAmount
        )

        val entity = MAPPER.toEntity(bpsCycle, listOf(bpsPositionMatchingCycleId, bpsPositionNotMatching))

        assertThat(entity.id).isEqualTo(bpsCycle.cycleId)
        assertThat(entity.cutOffTime).isEqualTo(bpsCycle.fileSubmissionCutOffTime)
        assertThat(entity.isNextDayCycle).isEqualTo(bpsCycle.isNextDayCycle)
        assertThat(entity.settlementConfirmationTime).isEqualTo(bpsCycle.settlementConfirmationTime)
        assertThat(entity.settlementTime).isEqualTo(bpsCycle.settlementTime)
        assertThat(entity.status).isEqualTo(CycleStatus.COMPLETED)
        assertThat(entity.totalPositions.size).isEqualTo(1)

        assertThat(entity.totalPositions[0].participantId).isEqualTo(bpsPositionMatchingCycleId.participantId)
        assertThat(entity.totalPositions[0].settlementDate).isEqualTo(bpsPositionMatchingCycleId.settlementDate)
        assertThat(entity.totalPositions[0].cycleId).isEqualTo(bpsPositionMatchingCycleId.cycleId)
        assertThat(entity.totalPositions[0].currency).isEqualTo(bpsPositionMatchingCycleId.currency)
        assertThat(entity.totalPositions[0].paymentSent.count).isEqualTo(bpsPositionMatchingCycleId.paymentSent.count)
        assertThat(entity.totalPositions[0].paymentSent.amount.currency).isEqualTo(bpsPositionMatchingCycleId.paymentSent.amount.currency)
        assertThat(entity.totalPositions[0].paymentSent.amount.amount).isEqualTo(bpsPositionMatchingCycleId.paymentSent.amount.amount)
        assertThat(entity.totalPositions[0].paymentReceived.count).isEqualTo(bpsPositionMatchingCycleId.paymentReceived.count)
        assertThat(entity.totalPositions[0].paymentReceived.amount.currency).isEqualTo(bpsPositionMatchingCycleId.paymentReceived.amount.currency)
        assertThat(entity.totalPositions[0].paymentReceived.amount.amount).isEqualTo(bpsPositionMatchingCycleId.paymentReceived.amount.amount)
        assertThat(entity.totalPositions[0].returnSent.count).isEqualTo(bpsPositionMatchingCycleId.returnSent.count)
        assertThat(entity.totalPositions[0].returnSent.amount.currency).isEqualTo(bpsPositionMatchingCycleId.returnSent.amount.currency)
        assertThat(entity.totalPositions[0].returnSent.amount.amount).isEqualTo(bpsPositionMatchingCycleId.returnSent.amount.amount)
        assertThat(entity.totalPositions[0].returnReceived.count).isEqualTo(bpsPositionMatchingCycleId.returnReceived.count)
        assertThat(entity.totalPositions[0].returnReceived.amount.currency).isEqualTo(bpsPositionMatchingCycleId.returnReceived.amount.currency)
        assertThat(entity.totalPositions[0].returnReceived.amount.amount).isEqualTo(bpsPositionMatchingCycleId.returnReceived.amount.amount)
        assertThat(entity.totalPositions[0].netPositionAmount.amount).isEqualTo(bpsPositionMatchingCycleId.netPositionAmount.amount)
        assertThat(entity.totalPositions[0].netPositionAmount.currency).isEqualTo(bpsPositionMatchingCycleId.netPositionAmount.currency)
    }

    @Test
    fun `should map ParticipantPosition fields`() {
        val amount10 = BPSAmount(BigDecimal.TEN, "SEK")
        val amount1 = BPSAmount(BigDecimal.ONE, "SEK")
        val amount100 = BPSAmount(BigDecimal.valueOf(100), "SEK")
        val amount0 = BPSAmount(BigDecimal.ZERO, "SEK")
        val netAmount = BPSAmount(BigDecimal.valueOf(50), "SEK")

        val paymentReceived = BPSPayment (234, amount10)
        val paymentSent = BPSPayment(234, amount1)
        val returnReceived = BPSPayment(234, amount100)
        val returnSent = BPSPayment(234, amount0)

        val bps = BPSSettlementPosition(
                LocalDate.now(),
                "participantId",
                "cycleId",
                "SEK",
                paymentSent, paymentReceived, returnSent, returnReceived, netAmount
        )
        val entity = MAPPER.toEntity(bps)
        assertThat(entity.participantId).isEqualTo(bps.participantId)
        assertThat(entity.settlementDate).isEqualTo(bps.settlementDate)
        assertThat(entity.cycleId).isEqualTo(bps.cycleId)
        assertThat(entity.currency).isEqualTo(bps.currency)
        assertThat(entity.paymentSent.count).isEqualTo(bps.paymentSent.count)
        assertThat(entity.paymentSent.amount.currency).isEqualTo(bps.paymentSent.amount.currency)
        assertThat(entity.paymentSent.amount.amount).isEqualTo(bps.paymentSent.amount.amount)
        assertThat(entity.paymentReceived.count).isEqualTo(bps.paymentReceived.count)
        assertThat(entity.paymentReceived.amount.currency).isEqualTo(bps.paymentReceived.amount.currency)
        assertThat(entity.paymentReceived.amount.amount).isEqualTo(bps.paymentReceived.amount.amount)
        assertThat(entity.returnSent.count).isEqualTo(bps.returnSent.count)
        assertThat(entity.returnSent.amount.currency).isEqualTo(bps.returnSent.amount.currency)
        assertThat(entity.returnSent.amount.amount).isEqualTo(bps.returnSent.amount.amount)
        assertThat(entity.returnReceived.count).isEqualTo(bps.returnReceived.count)
        assertThat(entity.returnReceived.amount.currency).isEqualTo(bps.returnReceived.amount.currency)
        assertThat(entity.returnReceived.amount.amount).isEqualTo(bps.returnReceived.amount.amount)
        assertThat(entity.netPositionAmount.amount).isEqualTo(bps.netPositionAmount.amount)
        assertThat(entity.netPositionAmount.currency).isEqualTo(bps.netPositionAmount.currency)
    }

    @Test
    fun `should map Participant fields`() {
        val bps = BPSParticipant(
                "schemeCode",
                "schemeParticipantIdentifier",
                "countryCode",
                "partyCode",
                "FUNDED",
                "connectingParty",
                "SUSPENDED",
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                "participantName",
                "rcvngParticipantConnectionId",
                "participantConnectionId",
                "SCHEME",
                "organizationId"
        )
        val entity = MAPPER.toEntity(bps)
        assertThat(entity.bic).isEqualTo(bps.schemeParticipantIdentifier)
        assertThat(entity.id).isEqualTo(bps.schemeParticipantIdentifier)
        assertThat(entity.name).isEqualTo(bps.participantName)
        assertThat(entity.status).isEqualTo(ParticipantStatus.SUSPENDED)
        assertThat(entity.participantType).isEqualTo(ParticipantType.FUNDED)
        assertThat(entity.suspendedTime).isEqualTo(bps.effectiveTillDate)
        assertThat(entity.fundingBic).isEqualTo(bps.connectingParty)
        assertThat(entity.suspensionLevel).isEqualTo(SuspensionLevel.SCHEME)
        assertThat(entity.schemeCode).isEqualTo(bps.schemeCode)
    }

    @Test
    fun `should map Batch fields`() {
        val bpsCycle = BPSCycle(
                "cycleId",
                BPSCycleStatus.COMPLETED,
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                true,
                ZonedDateTime.of(2020, Month.JUNE.value, 12, 12, 12, 0, 0, ZoneId.of("UTC"))
        )

        val bps = BPSBatchDetailed(
                "id",
                "batchId",
                10,
                "messageType",
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                "status",
                "reasonCode",
                bpsCycle.cycleId,
                bpsCycle.settlementTime,
                "fileName",
                "bank",
                "bic",
                "iban"
        )
        val entity = MAPPER.toEntity(bps)
        assertThat(entity.batchId).isEqualTo(bps.batchId)
        assertThat(entity.nrOfTransactions).isEqualTo(bps.numberOfTransactions)
        assertThat(entity.fileName).isEqualTo(bps.fileName)
        assertThat(entity.settlementDate).isEqualTo(bps.settlementDate.toLocalDate())
        assertThat(entity.settlementCycleId).isEqualTo(bps.settlementCycle)
        assertThat(entity.createdAt).isEqualTo(bps.sentDateAndTime)
        assertThat(entity.status).isEqualTo(bps.status)
        assertThat(entity.reasonCode).isEqualTo(bps.reasonCode)
        assertThat(entity.messageType).isEqualTo(bps.messageType)
    }

    @Test
    fun `should map File fields`() {
        val bps = BPSFile(
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
                "P27"
        )

        val entity = MAPPER.toEntity(bps)

        assertThat(entity.instructionId).isEqualTo(bps.instructionId)
        assertThat(entity.fileName).isEqualTo(bps.fileName)
        assertThat(entity.fileSize).isEqualTo(bps.fileSize)
        assertThat(entity.createdDate).isEqualTo(bps.createdDate)
        assertThat(entity.originator).isEqualTo(bps.originator)
        assertThat(entity.messageType).isEqualTo(bps.messageType)
        assertThat(entity.nrOfBatches).isEqualTo(bps.nrOfBatches)
        assertThat(entity.status).isEqualTo(bps.status)
        assertThat(entity.reasonCode).isEqualTo(bps.reasonCode)
        assertThat(entity.settlementCycle).isEqualTo(bps.settlementCycle)
        assertThat(entity.schemeParticipantIdentifier).isEqualTo(bps.schemeParticipantIdentifier)
    }

    @Test
    fun `should map FileReference fields`() {
        val bpsReasonCode = BPSReasonCodeReference.BPSReasonCode("ONE", "One Code", true)
        val bpsValidation = BPSReasonCodeReference.BPSValidation("FILE", listOf(bpsReasonCode))
        val bps = BPSReasonCodeReference(listOf(bpsValidation))
        val entity = MAPPER.toEntity(bps)
        assertThat(entity.validations).isNotEmpty()
        assertThat(entity.validations[0].validationLevel).isEqualTo(BPSEnquiryType.FILE.name)
        assertThat(entity.validations[0].reasonCodes[0].reasonCode).isEqualTo("ONE")
    }

    @Test
    fun `should map Alert fields`() {
        val bpsParticipant = BPSParticipant(
                "schemeCode",
                "schemeParticipantIdentifier",
                "countryCode",
                "partyCode",
                "DIRECT",
                "connectingParty",
                "SUSPENDED",
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                "participantName",
                "rcvngParticipantConnectionId",
                "participantConnectionId",
                "SELF",
                "organizationId"
        )
        val bps = BPSAlert(
                23423, "high", ZonedDateTime.now(ZoneId.of("UTC")), "type", listOf(bpsParticipant)
        )
        val entity = MAPPER.toEntity(bps)
        assertThat(entity.alertId).isEqualTo(bps.alertId)
        assertThat(entity.priority).isEqualTo(AlertPriorityType.HIGH)
        assertThat(entity.dateRaised).isEqualTo(bps.dateRaised)
        assertThat(entity.type).isEqualTo(bps.type)
        assertThat(entity.entities[0].id).isEqualTo(bps.entities[0].schemeParticipantIdentifier)
        assertThat(entity.entities[0].name).isEqualTo(bps.entities[0].participantName)
        assertThat(entity.entities[0].participantType).isEqualTo(ParticipantType.DIRECT)
        assertThat(entity.entities[0].fundingBic).isEqualTo(bps.entities[0].connectingParty)
        assertThat(entity.entities[0].schemeCode).isEqualTo(bps.entities[0].schemeCode)
        assertThat(entity.entities[0].suspensionLevel).isEqualTo(SuspensionLevel.SELF)
    }

    @Test
    fun `should map AlertStats fields`() {
        val alertStatsData = BPSAlertStatsData(
                "high", 20
        )
        val bps = BPSAlertStats(
                1, listOf(alertStatsData)
        )
        val entity = MAPPER.toEntity(bps)
        assertThat(entity.total).isEqualTo(1)
        assertThat(entity.items.size).isEqualTo(1)
        assertThat(entity.items[0].count).isEqualTo(20)
        assertThat(entity.items[0].priority).isEqualTo(AlertPriorityType.HIGH)
    }

    @Test
    fun `should map AlertReferenceData fields`() {
        val alertPriority = BPSAlertPriority("name", 100, true)
        val alertReferenceData = BPSAlertReferenceData(
                listOf(alertPriority), listOf("alertType")
        )
        val entity = MAPPER.toEntity(alertReferenceData)
        assertThat(entity.alertTypes.size).isEqualTo(1)
        assertThat(entity.priorities.size).isEqualTo(1)
        assertThat(entity.priorities[0].highlight).isEqualTo(true)
        assertThat(entity.priorities[0].threshold).isEqualTo(100)
        assertThat(entity.priorities[0].name).isEqualTo("name")
    }

    @Test
    fun `should map all fields on ParticipantIOData`() {
        val ioData = BPSIOData(20, "20.00", 100)
        val bps = BPSParticipantIOData("id", ioData, ioData, ioData)

        val entity = MAPPER.toEntity(bps)

        assertThat(entity.schemeParticipantIdentifier).isEqualTo(bps.schemeParticipantIdentifier)
        assertThat(entity.files.rejected).isEqualTo(bps.files.rejected)
        assertThat(entity.files.submitted).isEqualTo(bps.files.submitted)
        assertThat(entity.batches.rejected).isEqualTo(bps.batches.rejected)
        assertThat(entity.batches.submitted).isEqualTo(bps.batches.submitted)
        assertThat(entity.transactions.rejected).isEqualTo(bps.transactions.rejected)
        assertThat(entity.transactions.submitted).isEqualTo(bps.transactions.submitted)
    }

    @Test
    fun `should map Transaction from BPSTransaction fields`() {
        val amount = BPSAmount(BigDecimal.TEN, "SEK")
        val bps = BPSTransaction(
                "instructionId",
                ZonedDateTime.now(ZoneId.of("UTC")),
                "originator",
                "messageType",
                amount,
                "status"
        )
        val entity = MAPPER.toEntity(bps)
        assertThat(entity.instructionId).isEqualTo(bps.instructionId)
        assertThat(entity.amount.amount).isEqualTo(bps.amount.amount)
        assertThat(entity.amount.currency).isEqualTo(bps.amount.currency)
        assertThat(entity.createdAt).isEqualTo(bps.createdDateTime)
        assertThat(entity.status).isEqualTo(bps.status)
        assertThat(entity.originator).isEqualTo(bps.originator)
        assertThat(entity.messageType).isEqualTo(bps.messageType)
    }

    @Test
    fun `should map Transaction from BPSTransactionDetails fields`() {
        val amount = BPSAmount(BigDecimal.TEN, "SEK")
        val sender = BPSSenderDetails(
                "senderName", "senderBic", "iban", "fullname"
        )
        val receiver = BPSSenderDetails(
                "receiverName", "receiverBic", "iban", "fullname"
        )
        val bps = BPSTransactionDetails(
                "txnsInstructionId",
                "messageType",
                ZonedDateTime.now(ZoneId.of("UTC")),
                "transactionStatus",
                "reasonCode",
                "settlementCycle",
                ZonedDateTime.now(ZoneId.of("UTC")),
                "fileName",
                "batchId",
                amount,
                sender,
                receiver
        )
        val entity = MAPPER.toEntity(bps)
        assertThat(entity.instructionId).isEqualTo(bps.txnsInstructionId)
        assertThat(entity.messageType).isEqualTo(bps.messageType)
        assertThat(entity.createdAt).isEqualTo(bps.sentDateTime)
        assertThat(entity.reasonCode).isEqualTo(bps.reasonCode)
        assertThat(entity.settlementCycleId).isEqualTo(bps.settlementCycle)
        assertThat(entity.settlementDate).isEqualTo(bps.settlementDate.toLocalDate())
        assertThat(entity.fileName).isEqualTo(bps.fileName)
        assertThat(entity.batchId).isEqualTo(bps.batchId)
        assertThat(entity.status).isEqualTo(bps.transactionStatus)
        assertThat(entity.amount.amount).isEqualTo(bps.transactionAmount.amount)
        assertThat(entity.amount.currency).isEqualTo(bps.transactionAmount.currency)

        assertThat(entity.sender.entityBic).isEqualTo(bps.sender.entityBic)
        assertThat(entity.sender.fullName).isEqualTo(bps.sender.fullName)
        assertThat(entity.sender.iban).isEqualTo(bps.sender.iban)
        assertThat(entity.sender.entityName).isEqualTo(bps.sender.entityName)

        assertThat(entity.receiver.entityBic).isEqualTo(bps.receiver.entityBic)
        assertThat(entity.receiver.fullName).isEqualTo(bps.receiver.fullName)
        assertThat(entity.receiver.iban).isEqualTo(bps.receiver.iban)
        assertThat(entity.receiver.entityName).isEqualTo(bps.receiver.entityName)
    }

    @Test
    fun `should map AlertPriorityData fields`() {
        val bps = BPSAlertPriority("name", 234234, true)
        val entity = MAPPER.toEntity(bps)
        assertThat(entity.name).isEqualTo(bps.name)
        assertThat(entity.threshold).isEqualTo(bps.threshold)
        assertThat(entity.highlight).isEqualTo(bps.highlight)
    }

    @Test
    fun `should map AlertPriorityData fields with null threshold`() {
        val bps = BPSAlertPriority("name", null, true)
        val entity = MAPPER.toEntity(bps)
        assertNull(entity.threshold)
    }

    @Test
    fun `should map Account fields`() {
        val bps = BPSAccount("partyCode", 234234, "iban")
        val entity = MAPPER.toEntity(bps)
        assertThat(entity.partyCode).isEqualTo(bps.partyCode)
        assertThat(entity.iban).isEqualTo(bps.iban)
        assertThat(entity.accountNo).isEqualTo(bps.accountNo)
    }

    @Test
    fun `should map BPSApprovalDetails to ApprovalDetails`() {
        val approvalUser = BPSUserDetails("P27", "John", " 12a514", "Doe")
        val date = ZonedDateTime.of( LocalDateTime.now(), ZoneId.of("UTC+1"))
        val approvalId = "10000006"
        val requestedChange = mapOf("status" to "Suspended")
        val originalData = mapOf("data" to "data")
        val participantId = "NDEASESSXXX"

        val bpsApprovalDetails = BPSApproval(
                approvalId,
                BPSApprovalRequestType.PARTICIPANT_SUSPEND,
                listOf(participantId),
                date, approvalUser,
                BPSApprovalStatus.APPROVED,
                approvalUser,
                date,
                "This is the reason that I...",
                approvalUser,
                date,
                originalData,
                requestedChange,
                "hashed data",
                "hashed data",
                "Notes"
        )
        val result = MAPPER.toEntity(bpsApprovalDetails)
        assertThat(result.status).isEqualTo(ApprovalStatus.APPROVED)
        assertThat(result.requestedBy.lastName).isEqualTo(approvalUser.lastName)
        assertThat(result.requestedBy.firstName).isEqualTo(approvalUser.firstName)
        assertThat(result.requestedBy.userId).isEqualTo(approvalUser.userId)
        assertThat(result.date).isEqualTo(date)
        assertThat(result.approvedAt).isEqualTo(date)
        assertThat(result.rejectedAt).isEqualTo(date)
        assertThat(result.approvalId).isEqualTo(approvalId)
        assertThat(result.requestType).isEqualTo(ApprovalRequestType.PARTICIPANT_SUSPEND)
        assertThat(result.participantIds[0]).isEqualTo(participantId)
        assertThat(result.rejectedBy.lastName).isEqualTo(approvalUser.lastName)
        assertThat(result.rejectedBy.firstName).isEqualTo(approvalUser.firstName)
        assertThat(result.rejectedBy.userId).isEqualTo(approvalUser.userId)
        assertThat(result.requestedChange).isEqualTo(requestedChange)
    }

    @Test
    fun `should map RoutingRecord fields`() {
        val entity = BPSRoutingRecord(
                "schemeId",
                "reachableBic",
                ZonedDateTime.now(ZoneId.of("UTC")),
                ZonedDateTime.now(ZoneId.of("UTC")),
                "currency"
        )
        val result = MAPPER.toEntity(entity)
        assertThat(result.reachableBic).isEqualTo(entity.reachableBic)
        assertThat(result.validFrom).isEqualTo(entity.validFrom)
        assertThat(result.validTo).isEqualTo(entity.validTo)
        assertThat(result.currency).isEqualTo(entity.currency)
    }

    @Test
    fun `should map ParticipantConfiguration fields`() {
        val bpsApprovingUser = BPSUserDetails(
                "FORXSES1", "John", "E23423", "Doe"
        )
        val entity = BPSParticipantConfiguration(
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
                listOf(0.12, 0.25),
                ZonedDateTime.now(ZoneId.of("UTC")),
                bpsApprovingUser
        )
        val result = MAPPER.toEntity(entity)
        assertThat(result.schemeParticipantIdentifier).isEqualTo(entity.schemeParticipantIdentifier)
        assertThat(result.txnVolume).isEqualTo(entity.txnVolume)
        assertThat(result.outputFileTimeLimit).isEqualTo(entity.outputFileTimeLimit)
        assertThat(result.networkName).isEqualTo(entity.networkName)
        assertThat(result.gatewayName).isEqualTo(entity.gatewayName)
        assertThat(result.requestorDN).isEqualTo(entity.requestorDN)
        assertThat(result.responderDN).isEqualTo(entity.responderDN)
        assertThat(result.preSettlementAckType).isEqualTo(entity.preSettlementAckType)
        assertThat(result.preSettlementActGenerationLevel).isEqualTo(entity.preSettlementActGenerationLevel)
        assertThat(result.postSettlementAckType).isEqualTo(entity.postSettlementAckType)
        assertThat(result.postSettlementAckGenerationLevel).isEqualTo(entity.postSettlementAckGenerationLevel)
        assertThat(result.debitCapLimit).isEqualTo(entity.debitCapLimit)
        assertThat(result.debitCapLimitThresholds).isEqualTo(entity.debitCapLimitThresholds)
        assertThat(result.updatedAt).isEqualTo(entity.updatedAt)
        assertThat(result.updatedBy.firstName).isEqualTo(entity.updatedBy.firstName)
        assertThat(result.updatedBy.lastName).isEqualTo(entity.updatedBy.lastName)
        assertThat(result.updatedBy.userId).isEqualTo(entity.updatedBy.userId)
        assertThat(result.updatedBy.participantId).isEqualTo(entity.updatedBy.schemeParticipantIdentifier)
    }

    @Test
    fun `should map FileEnquirySearchCriteria fields`() {
        val request = FileEnquirySearchRequest()
        request.sort = listOf("sort")
        request.status = "status"
        request.id = "id"
        request.setDate_to(LocalDate.now().toString())
        request.setMsg_direction("sending")
        request.setMsg_type("msg_type")
        request.setParticipant_bic("participant_bic")
        request.setReason_code("reason_code")
        request.setCycle_ids(listOf("cycle1"))

        val entity = MAPPER.toEntity(request)

        assertThat(entity.offset).isEqualTo(request.offset)
        assertThat(entity.limit).isEqualTo(request.limit)
        assertThat(entity.sort).isEqualTo(request.sort)
        assertThat(entity.dateFrom).isEqualTo(request.dateFrom)
        assertThat(entity.dateTo).isEqualTo(request.dateTo)
        assertThat(entity.cycleId).isEqualTo(request.cycleId)
        assertThat(entity.messageDirection).isEqualTo(request.messageDirection)
        assertThat(entity.messageType).isEqualTo(request.messageType)
        assertThat(entity.participantBic).isEqualTo(request.participantBic)
        assertThat(entity.status).isEqualTo(request.status)
        assertThat(entity.reasonCode).isEqualTo(request.reasonCode)
        assertThat(entity.id).isEqualTo(request.id)
    }

    @Test
    fun `should set default date sort to FileEnquirySearchRequest if sort is missing`() {
        val request = FileEnquirySearchRequest()
        val entity = MAPPER.toEntity(request)
        assertThat(entity.sort).isEqualTo(listOf("-createdAt"))
    }

    @Test
    fun `should map BatchEnquirySearchCriteria fields`() {
        val request = BatchEnquirySearchRequest()
        request.sort = listOf("sort")
        request.status = "status"
        request.id = "id"
        request.setDate_to(LocalDate.now().toString())
        request.setMsg_direction("msg_direction")
        request.setMsg_type("msg_type")
        request.setParticipant_bic("participant_bic")
        request.setReason_code("reason_code")
        request.setCycle_ids(listOf("cycle1, cycle2"))

        val entity = MAPPER.toEntity(request)
        assertThat(entity.offset).isEqualTo(request.offset)
        assertThat(entity.limit).isEqualTo(request.limit)
        assertThat(entity.sort).isEqualTo(request.sort)
        assertThat(entity.dateFrom).isEqualTo(request.dateFrom)
        assertThat(entity.dateTo).isEqualTo(request.dateTo)
        assertThat(entity.messageDirection).isEqualTo(request.messageDirection)
        assertThat(entity.messageType).isEqualTo(request.messageType)
        assertThat(entity.participantBic).isEqualTo(request.participantBic)
        assertThat(entity.status).isEqualTo(request.status)
        assertThat(entity.reasonCode).isEqualTo(request.reasonCode)
        assertThat(entity.id).isEqualTo(request.id)
    }

    @Test
    fun `should set default date sort to BatchEnquirySearchCriteria if sort is missing`() {
        val request = BatchEnquirySearchRequest()
        val entity = MAPPER.toEntity(request)
        assertThat(entity.sort).isEqualTo(listOf("-createdAt"))
    }

    @Test
    fun `should map AlertSearchCriteria fields`() {
        val request = AlertSearchRequest()
        request.priorities = listOf("priority")
        request.types = listOf("types")
        request.entities = listOf("entities")
        request.setAlert_id("alertId")
        request.sort = listOf("sort")

        val entity = MAPPER.toEntity(request)
        assertThat(entity.offset).isEqualTo(request.offset)
        assertThat(entity.limit).isEqualTo(request.limit)
        assertThat(entity.sort).isEqualTo(request.sort)
        assertThat(entity.priorities).isEqualTo(request.priorities)
        assertThat(entity.dateFrom).isEqualTo(request.dateFrom)
        assertThat(entity.dateTo).isEqualTo(request.dateTo)
        assertThat(entity.types).isEqualTo(request.types)
        assertThat(entity.entities).isEqualTo(request.entities)
        assertThat(entity.alertId).isEqualTo(request.alertId)
    }

    @Test
    fun `should map TransactionEnquirySearchCriteria fields`() {
        val date = LocalDate.now()
        val request = TransactionEnquirySearchRequest(
                0, 0, listOf("sortBy"), date, date, date,
                "cycleName",
                "messageDirection",
                "messageType",
                "sendingBic",
                "receivingBic",
                "status",
                "reasonCode",
                "id",
                "sendingAccount",
                "receivingAccount",
                date, BigDecimal.TEN, BigDecimal.ONE
        )
        val criteria = MAPPER.toEntity(request)
        assertThat(criteria.offset).isEqualTo(request.offset)
        assertThat(criteria.limit).isEqualTo(request.limit)
        assertThat(criteria.sort).isEqualTo(request.sort)
        assertThat(criteria.dateFrom).isEqualTo(request.dateFrom)
        assertThat(criteria.dateTo).isEqualTo(request.dateTo)
        assertThat(criteria.cycleDay).isEqualTo(request.cycleDay)
        assertThat(criteria.cycleName).isEqualTo(request.cycleName)
        assertThat(criteria.messageDirection).isEqualTo(request.messageDirection)
        assertThat(criteria.messageType).isEqualTo(request.messageType)
        assertThat(criteria.sendingBic).isEqualTo(request.sendingBic)
        assertThat(criteria.receivingBic).isEqualTo(request.receivingBic)
        assertThat(criteria.status).isEqualTo(request.status)
        assertThat(criteria.reasonCode).isEqualTo(request.reasonCode)
        assertThat(criteria.id).isEqualTo(request.id)
        assertThat(criteria.sendingAccount).isEqualTo(request.sendingAccount)
        assertThat(criteria.receivingAccount).isEqualTo(request.receivingAccount)
        assertThat(criteria.valueDate).isEqualTo(request.valueDate)
        assertThat(criteria.txnFrom).isEqualTo(request.txnFrom)
        assertThat(criteria.txnTo).isEqualTo(request.txnTo)
    }

    @Test
    fun `should set default date sort to TransactionEnquirySearchRequest if sort is missing`() {
        val request = TransactionEnquirySearchRequest(
                0, 0, null, null, null, null,
                null, null, null, null,
                null, null, null, null, null,
                null, null, null, null
        )
        val entity = MAPPER.toEntity(request)
        assertThat(entity.sort).isEqualTo(listOf("-createdAt"))
    }

    @Test
    fun `should map EnquirySenderDetailsDto from Account and Participant`() {
        val account = Account("partyCode", 234234, "iban")
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
                null,
                null
        )
        val result = MAPPER.toEntity(account, participant)
        assertThat(result.entityBic).isEqualTo(account.partyCode)
        assertThat(result.entityName).isEqualTo(participant.name)
        assertThat(result.iban).isEqualTo(account.iban)
    }

    @Test
    fun `should map to RoutingRecordCriteria fields`() {
        val request = RoutingRecordRequest()
        request.limit = 20
        request.offset = 0
        request.sort = listOf("someValue1", "someValue2")
        val bic = "bic"

        val result = MAPPER.toEntity(request, bic)
        assertThat(result.limit).isEqualTo(request.limit)
        assertThat(result.offset).isEqualTo(request.offset)
        assertThat(result.sort).isEqualTo(request.sort)
        assertThat(result.bic).isEqualTo(bic)
    }

    @Test
    fun `should map to ReportSearchCriteria fields`() {
        val request = ReportsSearchRequest()
        request.limit = 20
        request.offset = 0
        request.sort = listOf("someValue1", "someValue2")

        val result = MAPPER.toEntity(request)
        assertThat(result.limit).isEqualTo(request.limit)
        assertThat(result.offset).isEqualTo(request.offset)
        assertThat(result.sort).isEqualTo(request.sort)
    }

    @Test
    fun `should map BPSReport to Report fields`() {
        val bpsReport = BPSReport(
                "10000000006",
                "PRE-SETTLEMENT_ADVICE",
                ZonedDateTime.parse("2021-02-14T00:00:00Z"),
                "20201231002",
                "IBCASES1",
                "ICA Banken"
        )
        val result = MAPPER.toEntity(bpsReport)
        assertThat(result.reportId).isEqualTo(bpsReport.reportId)
        assertThat(result.reportType).isEqualTo(bpsReport.reportType)
        assertThat(result.createdAt).isEqualTo(bpsReport.createdAt)
        assertThat(result.cycleId).isEqualTo(bpsReport.cycleId)
        assertThat(result.participantIdentifier).isEqualTo(bpsReport.participantIdentifier)
        assertThat(result.participantName).isEqualTo(bpsReport.participantName)
    }

    @Test
    fun `should map to ApprovalChangeCriteria fields`() {
        val request = ApprovalChangeRequest(
                "PARTICIPANT_SUSPEND", mapOf("status" to "suspended"), "notes"
        )
        val result = MAPPER.toEntity(request)
        assertThat(result.requestType).isEqualTo(ApprovalRequestType.PARTICIPANT_SUSPEND)
        assertThat(result.requestedChange).isEqualTo(request.requestedChange)
        assertThat(result.notes).isEqualTo(request.notes)
    }

    @Test
    fun `should map to ApprovalConfirmationResponse fields`() {
        val bps = BPSApprovalConfirmationResponse("response")
        val result = MAPPER.toEntity(bps)
        assertThat(result.responseMessage).isEqualTo(bps.responseMessage)
    }

    @Test
    fun `should map to ApprovalConfirmationCriteria fields`() {
        val approvalId = "approval_id"
        val request = ApprovalConfirmationRequest(
                ApprovalConfirmationType.APPROVE, "notes"
        )
        val result = MAPPER.toEntity(request, approvalId)
        assertThat(result.approvalId).isEqualTo(approvalId)
        assertThat(result.action).isEqualTo(ApprovalConfirmationType.APPROVE)
        assertThat(result.message).isEqualTo(request.message)
    }

    @Test
    fun `should map to DayCycle fields`() {
        val bps = BPSDayCycle(
                "cycleCode",
                "sessionCode",
                "sessionInstanceId",
                CycleStatus.COMPLETED,
                ZonedDateTime.now(ZoneId.of("UTC")),
                ZonedDateTime.now(ZoneId.of("UTC"))
        )
        val result = MAPPER.toEntity(bps)
        assertThat(result.cycleCode).isEqualTo(bps.cycleCode)
        assertThat(result.sessionCode).isEqualTo(bps.sessionCode)
        assertThat(result.sessionInstanceId).isEqualTo(bps.sessionInstanceId)
        assertThat(result.status).isEqualTo(bps.status)
        assertThat(result.createdDate).isEqualTo(bps.createdDate)
        assertThat(result.updatedDate).isEqualTo(bps.updatedDate)
    }

    @Test
    fun `should map to SettlementCycleSchedule fields`() {
        val bps = BPSSettlementCycleSchedule(
                "sessionCode",
                "startTime",
                "endTime",
                "settlementTime"
        )
        val result = MAPPER.toEntity(bps)
        assertThat(result.cycleName).isEqualTo(bps.sessionCode)
        assertThat(result.startTime).isEqualTo(bps.startTime)
        assertThat(result.cutOffTime).isEqualTo(bps.endTime)
        assertThat(result.settlementStartTime).isEqualTo(bps.settlementTime)
    }

    @Test
    fun `should map to SettlementDetailsSearchCriteria fields`() {
        val request = ParticipantSettlementRequest()
        request.limit = 20
        request.offset = 0
        request.sort = listOf("someValue1", "someValue2")
        request.participantId = "NDEASESSXXX"
        request.cycleId = "20210426002"

        val result = MAPPER.toEntity(request)

        assertThat(result.limit).isEqualTo(request.limit)
        assertThat(result.offset).isEqualTo(request.offset)
        assertThat(result.sort).isEqualTo(request.sort)
        assertThat(result.participantId).isEqualTo(request.participantId)
        assertThat(result.cycleId).isEqualTo(request.cycleId)
    }

    @Test
    fun `should map to SettlementSchedule fields`() {
        val bpsCycle = BPSSettlementCycleSchedule(
                "sessionCode",
                "startTime",
                "endTime",
                "settlementTime"
        )
        val scheduleDayDetails = BPSScheduleDayDetails(
                "weekDay",
                listOf(bpsCycle)
        )
        val bps = BPSSettlementSchedule(
                ZonedDateTime.now(ZoneId.of("UTC")),
                listOf(scheduleDayDetails)
        )
        val result = MAPPER.toEntity(bps)
        assertThat(result.updatedAt).isEqualTo(bps.updatedAt)
        assertThat(result.scheduleDayDetails[0].weekDay).isEqualTo(scheduleDayDetails.weekDay)

        assertThat(result.scheduleDayDetails[0].cycles[0].cycleName).isEqualTo(bpsCycle.sessionCode)
        assertThat(result.scheduleDayDetails[0].cycles[0].startTime).isEqualTo(bpsCycle.startTime)
        assertThat(result.scheduleDayDetails[0].cycles[0].cutOffTime).isEqualTo(bpsCycle.endTime)
        assertThat(result.scheduleDayDetails[0].cycles[0].settlementStartTime).isEqualTo(bpsCycle.settlementTime)
    }

    @Test
    fun `should map to ManagedParticipantsSearchCriteria fields`() {
        val request = ManagedParticipantsSearchRequest()
        request.offset = 0
        request.limit = 20
        request.q = "DABASESXGBG"
        request.sort = listOf(
                "-name", "+name",
                "-status", "+status",
                "-organizationId", "+organizationId",
                "-participantType", "+participantType",
                "-tpspName", "+tpspName",
                "-fundedParticipantsCount", "+fundedParticipantsCount")

        val result = MAPPER.toEntity(request)

        assertThat(result.limit).isEqualTo(request.limit)
        assertThat(result.offset).isEqualTo(request.offset)
        assertThat(result.sort).isEqualTo(request.sort)
        assertThat(result.q).isEqualTo(request.q)
        assertThat(result.sort.size).isEqualTo(request.sort.size)
        assertThat(result.sort).isEqualTo(request.sort)
    }

    @Test
    fun `should map from BPSManagedParticipant to Participant `() {
        val fromDate = ZonedDateTime.now()
        val tillDate = ZonedDateTime.now().plusDays(1L)
        val bpsManagedParticipant = BPSManagedParticipant.builder()
                .schemeCode("P27-SEK")
                .schemeParticipantIdentifier("HANDSESSXXX")
                .countryCode("SWE")
                .partyCode("HANDSESSXXX")
                .participantType("DIRECT")
                .connectingParty("NA")
                .status("SUSPENDED")
                .effectiveFromDate(fromDate)
                .effectiveTillDate(tillDate)
                .participantName("Svenska Handelsbanken")
                .rcvngParticipantConnectionId("NA")
                .participantConnectionId("NA")
                .suspensionLevel("SELF")
                .partyExternalIdentifier("34534534")
                .tpspName("Forex Bank")
                .tpspId("940404004")
                .build()

        val result = MAPPER.toEntity(bpsManagedParticipant)

        assertThat(result.id).isEqualTo(bpsManagedParticipant.schemeParticipantIdentifier)
        assertThat(result.bic).isEqualTo(bpsManagedParticipant.schemeParticipantIdentifier)
        assertThat(result.name).isEqualTo(bpsManagedParticipant.participantName)
        assertThat(result.fundingBic).isEqualTo(bpsManagedParticipant.connectingParty)
        assertThat(result.status).isEqualTo(ParticipantStatus.valueOf(bpsManagedParticipant.status))
        assertThat(result.suspendedTime).isEqualTo(bpsManagedParticipant.effectiveTillDate)
        assertThat(result.participantType).isEqualTo(ParticipantType.valueOf(bpsManagedParticipant.participantType))
        assertThat(result.schemeCode).isEqualTo(bpsManagedParticipant.schemeCode)
        assertThat(result.organizationId).isEqualTo(bpsManagedParticipant.partyExternalIdentifier)
        assertThat(result.suspensionLevel).isEqualTo(SuspensionLevel.valueOf(bpsManagedParticipant.suspensionLevel))
        assertThat(result.tpspName).isEqualTo(bpsManagedParticipant.tpspName)
        assertThat(result.tpspId).isEqualTo(bpsManagedParticipant.tpspId)
    }
}
