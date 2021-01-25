package com.vocalink.crossproduct.ui.presenter.mapper;

import com.vocalink.crossproduct.domain.Amount
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.account.Account
import com.vocalink.crossproduct.domain.alert.Alert
import com.vocalink.crossproduct.domain.alert.AlertPriorityData
import com.vocalink.crossproduct.domain.alert.AlertPriorityType
import com.vocalink.crossproduct.domain.alert.AlertReferenceData
import com.vocalink.crossproduct.domain.alert.AlertStats
import com.vocalink.crossproduct.domain.alert.AlertStatsData
import com.vocalink.crossproduct.domain.approval.ApprovalDetails
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.domain.approval.ApprovalStatus
import com.vocalink.crossproduct.domain.approval.ApprovalUser
import com.vocalink.crossproduct.domain.approval.RejectionReason
import com.vocalink.crossproduct.domain.batch.Batch
import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.files.EnquirySenderDetails
import com.vocalink.crossproduct.domain.files.File
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross
import com.vocalink.crossproduct.domain.position.ParticipantPosition
import com.vocalink.crossproduct.domain.position.Payment
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference
import com.vocalink.crossproduct.domain.reference.ParticipantReference
import com.vocalink.crossproduct.domain.settlement.ParticipantInstruction
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement
import com.vocalink.crossproduct.domain.settlement.SettlementStatus
import com.vocalink.crossproduct.domain.transaction.Transaction
import com.vocalink.crossproduct.infrastructure.adapter.EntityMapper
import com.vocalink.crossproduct.ui.dto.alert.AlertDto
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest
import com.vocalink.crossproduct.ui.dto.batch.BatchDto
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto
import com.vocalink.crossproduct.ui.dto.file.FileDto
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantInstructionDto
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DTOMapperTest {

    @Test
    fun `should map all fields`() {
        val id = "anyId"
        val cutoffTime = ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC"))
        val settlementTime = ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC"))
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
                .entities(listOf(ParticipantReference("NDEASESSXXX", "Nordea",
                        ParticipantType.DIRECT_ONLY, null, null)))
                .build()

        val alerts = Page<Alert>(1, listOf(alert))

        val result = MAPPER.toAlertPageDto(alerts)

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
        val sender = EnquirySenderDetails.builder()
                .entityBic("sender_bic")
                .build()
        val file = File.builder()
                .createdAt(ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")))
                .messageType("message_type")
                .fileName("name")
                .nrOfBatches(12)
                .sender(sender)
                .status("status")
                .build()
        val page = Page<File>(totalResults, listOf(file))

        val result = MAPPER.toFilePageDto(page)

        assertThat(result).isNotNull

        val resultItem = result.items[0] as FileDto
        assertThat(result.totalResults).isEqualTo(totalResults)
        assertThat(resultItem.createdAt).isEqualTo(file.createdAt)
        assertThat(resultItem.messageType).isEqualTo(file.messageType)
        assertThat(resultItem.name).isEqualTo(file.fileName)
        assertThat(resultItem.nrOfBatches).isEqualTo(file.nrOfBatches)
        assertThat(resultItem.senderBic).isEqualTo(file.sender.entityBic)
        assertThat(resultItem.status).isEqualTo(file.status)
    }

    @Test
    fun `should map FileDetails fields`() {
        val sender = EnquirySenderDetails.builder()
                .entityBic("entity_bic")
                .entityName("sender_name")
                .fullName("full_name")
                .iban("23423423423423423")
                .build()
        val file = File.builder()
                .fileName("name")
                .nrOfBatches(12)
                .fileSize(23423423423)
                .settlementCycleId("01")
                .settlementDate(LocalDate.of(2020, Month.AUGUST, 12))
                .createdAt(ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")))
                .status("status")
                .reasonCode(null)
                .messageType("message_type")
                .sender(sender)
                .build()

        val result: FileDetailsDto = MAPPER.toDetailsDto(file)

        assertThat(result).isNotNull
        assertThat(result.fileName).isEqualTo(file.fileName)
        assertThat(result.nrOfBatches).isEqualTo(file.nrOfBatches)
        assertThat(result.fileSize).isEqualTo(file.fileSize)
        assertThat(result.settlementCycleId).isEqualTo(file.settlementCycleId)
        assertThat(result.settlementDate).isEqualTo(file.settlementDate)
        assertThat(result.createdAt).isEqualTo(file.createdAt)
        assertThat(result.status).isEqualTo(file.status)
        assertThat(result.reasonCode).isEqualTo(file.reasonCode)
        assertThat(result.messageType).isEqualTo(file.messageType)
        assertThat(result.sender.entityBic).isEqualTo(file.sender.entityBic)
        assertThat(result.sender.entityName).isEqualTo(file.sender.entityName)
        assertThat(result.sender.fullName).isEqualTo(file.sender.fullName)
        assertThat(result.sender.iban).isEqualTo(file.sender.iban)
    }

    @Test
    fun `should map Batch fields`() {
        val totalResults = 1
        val sender = EnquirySenderDetails.builder()
                .entityBic("sender_bic")
                .build()
        val batch = Batch.builder()
                .fileName("filename")
                .createdAt(ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")))
                .messageType("message_type")
                .batchId("id")
                .nrOfTransactions(12)
                .sender(sender)
                .status("status")
                .build()
        val page = Page<Batch>(totalResults, listOf(batch))

        val result = MAPPER.toBatchPageDto(page)

        assertThat(result).isNotNull

        val resultItem = result.items[0] as BatchDto
        assertThat(result.totalResults).isEqualTo(totalResults)
        assertThat(resultItem.id).isEqualTo(batch.batchId)
        assertThat(resultItem.createdAt).isEqualTo(batch.createdAt)
        assertThat(resultItem.senderBic).isEqualTo(batch.sender.entityBic)
        assertThat(resultItem.messageType).isEqualTo(batch.messageType)
        assertThat(resultItem.nrOfTransactions).isEqualTo(batch.nrOfTransactions)
        assertThat(resultItem.status).isEqualTo(batch.status)
    }

    @Test
    fun `should map Batch Details fields`() {
        val sender = EnquirySenderDetails.builder()
                .entityBic("sender_bic")
                .fullName("full_name")
                .build()
        val batch = Batch.builder()
                .fileName("filename")
                .createdAt(ZonedDateTime.of(2020, Month.AUGUST.value, 12, 12, 12, 0, 0, ZoneId.of("UTC")))
                .messageType("message_type")
                .batchId("id")
                .nrOfTransactions(12)
                .sender(sender)
                .status("status")
                .build()

        val result = MAPPER.toDetailsDto(batch)

        assertThat(result).isNotNull
        assertThat(result.batchId).isEqualTo(batch.batchId)
        assertThat(result.fileName).isEqualTo(batch.fileName)
        assertThat(result.nrOfTransactions).isEqualTo(batch.nrOfTransactions)
        assertThat(result.fileSize).isEqualTo(batch.fileSize)
        assertThat(result.settlementDate).isEqualTo(batch.settlementDate)
        assertThat(result.settlementCycleId).isEqualTo(batch.settlementCycleId)
        assertThat(result.createdAt).isEqualTo(batch.createdAt)
        assertThat(result.status).isEqualTo(batch.status)
        assertThat(result.reasonCode).isEqualTo(batch.reasonCode)
        assertThat(result.settlementDate).isEqualTo(batch.settlementDate)
        assertThat(result.messageType).isEqualTo(batch.messageType)
        assertThat(result.sender.entityBic).isEqualTo(batch.sender.entityBic)
        assertThat(result.sender.entityName).isEqualTo(batch.sender.entityName)
    }

    @Test
    fun `should map ParticipantSettlementDto fields`() {
        val instruction = ParticipantInstruction(
                "cycleId", "participantId", "reference", "ACCEPTED",
                "counterpartyId", "settlementCounterpartyId",
                BigDecimal.TEN, BigDecimal.TEN
        )
        val settlement = ParticipantSettlement("cycleId", ZonedDateTime.of(2020, 10, 10, 10, 10, 10, 0, ZoneId.of("UTC")),
                SettlementStatus.PARTIAL, "participantId", Page(1, listOf(instruction)))
        val cycle = Cycle("cycleId",
                ZonedDateTime.of(2020, 10, 10, 10, 10, 10, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, 10, 10, 12, 10, 10, 0, ZoneId.of("UTC")),
                CycleStatus.COMPLETED,
                false,
                ZonedDateTime.of(2020, 10, 10, 12, 10, 10, 0, ZoneId.of("UTC")),
                emptyList()
        )
        val participant = Participant("participantId", "participantId", "name",
                "fundingBic", ParticipantStatus.ACTIVE, null, ParticipantType.FUNDED, null)
        val counterparty = Participant("counterpartyId", "counterpartyId", "counterpartyName",
                "fundingBic", ParticipantStatus.ACTIVE, null, ParticipantType.FUNDED, null)
        val settlementCounterparty = Participant("settlementCounterpartyId", "settlementCounterpartyId", "settlementCounterpartyName",
                "fundingBic", ParticipantStatus.ACTIVE, null, ParticipantType.FUNDED, null)

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

        assertThat(instructionResult.settlementCounterparty.participantIdentifier).isEqualTo(settlementCounterparty.bic)
        assertThat(instructionResult.settlementCounterparty.name).isEqualTo(settlementCounterparty.name)
        assertThat(instructionResult.settlementCounterparty.connectingParticipantId).isEqualTo(settlementCounterparty.fundingBic)
        assertThat(instructionResult.settlementCounterparty.participantType).isEqualTo(settlementCounterparty.participantType.description)
    }

    @Test
    fun `should map FileEnquirySearchCriteria fields`() {
        val request = FileEnquirySearchRequest()
        request.sort = listOf("sort")
        request.status = "status"
        request.id = "id"
        request.setDate_to(LocalDate.now().toString())
        request.setMsg_direction("msg_direction")
        request.setMsg_type("msg_type")
        request.setSend_bic("send_bic")
        request.setRecv_bic("rcvng_bic")
        request.setReason_code("reason_code")
        request.setCycle_ids(listOf("cycle1, cycle2"))

        val entity = EntityMapper.MAPPER.toEntity(request)
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
    fun `should map BatchEnquirySearchCriteria fields`() {
        val request = BatchEnquirySearchRequest()
        request.sort = listOf("sort")
        request.status = "status"
        request.id = "id"
        request.setDate_to(LocalDate.now().toString())
        request.setMsg_direction("msg_direction")
        request.setMsg_type("msg_type")
        request.setSend_bic("send_bic")
        request.setRecv_bic("rcvng_bic")
        request.setReason_code("reason_code")
        request.setCycle_ids(listOf("cycle1, cycle2"))

        val entity = EntityMapper.MAPPER.toEntity(request)
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
    fun `should map AlertSearchCriteria fields`() {
        val request = AlertSearchRequest()
        request.priorities = listOf("priority")
        request.types = listOf("types")
        request.entities = listOf("entities")
        request.setAlert_id("alertId")
        request.sort = listOf("sort")


        val entity = EntityMapper.MAPPER.toEntity(request)
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
    fun `should map TransactionDto fields`() {
        val amount = Amount(BigDecimal.TEN, "SEK")
        val transaction = Transaction(
                "instructionId",
                amount,
                "fileName",
                "batchId",
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
    fun `should map TransactionEnquirySearchCriteria fields`() {
        val date = LocalDate.of(2021, 1, 15)
        val request = TransactionEnquirySearchRequest(
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

        val criteria = EntityMapper.MAPPER.toEntity(request)
        assertThat(criteria.offset).isEqualTo(request.offset)
        assertThat(criteria.limit).isEqualTo(request.limit)
        assertThat(criteria.sort).isEqualTo(request.sort)
        assertThat(criteria.dateFrom).isEqualTo(request.dateFrom)
        assertThat(criteria.dateTo).isEqualTo(request.dateTo)
        assertThat(criteria.cycleIds).isEqualTo(request.cycleIds)
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
                LocalDate.of(2021, 1,15),
                "receiverEntityBic",
                LocalDate.of(2021, 1,15),
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
    fun `should map EnquirySenderDetailsDto from Account and Participant`() {
        val account = Account("partyCode", 234234, "iban")
        val participant = Participant("participantId", "participantId", "name",
                "fundingBic", ParticipantStatus.ACTIVE, null, ParticipantType.FUNDED, null)

        val result = EntityMapper.MAPPER.toEntity(account, participant)
        assertThat(result.entityBic).isEqualTo(account.partyCode)
        assertThat(result.entityName).isEqualTo(participant.name)
        assertThat(result.iban).isEqualTo(account.iban)
    }

    @Test
    fun `should map ParticipantDashboardSettlementDetailsDto fields with null threshold`() {
        val previousCycle = Cycle("cycleId",
                ZonedDateTime.of(2020, 10, 10, 10, 10, 10, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, 10, 10, 12, 10, 10, 0, ZoneId.of("UTC")),
                CycleStatus.COMPLETED,
                false,
                ZonedDateTime.of(2020, 10, 10, 12, 10, 10, 0, ZoneId.of("UTC")),
                emptyList()
        )
        val currentCycle = Cycle("cycleId",
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

        val participant = Participant("participantId", "participantId", "name",
                "fundingBic", ParticipantStatus.ACTIVE, null, ParticipantType.FUNDED, null)

        val result = MAPPER.toDto(currentCycle, previousCycle, currentPosition, previousPosition,
                participant, participant, intraDay)

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
        assertThat(result.currentPosition.customerCreditTransfer.netPosition).isEqualTo(currentNetCCT)

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
        val approvalUser = ApprovalUser("John Doe", "12a514")
        val jobId = "10000020"
        val createdAt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+1"))
        val rejectionReason = RejectionReason(approvalUser,
                "Please check ticket number...")
        val requestedChange = mapOf("status" to "suspended")

        val approvalDetails = ApprovalDetails(
                ApprovalStatus.APPROVED,
                approvalUser, approvalUser,
                createdAt, jobId,
                ApprovalRequestType.UNSUSPEND,
                "FORXSES1",
                "Forex Bank",
                rejectionReason,
                requestedChange)

        val result = MAPPER.toDto(approvalDetails)

        assertThat(result.status).isEqualTo(ApprovalStatus.APPROVED)
        assertThat(result.requestedBy.name).isEqualTo("John Doe")
        assertThat(result.requestedBy.id).isEqualTo("12a514")
        assertThat(result.createdAt).isEqualTo(createdAt)
        assertThat(result.jobId).isEqualTo(jobId)
        assertThat(result.requestType).isEqualTo(ApprovalRequestType.UNSUSPEND)
        assertThat(result.participantIdentifier).isEqualTo("FORXSES1")
        assertThat(result.participantName).isEqualTo("Forex Bank")
        assertThat(result.rejectionReason.rejectedBy.name).isEqualTo("John Doe")
        assertThat(result.rejectionReason.comment).isEqualTo("Please check ticket number...")
        assertThat(result.requestedChange).isEqualTo(requestedChange)
    }
}
