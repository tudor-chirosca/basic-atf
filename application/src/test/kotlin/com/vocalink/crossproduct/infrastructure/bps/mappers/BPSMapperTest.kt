package com.vocalink.crossproduct.infrastructure.bps.mappers

import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria
import com.vocalink.crossproduct.domain.approval.ApprovalChangeCriteria
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmation
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationType
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType.PARTICIPANT_SUSPEND
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria
import com.vocalink.crossproduct.domain.approval.ApprovalStatus.PENDING
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria
import com.vocalink.crossproduct.domain.participant.ManagedParticipantsSearchCriteria
import com.vocalink.crossproduct.domain.report.ReportSearchCriteria
import com.vocalink.crossproduct.domain.routing.RoutingRecordCriteria
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria
import com.vocalink.crossproduct.infrastructure.bps.BPSSortOrder
import com.vocalink.crossproduct.infrastructure.bps.BPSSortOrder.*
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalRequestType
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalStatus
import com.vocalink.crossproduct.infrastructure.bps.mappers.BPSMapper.BPSMAPPER
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BPSMapperTest {

    @Test
    fun `should map BPSParticipantsSearchRequest fields`() {
        val connectionParty = "connectingParty"
        val participantType = "participantType"

        val entity = BPSMAPPER.toBps(connectionParty, participantType)
        assertThat(entity.connectingParty).isEqualTo(connectionParty)
        assertThat(entity.participantType).isEqualTo(participantType)
    }

    @Test
    fun `should map BPSBatchEnquirySearchRequest fields and null dates if cycle present`() {
        val request = BatchEnquirySearchCriteria(
                0, 20, ZonedDateTime.now(ZoneId.of("UTC")), ZonedDateTime.now(ZoneId.of("UTC")),
                "cycle1", "sending", "msg_type",
                "participant_bic", "status", "reasonCode",
                null, listOf("id"))

        val entity = BPSMAPPER.toBps(request)
        assertThat(entity.sortingOrder[0].sortOrderBy).isEqualTo("messageIdentifier")
        assertThat(entity.createdFromDate).isNull()
        assertThat(entity.createdToDate).isNull()
        assertThat(entity.messageDirection).isEqualTo(request.messageDirection)
        assertThat(entity.messageType).isEqualTo(request.messageType)
        assertThat(entity.sendingParticipant).isEqualTo(request.participantBic)
        assertThat(entity.receivingParticipant).isNull()
        assertThat(entity.status).isEqualTo(request.status)
        assertThat(entity.reasonCode).isEqualTo(request.reasonCode)
        assertThat(entity.identifier).isEqualTo(request.id)
    }

    @Test
    fun `should map BPSBatchEnquirySearchRequest fields with dates if cycle id missing`() {
        val request = BatchEnquirySearchCriteria(
                0, 20, ZonedDateTime.now(ZoneId.of("UTC")), ZonedDateTime.now(ZoneId.of("UTC")),
                null, "receiving", "msg_type",
                "participant_bic", "status", "reasonCode",
                null, listOf("id"))

        val entity = BPSMAPPER.toBps(request)
        assertThat(entity.sortingOrder[0].sortOrderBy).isEqualTo("messageIdentifier")
        assertThat(entity.createdFromDate).isEqualTo(request.dateFrom)
        assertThat(entity.createdToDate).isEqualTo(request.dateTo)
        assertThat(entity.messageDirection).isEqualTo(request.messageDirection)
        assertThat(entity.messageType).isEqualTo(request.messageType)
        assertThat(entity.sendingParticipant).isNull()
        assertThat(entity.receivingParticipant).isEqualTo(request.participantBic)
        assertThat(entity.status).isEqualTo(request.status)
        assertThat(entity.reasonCode).isEqualTo(request.reasonCode)
        assertThat(entity.identifier).isEqualTo(request.id)
    }

    @Test
    fun `should map BPSFileEnquirySearchRequest fields having cycle id`() {
        val request = FileEnquirySearchCriteria(
                0, 20, ZonedDateTime.now(ZoneId.of("UTC")), ZonedDateTime.now(ZoneId.of("UTC")),
                "cycle1", "sending", "msg_type",
                "participant_bic",  "status", "reasonCode",
                "id", listOf("nrOfBatches"))

        val entity = BPSMAPPER.toBps(request)

        assertThat(entity.sortingOrder[0].sortOrderBy).isEqualTo(request.sort[0])
        assertThat(entity.createdFromDate).isNull()
        assertThat(entity.createdToDate).isNull()
        assertThat(entity.messageDirection).isEqualTo(request.messageDirection)
        assertThat(entity.messageType).isEqualTo(request.messageType)
        assertThat(entity.sendingParticipant).isEqualTo(request.participantBic)
        assertThat(entity.receivingParticipant).isNull()
        assertThat(entity.status).isEqualTo(request.status)
        assertThat(entity.reasonCode).isEqualTo(request.reasonCode)
        assertThat(entity.identifier).isEqualTo(request.id)
    }

    @Test
    fun `should map BPSFileEnquirySearchRequest fields not having cycle id`() {
        val request = FileEnquirySearchCriteria(
                0, 20, ZonedDateTime.now(ZoneId.of("UTC")), ZonedDateTime.now(ZoneId.of("UTC")),
                null, "receiving", "msg_type",
                "participant_bic", "status", "reasonCode",
                "id", listOf("nrOfBatches"))

        val entity = BPSMAPPER.toBps(request)

        assertThat(entity.sortingOrder[0].sortOrderBy).isEqualTo(request.sort[0])
        assertThat(entity.createdFromDate).isEqualTo(request.dateFrom)
        assertThat(entity.createdToDate).isEqualTo(request.dateTo)
        assertThat(entity.messageDirection).isEqualTo(request.messageDirection)
        assertThat(entity.messageType).isEqualTo(request.messageType)
        assertThat(entity.sendingParticipant).isNull()
        assertThat(entity.receivingParticipant).isEqualTo(request.participantBic)
        assertThat(entity.status).isEqualTo(request.status)
        assertThat(entity.reasonCode).isEqualTo(request.reasonCode)
        assertThat(entity.identifier).isEqualTo(request.id)
    }

    @Test
    fun `should map BPSAlertSearchRequest fields`() {
        val criteria = AlertSearchCriteria(
                0, 20, listOf("priority"), ZonedDateTime.now(ZoneId.of("UTC")), null,
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
    fun `should map BPSTransactionEnquirySearchRequest fields and remove dates if cycleName and cycleDay persist`() {
        val date = ZonedDateTime.now(ZoneId.of("UTC"))
        val currency = "SEK"
        val sorting = listOf(
                "-instructionId", "+instructionId",
                "-createdAt", "createdAt",
                "-senderBic", "+senderBic",
                "-messageType", "messageType",
                "-amount", "+amount",
                "-status", "+status"
        )
        val criteria = TransactionEnquirySearchCriteria(
                0, 0, sorting, date, date, date,
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
        val request = BPSMAPPER.toBps(criteria, currency)
        assertNull(request.createdDateFrom)
        assertNull(request.createdDateTo)
        assertThat(request.cycleDay).isEqualTo(criteria.cycleDay)
        assertThat(request.cycleName).isEqualTo(criteria.cycleName)
        assertThat(request.messageDirection).isEqualTo(criteria.messageDirection)
        assertThat(request.messageType).isEqualTo(criteria.messageType)
        assertThat(request.sendingParticipant).isEqualTo(criteria.sendingBic)
        assertThat(request.receivingParticipant).isEqualTo(criteria.receivingBic)
        assertThat(request.status).isEqualTo(criteria.status)
        assertThat(request.reasonCode).isEqualTo(criteria.reasonCode)
        assertThat(request.instructionIdentifier).isEqualTo(criteria.id)
        assertThat(request.sendingAccount).isEqualTo(criteria.sendingAccount)
        assertThat(request.receivingAccount).isEqualTo(criteria.receivingAccount)
        assertThat(request.valueDate).isEqualTo(criteria.valueDate)
        assertThat(request.transactionRangeFrom.amount).isEqualTo(criteria.txnFrom)
        assertThat(request.transactionRangeFrom.currency).isEqualTo(currency)
        assertThat(request.transactionRangeTo.amount).isEqualTo(criteria.txnTo)
        assertThat(request.transactionRangeTo.currency).isEqualTo(currency)

        assertThat(request.sortingOrder[0].sortOrderBy).isEqualTo("instructionId")
        assertThat(request.sortingOrder[0].sortOrder).isEqualTo(DESC)
        assertThat(request.sortingOrder[1].sortOrderBy).isEqualTo("instructionId")
        assertThat(request.sortingOrder[1].sortOrder).isEqualTo(ASC)

        assertThat(request.sortingOrder[2].sortOrderBy).isEqualTo("createdDateTime")
        assertThat(request.sortingOrder[2].sortOrder).isEqualTo(DESC)
        assertThat(request.sortingOrder[3].sortOrderBy).isEqualTo("createdDateTime")
        assertThat(request.sortingOrder[3].sortOrder).isEqualTo(ASC)

        assertThat(request.sortingOrder[4].sortOrderBy).isEqualTo("originator")
        assertThat(request.sortingOrder[4].sortOrder).isEqualTo(DESC)
        assertThat(request.sortingOrder[5].sortOrderBy).isEqualTo("originator")
        assertThat(request.sortingOrder[5].sortOrder).isEqualTo(ASC)

        assertThat(request.sortingOrder[6].sortOrderBy).isEqualTo("messageType")
        assertThat(request.sortingOrder[6].sortOrder).isEqualTo(DESC)
        assertThat(request.sortingOrder[7].sortOrderBy).isEqualTo("messageType")
        assertThat(request.sortingOrder[7].sortOrder).isEqualTo(ASC)

        assertThat(request.sortingOrder[8].sortOrderBy).isEqualTo("amount")
        assertThat(request.sortingOrder[8].sortOrder).isEqualTo(DESC)
        assertThat(request.sortingOrder[9].sortOrderBy).isEqualTo("amount")
        assertThat(request.sortingOrder[9].sortOrder).isEqualTo(ASC)

        assertThat(request.sortingOrder[10].sortOrderBy).isEqualTo("status")
        assertThat(request.sortingOrder[10].sortOrder).isEqualTo(DESC)
        assertThat(request.sortingOrder[11].sortOrderBy).isEqualTo("status")
        assertThat(request.sortingOrder[11].sortOrder).isEqualTo(ASC)
    }

    @Test
    fun `should map BPSTransactionEnquirySearchRequest fields`() {
        val date = ZonedDateTime.now(ZoneId.of("UTC"))
        val currency = "SEK"
        val criteria = TransactionEnquirySearchCriteria(
                0, 0, null, date, date,
                null,
                null,
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
        val request = BPSMAPPER.toBps(criteria, currency)
        assertThat(request.createdDateFrom).isEqualTo(criteria.dateFrom)
        assertThat(request.createdDateTo).isEqualTo(criteria.dateTo)
    }

    @Test
    fun `should map RoutingRecordCriteria fields`() {
        val bic = "bic"
        val entity = RoutingRecordCriteria(
                0, 10, listOf("someValue1", "someValue2"), bic
        )
        val result = BPSMAPPER.toBps(entity)
        assertThat(result.bic).isEqualTo(bic)
        assertThat(result.limit).isEqualTo(entity.limit)
        assertThat(result.offset).isEqualTo(entity.offset)
        assertThat(result.sort).isEqualTo(entity.sort)
    }

    @Test
    fun `should map to BPSApprovalChangeRequest fields`() {
        val criteria = ApprovalChangeCriteria(
                PARTICIPANT_SUSPEND, mapOf("status" to "suspended"), "notes"
        )
        val result = BPSMAPPER.toBps(criteria)
        assertThat(result.requestType).isEqualTo(BPSApprovalRequestType.PARTICIPANT_SUSPEND)
        assertThat(result.requestedChange).isEqualTo(criteria.requestedChange)
        assertThat(result.notes).isEqualTo(criteria.notes)
    }

    @Test
    fun `should map to BPSApprovalSearchRequest fields`() {
        val criteria = ApprovalSearchCriteria(
                0, 20,null, null,
                null, null, null, null, null,
                listOf(
                        "-participants", "+participants",
                        "-requestType", "requestType",
                        "-jobId", "+jobId",
                        "-createdAt", "createdAt",
                        "-requestedBy", "+requestedBy",
                        "-status", "+status"
                )
        )
        val result = BPSMAPPER.toBps(criteria)
        assertThat(result.sortingOrder[0].sortOrderBy).isEqualTo("participantName")
        assertThat(result.sortingOrder[0].sortOrder).isEqualTo(DESC)
        assertThat(result.sortingOrder[1].sortOrderBy).isEqualTo("participantName")
        assertThat(result.sortingOrder[1].sortOrder).isEqualTo(ASC)

        assertThat(result.sortingOrder[2].sortOrderBy).isEqualTo("requestType")
        assertThat(result.sortingOrder[2].sortOrder).isEqualTo(DESC)
        assertThat(result.sortingOrder[3].sortOrderBy).isEqualTo("requestType")
        assertThat(result.sortingOrder[3].sortOrder).isEqualTo(ASC)

        assertThat(result.sortingOrder[4].sortOrderBy).isEqualTo("approvalId")
        assertThat(result.sortingOrder[4].sortOrder).isEqualTo(DESC)
        assertThat(result.sortingOrder[5].sortOrderBy).isEqualTo("approvalId")
        assertThat(result.sortingOrder[5].sortOrder).isEqualTo(ASC)

        assertThat(result.sortingOrder[6].sortOrderBy).isEqualTo("date")
        assertThat(result.sortingOrder[6].sortOrder).isEqualTo(DESC)
        assertThat(result.sortingOrder[7].sortOrderBy).isEqualTo("date")
        assertThat(result.sortingOrder[7].sortOrder).isEqualTo(ASC)

        assertThat(result.sortingOrder[8].sortOrderBy).isEqualTo("requestedBy")
        assertThat(result.sortingOrder[8].sortOrder).isEqualTo(DESC)
        assertThat(result.sortingOrder[9].sortOrderBy).isEqualTo("requestedBy")
        assertThat(result.sortingOrder[9].sortOrder).isEqualTo(ASC)

        assertThat(result.sortingOrder[10].sortOrderBy).isEqualTo("status")
        assertThat(result.sortingOrder[10].sortOrder).isEqualTo(DESC)
        assertThat(result.sortingOrder[11].sortOrderBy).isEqualTo("status")
        assertThat(result.sortingOrder[11].sortOrder).isEqualTo(ASC)
    }

    @Test
    fun `should map all fields from ApprovalSearchCriteria to BPSApprovalSearchRequest fields`() {
        val fromDate = ZonedDateTime.of(2021, 2, 1, 10, 10, 0, 0, ZoneId.of("UTC"))
        val toDate = ZonedDateTime.of(2020, 2, 20, 12, 10, 0, 0, ZoneId.of("UTC"))

        val criteria = ApprovalSearchCriteria(
                0, 20,"10000000", fromDate,
                toDate, listOf("NDEASESSXXX"), listOf(PARTICIPANT_SUSPEND),
                listOf("23451sdf"), listOf(PENDING), listOf("+status")
        )
        val result = BPSMAPPER.toBps(criteria)

        assertThat(result.approvalId).isEqualTo("10000000")
        assertThat(result.fromDate).isEqualTo(fromDate)
        assertThat(result.toDate).isEqualTo(toDate)
        assertThat(result.schemeParticipantIdentifiers).isEqualTo(listOf("NDEASESSXXX"))
        assertThat(result.requestTypes).isEqualTo(listOf(BPSApprovalRequestType.PARTICIPANT_SUSPEND))
        assertThat(result.requestedBy).isEqualTo(listOf("23451sdf"))
        assertThat(result.statuses).isEqualTo(listOf(BPSApprovalStatus.PENDING))
        assertThat(result.sortingOrder[0].sortOrderBy).isEqualTo("status")
        assertThat(result.sortingOrder[0].sortOrder).isEqualTo(ASC)
    }

    @Test
    fun `should map BPSReportsSearchRequest fields`() {
        val criteria = ReportSearchCriteria(
                0,
                20,
                listOf("sort"),
                listOf("DAILY_SETTLEMENT_REPORT"),
                listOf("Resursbank"),
                "10000000305",
                ZonedDateTime.parse("2021-02-15T00:00:00Z"),
                ZonedDateTime.parse("2021-02-16T00:00:00Z")
        )

        val request = BPSMAPPER.toBps(criteria)
        assertThat(request.offset).isEqualTo(criteria.offset)
        assertThat(request.limit).isEqualTo(criteria.limit)
        assertThat(request.sort).isEqualTo(criteria.sort)
        assertThat(request.participants).isEqualTo(criteria.participants)
        assertThat(request.dateFrom).isEqualTo(criteria.dateFrom)
        assertThat(request.id).isEqualTo(criteria.id)
        assertThat(request.dateTo).isEqualTo(criteria.dateTo)
        assertThat(request.reportTypes).isEqualTo(criteria.reportTypes)
    }

    @Test
    fun `should map to BPSApprovalConfirmationRequest fields with APPROVED status`() {
        val approvalConfirmation = ApprovalConfirmation(
                "approval_id", ApprovalConfirmationType.APPROVE, "some_note"
        )
        val request = BPSMAPPER.toBps(approvalConfirmation)
        assertThat(request.approvalId).isEqualTo(approvalConfirmation.approvalId)
        assertThat(request.isApproved).isEqualTo(true)
        assertThat(request.notes).isEqualTo(approvalConfirmation.message)
    }

    @Test
    fun `should map to BPSApprovalConfirmationRequest fields with REJECTED status`() {
        val approvalConfirmation = ApprovalConfirmation(
                "approval_id", ApprovalConfirmationType.REJECT, "some_note"
        )
        val request = BPSMAPPER.toBps(approvalConfirmation)
        assertThat(request.approvalId).isEqualTo(approvalConfirmation.approvalId)
        assertThat(request.isApproved).isEqualTo(false)
        assertThat(request.notes).isEqualTo(approvalConfirmation.message)
    }

    @Test
    fun `should map from ManagedParticipantsSearchCriteria to BPSManagedParticipantsSearchRequest`() {
        val allSortingFields = listOf(
                "-name", "+name",
                "-status", "+status",
                "-organizationId", "+organizationId",
                "-participantType", "+participantType",
                "-tpspName", "+tpspName",
                "-fundedParticipantsCount", "+fundedParticipantsCount")
        val criteria = ManagedParticipantsSearchCriteria.builder()
                .offset(0)
                .limit(20)
                .q("DABASESXGBG")
                .sort(allSortingFields)
                .build()

        val result = BPSMAPPER.toBps(criteria)

        assertThat(result.offset).isEqualTo(criteria.offset)
        assertThat(result.limit).isEqualTo(criteria.limit)
        assertThat(result.q).isEqualTo(criteria.q)
        assertThat(result.sortingOrder[0].sortOrderBy).isEqualTo("name")
        assertThat(result.sortingOrder[0].sortOrder).isEqualTo(DESC)
        assertThat(result.sortingOrder[1].sortOrderBy).isEqualTo("name")
        assertThat(result.sortingOrder[1].sortOrder).isEqualTo(ASC)
        assertThat(result.sortingOrder[2].sortOrderBy).isEqualTo("status")
        assertThat(result.sortingOrder[2].sortOrder).isEqualTo(DESC)
        assertThat(result.sortingOrder[3].sortOrderBy).isEqualTo("status")
        assertThat(result.sortingOrder[3].sortOrder).isEqualTo(ASC)
        assertThat(result.sortingOrder[4].sortOrderBy).isEqualTo("partyExternalIdentifier")
        assertThat(result.sortingOrder[4].sortOrder).isEqualTo(DESC)
        assertThat(result.sortingOrder[5].sortOrderBy).isEqualTo("partyExternalIdentifier")
        assertThat(result.sortingOrder[5].sortOrder).isEqualTo(ASC)
        assertThat(result.sortingOrder[6].sortOrderBy).isEqualTo("participantType")
        assertThat(result.sortingOrder[6].sortOrder).isEqualTo(DESC)
        assertThat(result.sortingOrder[7].sortOrderBy).isEqualTo("participantType")
        assertThat(result.sortingOrder[7].sortOrder).isEqualTo(ASC)
        assertThat(result.sortingOrder[8].sortOrderBy).isEqualTo("tpspName")
        assertThat(result.sortingOrder[8].sortOrder).isEqualTo(DESC)
        assertThat(result.sortingOrder[9].sortOrderBy).isEqualTo("tpspName")
        assertThat(result.sortingOrder[9].sortOrder).isEqualTo(ASC)
        assertThat(result.sortingOrder[10].sortOrderBy).isEqualTo("fundedParticipantsCount")
        assertThat(result.sortingOrder[10].sortOrder).isEqualTo(DESC)
        assertThat(result.sortingOrder[11].sortOrderBy).isEqualTo("fundedParticipantsCount")
        assertThat(result.sortingOrder[11].sortOrder).isEqualTo(ASC)
    }
}
