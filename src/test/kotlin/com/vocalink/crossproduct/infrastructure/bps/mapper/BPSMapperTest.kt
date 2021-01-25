package com.vocalink.crossproduct.infrastructure.bps.mapper

import com.vocalink.crossproduct.domain.alert.AlertPriorityType
import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.domain.approval.ApprovalStatus
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria
import com.vocalink.crossproduct.infrastructure.adapter.EntityMapper
import com.vocalink.crossproduct.infrastructure.bps.account.BPSAccount
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlert
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertPriority
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertReferenceData
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertStats
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertStatsData
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalDetails
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalUser
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSRejectionReason
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatch
import com.vocalink.crossproduct.infrastructure.bps.config.BPSMapper.BPSMAPPER
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSCycle
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSPayment
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSSettlementPosition
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFile
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFileReference
import com.vocalink.crossproduct.infrastructure.bps.file.BPSSenderDetails
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOBatchesMessageTypes
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOData
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODataAmountDetails
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODataDetails
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODetails
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOTransactionsMessageTypes
import com.vocalink.crossproduct.infrastructure.bps.io.BPSParticipantIOData
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipant
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransaction
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class BPSMapperTest {

    @Test
    fun `should map Cycle fields`() {
        val bps = BPSCycle(
                "cycleId",
                "COMPLETED",
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                true,
                ZonedDateTime.of(2020, Month.JUNE.value, 12, 12, 12, 0, 0, ZoneId.of("UTC"))
        )

        val entity = BPSMAPPER.toEntity(bps)

        assertThat(entity.id).isEqualTo(bps.cycleId)
        assertThat(entity.cutOffTime).isEqualTo(bps.fileSubmissionCutOffTime)
        assertThat(entity.isNextDayCycle).isEqualTo(bps.isNextDayCycle)
        assertThat(entity.settlementConfirmationTime).isEqualTo(bps.settlementConfirmationTime)
        assertThat(entity.settlementTime).isEqualTo(bps.settlementTime)
        assertThat(entity.status).isEqualTo(CycleStatus.COMPLETED)
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

        val entity = EntityMapper.MAPPER.toEntity(bps)

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
                "participantConnectionId"
        )
        val entity = BPSMAPPER.toEntity(bps)

        assertThat(entity.bic).isEqualTo(bps.schemeParticipantIdentifier)
        assertThat(entity.id).isEqualTo(bps.schemeParticipantIdentifier)
        assertThat(entity.name).isEqualTo(bps.participantName)
        assertThat(entity.status).isEqualTo(ParticipantStatus.SUSPENDED)
        assertThat(entity.participantType).isEqualTo(ParticipantType.FUNDED)
        assertThat(entity.suspendedTime).isEqualTo(bps.effectiveTillDate)
        assertThat(entity.fundingBic).isEqualTo(bps.connectingParty)
        assertThat(entity.schemeCode).isEqualTo(bps.schemeCode)
    }

    @Test
    fun `should map BPSParticipantsSearchRequest fields`() {
        val connectionParty = "connectingParty"
        val participantType = "participantType"

        val entity = BPSMAPPER.toBps(connectionParty, participantType)
        assertThat(entity.connectingParty).isEqualTo(connectionParty)
        assertThat(entity.participantType).isEqualTo(participantType)
    }

    @Test
    fun `should map BPSBatchEnquirySearchRequest fields`() {
        val request = BatchEnquirySearchCriteria(
                0, 20, listOf("sort"), LocalDate.now(), null,
                listOf("cycle1, cycle2"), "msg_direction", "msg_type",
                "send_bic", "rcvng_bic", "status", "reasonCode",
                "id"
        )
        val entity = BPSMAPPER.toBps(request)
        assertThat(entity.offset).isEqualTo(request.offset)
        assertThat(entity.limit).isEqualTo(request.limit)
        assertThat(entity.sort).isEqualTo(request.sort)
        assertThat(entity.dateFrom).isEqualTo(request.dateFrom)
        assertThat(entity.dateTo).isEqualTo(request.dateTo)
        assertThat(entity.cycleIds).isEqualTo(request.cycleIds)
        assertThat(entity.messageDirection).isEqualTo(request.messageDirection)
        assertThat(entity.messageType).isEqualTo(request.messageType)
        assertThat(entity.sendingBic).isEqualTo(request.sendingBic)
        assertThat(entity.receivingBic).isEqualTo(request.receivingBic)
        assertThat(entity.status).isEqualTo(request.status)
        assertThat(entity.reasonCode).isEqualTo(request.reasonCode)
        assertThat(entity.id).isEqualTo(request.id)
    }

    @Test
    fun `should map BPSFileEnquirySearchRequest fields`() {
        val request = FileEnquirySearchCriteria(
                0, 20, listOf("sort"), LocalDate.now(), null,
                listOf("cycle1, cycle2"), "msg_direction", "msg_type",
                "send_bic", "rcvng_bic", "status", "reasonCode",
                "id"
        )
        val entity = BPSMAPPER.toBps(request)
        assertThat(entity.offset).isEqualTo(request.offset)
        assertThat(entity.limit).isEqualTo(request.limit)
        assertThat(entity.sort).isEqualTo(request.sort)
        assertThat(entity.dateFrom).isEqualTo(request.dateFrom)
        assertThat(entity.dateTo).isEqualTo(request.dateTo)
        assertThat(entity.cycleIds).isEqualTo(request.cycleIds)
        assertThat(entity.messageDirection).isEqualTo(request.messageDirection)
        assertThat(entity.messageType).isEqualTo(request.messageType)
        assertThat(entity.sendingBic).isEqualTo(request.sendingBic)
        assertThat(entity.receivingBic).isEqualTo(request.receivingBic)
        assertThat(entity.status).isEqualTo(request.status)
        assertThat(entity.reasonCode).isEqualTo(request.reasonCode)
        assertThat(entity.id).isEqualTo(request.id)
    }

    @Test
    fun `should map Batch fields`() {
        val bpsCycle = BPSCycle(
                "cycleId",
                "COMPLETED",
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                true,
                ZonedDateTime.of(2020, Month.JUNE.value, 12, 12, 12, 0, 0, ZoneId.of("UTC"))
        )

        val bpsSenderDetails = BPSSenderDetails(
                "entityName",
                "entityBic",
                "iban",
                "fullName"
        )

        val bps = BPSBatch(
                "batchId",
                "name",
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                234234,
                bpsCycle,
                "receivingBic",
                "messageType",
                "messageDirection",
                10,
                "status",
                "reasonCode",
                bpsSenderDetails
        )
        val entity = BPSMAPPER.toEntity(bps)
        assertThat(entity.batchId).isEqualTo(bps.batchId)
        assertThat(entity.nrOfTransactions).isEqualTo(bps.nrOfTransactions)
        assertThat(entity.fileName).isEqualTo(bps.name)
        assertThat(entity.fileSize).isEqualTo(bps.fileSize)
        assertThat(entity.settlementDate).isEqualTo(bps.cycle.settlementTime.toLocalDate())
        assertThat(entity.settlementCycleId).isEqualTo(bps.cycle.cycleId)
        assertThat(entity.createdAt).isEqualTo(bps.createdAt)
        assertThat(entity.status).isEqualTo(bps.status)
        assertThat(entity.reasonCode).isEqualTo(bps.reasonCode)
        assertThat(entity.messageType).isEqualTo(bps.messageType)

        assertThat(entity.sender.entityName).isEqualTo(bps.sender.entityName)
        assertThat(entity.sender.entityBic).isEqualTo(bps.sender.entityBic)
        assertThat(entity.sender.iban).isEqualTo(bps.sender.iban)
        assertThat(entity.sender.fullName).isEqualTo(bps.sender.fullName)
    }

    @Test
    fun `should map File fields`() {
        val bpsCycle = BPSCycle(
                "cycleId",
                "COMPLETED",
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                true,
                ZonedDateTime.of(2020, Month.JUNE.value, 12, 12, 12, 0, 0, ZoneId.of("UTC"))
        )

        val bpsSenderDetails = BPSSenderDetails(
                "entityName",
                "entityBic",
                "iban",
                "fullName"
        )

        val bps = BPSFile(
                "name",
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                234234,
                bpsCycle,
                "receivingBic",
                "messageType",
                "messageDirection",
                10,
                "status",
                "reasonCode",
                bpsSenderDetails
        )
        val entity = BPSMAPPER.toEntity(bps)
        assertThat(entity.nrOfBatches).isEqualTo(bps.nrOfBatches)
        assertThat(entity.fileName).isEqualTo(bps.name)
        assertThat(entity.fileSize).isEqualTo(bps.fileSize)
        assertThat(entity.settlementDate).isEqualTo(bps.cycle.settlementTime.toLocalDate())
        assertThat(entity.settlementCycleId).isEqualTo(bps.cycle.cycleId)
        assertThat(entity.createdAt).isEqualTo(bps.createdAt)
        assertThat(entity.status).isEqualTo(bps.status)
        assertThat(entity.reasonCode).isEqualTo(bps.reasonCode)
        assertThat(entity.messageType).isEqualTo(bps.messageType)
        assertThat(entity.sender.entityName).isEqualTo(bps.sender.entityName)
        assertThat(entity.sender.entityBic).isEqualTo(bps.sender.entityBic)
        assertThat(entity.sender.iban).isEqualTo(bps.sender.iban)
        assertThat(entity.sender.fullName).isEqualTo(bps.sender.fullName)
    }

    @Test
    fun `should map FileReference fields`() {
        val bps = BPSFileReference(
                "status",
                true,
                "enquiryType",
                listOf("reasonCodes")
        )
        val entity = BPSMAPPER.toEntity(bps)
        assertThat(entity.status).isEqualTo(bps.status)
        assertThat(entity.isHasReason).isEqualTo(bps.isHasReason)
        assertThat(entity.enquiryType).isEqualTo(bps.enquiryType)
        assertThat(entity.reasonCodes).isEqualTo(bps.reasonCodes)
    }

    @Test
    fun `should map Alert fields`() {
        val bpsParticipant = BPSParticipant(
                "schemeCode",
                "schemeParticipantIdentifier",
                "countryCode",
                "partyCode",
                "DIRECT+ONLY",
                "connectingParty",
                "SUSPENDED",
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, Month.JULY.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                "participantName",
                "rcvngParticipantConnectionId",
                "participantConnectionId"
        )
        val bps = BPSAlert(
                23423, "high", ZonedDateTime.now(), "type", listOf(bpsParticipant)
        )
        val entity = BPSMAPPER.toEntity(bps)
        assertThat(entity.alertId).isEqualTo(bps.alertId)
        assertThat(entity.priority).isEqualTo(AlertPriorityType.HIGH)
        assertThat(entity.dateRaised).isEqualTo(bps.dateRaised)
        assertThat(entity.type).isEqualTo(bps.type)

        assertThat(entity.entities[0].participantIdentifier).isEqualTo(bps.entities[0].schemeParticipantIdentifier)
        assertThat(entity.entities[0].name).isEqualTo(bps.entities[0].participantName)
        assertThat(entity.entities[0].participantType).isEqualTo(ParticipantType.DIRECT_ONLY)
        assertThat(entity.entities[0].connectingParticipantId).isEqualTo(bps.entities[0].connectingParty)
        assertThat(entity.entities[0].schemeCode).isEqualTo(bps.entities[0].schemeCode)
    }

    @Test
    fun `should map AlertStats fields`() {
        val alertStatsData = BPSAlertStatsData(
                "high", 20
        )
        val bps = BPSAlertStats(
                1, listOf(alertStatsData)
        )
        val entity = BPSMAPPER.toEntity(bps)
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
        val entity = BPSMAPPER.toEntity(alertReferenceData)
        assertThat(entity.alertTypes.size).isEqualTo(1)
        assertThat(entity.priorities.size).isEqualTo(1)
        assertThat(entity.priorities[0].highlight).isEqualTo(true)
        assertThat(entity.priorities[0].threshold).isEqualTo(100)
        assertThat(entity.priorities[0].name).isEqualTo("name")
    }

    @Test
    fun `should map BPSAlertSearchRequest fields`() {
        val criteria = AlertSearchCriteria(
                0, 20, listOf("priority"), ZonedDateTime.now(), null,
                listOf("types"), listOf("entities"), "alertId", listOf("sort")
        )

        val request = BPSMAPPER.toBps(criteria)
        assertThat(request.offset).isEqualTo(criteria.offset)
        assertThat(request.limit).isEqualTo(criteria.limit)
        assertThat(request.sort).isEqualTo(criteria.sort)
        assertThat(request.priorities).isEqualTo(criteria.priorities)
        assertThat(request.dateFrom).isEqualTo(criteria.dateFrom)
        assertThat(request.dateTo).isEqualTo(criteria.dateTo)
        assertThat(request.types).isEqualTo(criteria.types)
        assertThat(request.entities).isEqualTo(criteria.entities)
        assertThat(request.alertId).isEqualTo(criteria.alertId)
    }

    @Test
    fun `should map all fields on ParticipantIOData`() {
        val ioData = BPSIOData(20, 20.00)
        val bps = BPSParticipantIOData("id", ioData, ioData, ioData)
        val entity = BPSMAPPER.toEntity(bps)

        assertThat(entity.participantId).isEqualTo(bps.participantId)
        assertThat(entity.files.rejected).isEqualTo(bps.files.rejected)
        assertThat(entity.files.submitted).isEqualTo(bps.files.submitted)
        assertThat(entity.batches.rejected).isEqualTo(bps.batches.rejected)
        assertThat(entity.batches.rejected).isEqualTo(bps.batches.rejected)
        assertThat(entity.transactions.rejected).isEqualTo(bps.transactions.rejected)
        assertThat(entity.transactions.rejected).isEqualTo(bps.transactions.rejected)
    }

    @Test
    fun `should map all fields on IODetails`() {
        val ioDataDetails = BPSIODataDetails(1, 2, 3, 4.00)
        val ioDataAmountDetails = BPSIODataAmountDetails(5, 6, 7, 8.00, 9, 10)
        val batches = BPSIOBatchesMessageTypes("batch_code", "batch_name", ioDataDetails)
        val transactions = BPSIOTransactionsMessageTypes("transaction_code", "transaction_name", ioDataAmountDetails)
        val bps = BPSIODetails("ident", ioDataDetails, listOf(batches), listOf(transactions))
        val entity = BPSMAPPER.toEntity(bps)

        assertThat(entity.schemeParticipantIdentifier).isEqualTo(bps.schemeParticipantIdentifier)
        assertThat(entity.files.rejected).isEqualTo(bps.files.rejected)
        assertThat(entity.files.submitted).isEqualTo(bps.files.submitted)
        assertThat(entity.files.accepted).isEqualTo(bps.files.accepted)
        assertThat(entity.files.output).isEqualTo(bps.files.output)
        assertThat(entity.batches[0].name).isEqualTo(bps.batches[0].name)
        assertThat(entity.batches[0].code).isEqualTo(bps.batches[0].code)
        assertThat(entity.batches[0].data.rejected).isEqualTo(bps.batches[0].data.rejected)
        assertThat(entity.batches[0].data.submitted).isEqualTo(bps.batches[0].data.submitted)
        assertThat(entity.batches[0].data.accepted).isEqualTo(bps.batches[0].data.accepted)
        assertThat(entity.batches[0].data.output).isEqualTo(bps.batches[0].data.output)

        assertThat(entity.transactions[0].name).isEqualTo(bps.transactions[0].name)
        assertThat(entity.transactions[0].code).isEqualTo(bps.transactions[0].code)
        assertThat(entity.transactions[0].data.rejected).isEqualTo(bps.transactions[0].data.rejected)
        assertThat(entity.transactions[0].data.submitted).isEqualTo(bps.transactions[0].data.submitted)
        assertThat(entity.transactions[0].data.accepted).isEqualTo(bps.transactions[0].data.accepted)
        assertThat(entity.transactions[0].data.output).isEqualTo(bps.transactions[0].data.output)
        assertThat(entity.transactions[0].data.amountAccepted).isEqualTo(bps.transactions[0].data.amountAccepted)
        assertThat(entity.transactions[0].data.amountOutput).isEqualTo(bps.transactions[0].data.amountOutput)
    }

    @Test
    fun `should map BPSTransactionEnquirySearchRequest fields`() {
        val date = LocalDate.of(2021, 1,15)
        val criteria = TransactionEnquirySearchCriteria(
                0, 0, listOf("sortBy"), date, date,
                listOf("cycle_id"),
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

        val request = BPSMAPPER.toBps(criteria)
        assertThat(request.offset).isEqualTo(criteria.offset)
        assertThat(request.limit).isEqualTo(criteria.limit)
        assertThat(request.sort).isEqualTo(criteria.sort)
        assertThat(request.dateFrom).isEqualTo(criteria.dateFrom)
        assertThat(request.dateTo).isEqualTo(criteria.dateTo)
        assertThat(request.cycleIds).isEqualTo(criteria.cycleIds)
        assertThat(request.messageDirection).isEqualTo(criteria.messageDirection)
        assertThat(request.messageType).isEqualTo(criteria.messageType)
        assertThat(request.sendingBic).isEqualTo(criteria.sendingBic)
        assertThat(request.receivingBic).isEqualTo(criteria.receivingBic)
        assertThat(request.status).isEqualTo(criteria.status)
        assertThat(request.reasonCode).isEqualTo(criteria.reasonCode)
        assertThat(request.id).isEqualTo(criteria.id)
        assertThat(request.sendingAccount).isEqualTo(criteria.sendingAccount)
        assertThat(request.receivingAccount).isEqualTo(criteria.receivingAccount)
        assertThat(request.valueDate).isEqualTo(criteria.valueDate)
        assertThat(request.txnFrom).isEqualTo(criteria.txnFrom)
        assertThat(request.txnTo).isEqualTo(request.txnTo)
    }

    @Test
    fun `should map Transaction fields`() {
        val amount = BPSAmount(BigDecimal.TEN, "SEK")
        val bps = BPSTransaction(
                "instructionId",
                amount,
                "fileName",
                "batchId",
                LocalDate.of(2021, 1,15),
                "receiverEntityBic",
                LocalDate.of(2021, 1,15),
                "settlementCycleId",
                ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")),
                "status",
                "reasonCode",
                "messageType",
                "senderEntityName",
                "senderEntityBic"
        )
        val entity = EntityMapper.MAPPER.toEntity(bps)
        assertThat(entity.instructionId).isEqualTo(bps.instructionId)
        assertThat(entity.amount.amount).isEqualTo(bps.amount.amount)
        assertThat(entity.amount.currency).isEqualTo(bps.amount.currency)
        assertThat(entity.batchId).isEqualTo(bps.batchId)
        assertThat(entity.valueDate).isEqualTo(bps.valueDate)
        assertThat(entity.settlementDate).isEqualTo(bps.settlementDate)
        assertThat(entity.settlementCycleId).isEqualTo(bps.settlementCycleId)
        assertThat(entity.createdAt).isEqualTo(bps.createdAt)
        assertThat(entity.status).isEqualTo(bps.status)
        assertThat(entity.reasonCode).isEqualTo(bps.reasonCode)
        assertThat(entity.messageType).isEqualTo(bps.messageType)
    }

    @Test
    fun `should map AlertPriorityData fields`() {
        val bps = BPSAlertPriority("name", 234234, true)
        val entity = EntityMapper.MAPPER.toEntity(bps)
        assertThat(entity.name).isEqualTo(bps.name)
        assertThat(entity.threshold).isEqualTo(bps.threshold)
        assertThat(entity.highlight).isEqualTo(bps.highlight)
    }

    @Test
    fun `should map AlertPriorityData fields with null threshold`() {
        val bps = BPSAlertPriority("name", null, true)
        val entity = EntityMapper.MAPPER.toEntity(bps)
        assertNull(entity.threshold)
    }

    @Test
    fun `should map Account fields`() {
        val bps = BPSAccount("partyCode", 234234, "iban")
        val entity = EntityMapper.MAPPER.toEntity(bps)
        assertThat(entity.partyCode).isEqualTo(bps.partyCode)
        assertThat(entity.iban).isEqualTo(bps.iban)
        assertThat(entity.accountNo).isEqualTo(bps.accountNo)
    }

    @Test
    fun `should map BPSApprovalDetails to ApprovalDetails`() {
        val approvalUser = BPSApprovalUser("John Doe", "12a514")
        val createdAt = ZonedDateTime.of( LocalDateTime.now(), ZoneId.of("UTC+1"))
        val rejectionReason = BPSRejectionReason(approvalUser,
                "Please check ticket number...")
        val requestedChange = mapOf("status" to "Suspended")

        val bpsApprovalDetails = BPSApprovalDetails(
                "approved",
                approvalUser,
                approvalUser,
                createdAt,
                "10000006",
                "unsuspend",
                "FORXSES1",
                "Forex Bank",
                rejectionReason,
                requestedChange)

        val result = BPSMAPPER.toEntity(bpsApprovalDetails)

        assertThat(result.status).isEqualTo(ApprovalStatus.APPROVED)
        assertThat(result.requestedBy.name).isEqualTo("John Doe")
        assertThat(result.requestedBy.id).isEqualTo("12a514")
        assertThat(result.createdAt).isEqualTo(createdAt)
        assertThat(result.jobId).isEqualTo("10000006")
        assertThat(result.requestType).isEqualTo(ApprovalRequestType.UNSUSPEND)
        assertThat(result.participantIdentifier).isEqualTo("FORXSES1")
        assertThat(result.participantName).isEqualTo("Forex Bank")
        assertThat(result.rejectionReason.rejectedBy.name).isEqualTo("John Doe")
        assertThat(result.rejectionReason.comment).isEqualTo("Please check ticket number...")
        assertThat(result.requestedChange).isEqualTo(requestedChange)
    }
}
