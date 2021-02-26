package com.vocalink.crossproduct.infrastructure.bps.mappers

import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria
import com.vocalink.crossproduct.domain.approval.ApprovalChangeCriteria
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmation
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationType
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria
import com.vocalink.crossproduct.domain.report.ReportSearchCriteria
import com.vocalink.crossproduct.domain.routing.RoutingRecordCriteria
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria
import com.vocalink.crossproduct.infrastructure.bps.BPSSortOrder
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalRequestType
import com.vocalink.crossproduct.infrastructure.bps.mappers.BPSMapper.BPSMAPPER
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime
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
    fun `should map BPSTransactionEnquirySearchRequest fields`() {
        val date = LocalDate.of(2021, 1, 15)
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
            ApprovalRequestType.STATUS_CHANGE, mapOf("status" to "suspended"), "notes"
        )
        val result = BPSMAPPER.toBps(criteria)
        assertThat(result.requestType).isEqualTo(BPSApprovalRequestType.STATUS_CHANGE)
        assertThat(result.requestedChange).isEqualTo(criteria.requestedChange)
        assertThat(result.notes).isEqualTo(criteria.notes)
    }

    @Test
    fun `should map to BPSApprovalSearchRequest fields`() {
        val criteria = ApprovalSearchCriteria(
            0, 20,
            listOf(
                "-participantName", "+participantName",
                "-requestType", "requestType",
                "-jobId", "+jobId",
                "-createdAt", "createdAt",
                "-requestedBy", "+requestedBy",
                "-status", "+status"
            )
        )
        val result = BPSMAPPER.toBps(criteria)
        assertThat(result.sortingOrder[0].sortOrderBy).isEqualTo("participantName")
        assertThat(result.sortingOrder[0].sortOrder).isEqualTo(BPSSortOrder.DESC)
        assertThat(result.sortingOrder[1].sortOrderBy).isEqualTo("participantName")
        assertThat(result.sortingOrder[1].sortOrder).isEqualTo(BPSSortOrder.ASC)

        assertThat(result.sortingOrder[2].sortOrderBy).isEqualTo("requestType")
        assertThat(result.sortingOrder[2].sortOrder).isEqualTo(BPSSortOrder.DESC)
        assertThat(result.sortingOrder[3].sortOrderBy).isEqualTo("requestType")
        assertThat(result.sortingOrder[3].sortOrder).isEqualTo(BPSSortOrder.ASC)

        assertThat(result.sortingOrder[4].sortOrderBy).isEqualTo("approvalId")
        assertThat(result.sortingOrder[4].sortOrder).isEqualTo(BPSSortOrder.DESC)
        assertThat(result.sortingOrder[5].sortOrderBy).isEqualTo("approvalId")
        assertThat(result.sortingOrder[5].sortOrder).isEqualTo(BPSSortOrder.ASC)

        assertThat(result.sortingOrder[6].sortOrderBy).isEqualTo("date")
        assertThat(result.sortingOrder[6].sortOrder).isEqualTo(BPSSortOrder.DESC)
        assertThat(result.sortingOrder[7].sortOrderBy).isEqualTo("date")
        assertThat(result.sortingOrder[7].sortOrder).isEqualTo(BPSSortOrder.ASC)

        assertThat(result.sortingOrder[8].sortOrderBy).isEqualTo("requestedBy")
        assertThat(result.sortingOrder[8].sortOrder).isEqualTo(BPSSortOrder.DESC)
        assertThat(result.sortingOrder[9].sortOrderBy).isEqualTo("requestedBy")
        assertThat(result.sortingOrder[9].sortOrder).isEqualTo(BPSSortOrder.ASC)

        assertThat(result.sortingOrder[10].sortOrderBy).isEqualTo("status")
        assertThat(result.sortingOrder[10].sortOrder).isEqualTo(BPSSortOrder.DESC)
        assertThat(result.sortingOrder[11].sortOrderBy).isEqualTo("status")
        assertThat(result.sortingOrder[11].sortOrder).isEqualTo(BPSSortOrder.ASC)
    }

    @Test
    fun `should map BPSReportsSearchRequest fields`() {
        val criteria = ReportSearchCriteria(
            0,
            20,
            listOf("sort"),
            "DAILY_SETTLEMENT_REPORT",
            "Resursbank",
            "10000000305",
            ZonedDateTime.parse("2021-02-15T00:00:00Z"),
            ZonedDateTime.parse("2021-02-16T00:00:00Z")
        )

        val request = BPSMAPPER.toBps(criteria)
        assertThat(request.offset).isEqualTo(criteria.offset)
        assertThat(request.limit).isEqualTo(criteria.limit)
        assertThat(request.sort).isEqualTo(criteria.sort)
        assertThat(request.participant).isEqualTo(criteria.participant)
        assertThat(request.dateFrom).isEqualTo(criteria.dateFrom)
        assertThat(request.id).isEqualTo(criteria.id)
        assertThat(request.dateTo).isEqualTo(criteria.dateTo)
        assertThat(request.reportType).isEqualTo(criteria.reportType)
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
}
