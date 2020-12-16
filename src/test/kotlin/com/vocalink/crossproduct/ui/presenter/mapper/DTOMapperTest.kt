package com.vocalink.crossproduct.ui.presenter.mapper;

import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.alert.Alert
import com.vocalink.crossproduct.domain.alert.AlertData
import com.vocalink.crossproduct.domain.alert.AlertPriority
import com.vocalink.crossproduct.domain.alert.AlertReferenceData
import com.vocalink.crossproduct.domain.alert.AlertStats
import com.vocalink.crossproduct.domain.batch.Batch
import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.files.EnquirySenderDetails
import com.vocalink.crossproduct.domain.files.File
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross
import com.vocalink.crossproduct.domain.position.ParticipantPosition
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference
import com.vocalink.crossproduct.domain.reference.ParticipantReference
import com.vocalink.crossproduct.shared.participant.ParticipantType
import com.vocalink.crossproduct.ui.dto.alert.AlertDto
import com.vocalink.crossproduct.ui.dto.batch.BatchDto
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto
import com.vocalink.crossproduct.ui.dto.file.FileDto
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionGrossDto
import com.vocalink.crossproduct.ui.dto.position.ParticipantPositionDto
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class DTOMapperTest {

    @Test
    fun `should map all fields`() {
        val id = "anyId"
        val cutoffTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val settlementTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
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
    fun `should map all Total Position fields with no intra-days`() {
        val bic = "any_bic"
        val participantId = "any_id"
        val name = "any_name"
        val fundingBic = "any_funding_bic"
        val participantStatus = ParticipantStatus.ACTIVE
        val suspendedTime = LocalDateTime.of(2020, Month.JULY, 10, 10, 10)

        val credit = BigDecimal.TEN
        val debit = BigDecimal.TEN
        val netPosition = BigDecimal.TEN

        val totalPositions: List<ParticipantPosition> = listOf(
                ParticipantPosition.builder()
                        .participantId(participantId)
                        .credit(credit)
                        .debit(debit)
                        .netPosition(netPosition)
                        .build())

        val participant = Participant.builder()
                .id(participantId)
                .bic(bic)
                .status(participantStatus)
                .name(name)
                .suspendedTime(suspendedTime)
                .fundingBic(fundingBic)
                .build()

        val currentCycleId = "anyId"
        val currentCycleCutoffTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val currentCycleSettlementTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val currentCycleStatus = CycleStatus.COMPLETED

        val currentCycle = Cycle.builder()
                .id(currentCycleId)
                .cutOffTime(currentCycleCutoffTime)
                .settlementTime(currentCycleSettlementTime)
                .status(currentCycleStatus)
                .totalPositions(totalPositions)
                .build()

        val previousCycleId = "anyId"
        val previousCycleCutoffTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val previousCycleSettlementTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val previousCycleStatus = CycleStatus.COMPLETED

        val previousCycle = Cycle.builder()
                .id(previousCycleId)
                .cutOffTime(previousCycleCutoffTime)
                .settlementTime(previousCycleSettlementTime)
                .totalPositions(totalPositions)
                .status(previousCycleStatus)
                .build()

        val dto = MAPPER.toDto(participant, currentCycle, previousCycle, participantId)
        assertThat(dto.participant.bic).isEqualTo(bic)
        assertThat(dto.participant.id).isEqualTo(participantId)
        assertThat(dto.participant.name).isEqualTo(name)
        assertThat(dto.participant.status).isEqualTo(participantStatus)
        assertThat(dto.participant.suspendedTime).isEqualTo(suspendedTime)
        assertThat(dto.participant.fundingBic).isEqualTo(fundingBic)

        assertThat(dto.currentPosition.credit).isEqualTo(credit)
        assertThat(dto.currentPosition.debit).isEqualTo(debit)
        assertThat(dto.currentPosition.netPosition).isEqualTo(netPosition)

        assertThat(dto.previousPosition.credit).isEqualTo(credit)
        assertThat(dto.previousPosition.debit).isEqualTo(debit)
        assertThat(dto.previousPosition.netPosition).isEqualTo(netPosition)
    }

    @Test
    fun `should map all Total Position fields with intra-days`() {
        val bic = "any_bic"
        val participantId = "any_id"
        val name = "any_name"
        val fundingBic = "any_funding_bic"
        val participantStatus = ParticipantStatus.ACTIVE
        val suspendedTime = LocalDateTime.of(2020, Month.JULY, 10, 10, 10)

        val credit = BigDecimal.TEN
        val debit = BigDecimal.TEN
        val netPosition = BigDecimal.TEN

        val totalPositions: List<ParticipantPosition> = listOf(
                ParticipantPosition.builder()
                        .participantId(participantId)
                        .credit(credit)
                        .debit(debit)
                        .netPosition(netPosition)
                        .build())

        val participant = Participant.builder()
                .id(participantId)
                .bic(bic)
                .status(participantStatus)
                .name(name)
                .suspendedTime(suspendedTime)
                .fundingBic(fundingBic)
                .build()

        val currentCycleId = "anyId"
        val currentCycleCutoffTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val currentCycleSettlementTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val currentCycleStatus = CycleStatus.COMPLETED

        val currentCycle = Cycle.builder()
                .id(currentCycleId)
                .cutOffTime(currentCycleCutoffTime)
                .settlementTime(currentCycleSettlementTime)
                .status(currentCycleStatus)
                .totalPositions(totalPositions)
                .build()

        val previousCycleId = "anyId"
        val previousCycleCutoffTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val previousCycleSettlementTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val previousCycleStatus = CycleStatus.COMPLETED

        val previousCycle = Cycle.builder()
                .id(previousCycleId)
                .cutOffTime(previousCycleCutoffTime)
                .settlementTime(previousCycleSettlementTime)
                .totalPositions(totalPositions)
                .status(previousCycleStatus)
                .build()

        val debitCap = BigDecimal.TEN
        val debitPosition = BigDecimal.TEN

        val intraDays: List<IntraDayPositionGross> = listOf(
                IntraDayPositionGross.builder()
                        .participantId(participantId)
                        .debitCap(debitCap)
                        .debitPosition(debitPosition)
                        .build()
        )

        val dto = MAPPER.toDto(participant, currentCycle, previousCycle, intraDays, participantId)
        assertThat(dto.participant.bic).isEqualTo(bic)
        assertThat(dto.participant.id).isEqualTo(participantId)
        assertThat(dto.participant.name).isEqualTo(name)
        assertThat(dto.participant.status).isEqualTo(participantStatus)
        assertThat(dto.participant.suspendedTime).isEqualTo(suspendedTime)
        assertThat(dto.participant.fundingBic).isEqualTo(fundingBic)

        assertThat(dto.currentPosition.credit).isEqualTo(credit)
        assertThat(dto.currentPosition.debit).isEqualTo(debit)
        assertThat(dto.currentPosition.netPosition).isEqualTo(netPosition)

        assertThat(dto.previousPosition.credit).isEqualTo(credit)
        assertThat(dto.previousPosition.debit).isEqualTo(debit)
        assertThat(dto.previousPosition.netPosition).isEqualTo(netPosition)

        assertThat(dto.intraDayPositionGross.debitCap).isEqualTo(debitCap)
        assertThat(dto.intraDayPositionGross.debitPosition).isEqualTo(debitPosition)
    }

    @Test
    fun `should map all Settlement Dashboard Dto fields with no intra-days`() {
        val bic = "any_bic"
        val participantId = "any_id"

        val name = "any_name"
        val fundingBic = "any_funding_bic"
        val participantStatus = ParticipantStatus.ACTIVE
        val suspendedTime = LocalDateTime.of(2020, Month.JULY, 10, 10, 10)

        val credit = BigDecimal.TEN
        val debit = BigDecimal.TEN
        val netPosition = BigDecimal.TEN

        val participant = ParticipantDto.builder()
                .id(participantId)
                .bic(bic)
                .status(participantStatus)
                .name(name)
                .suspendedTime(suspendedTime)
                .fundingBic(fundingBic)
                .build()

        val positions = listOf(
                ParticipantPosition.builder()
                        .participantId(participantId)
                        .credit(credit)
                        .debit(debit)
                        .netPosition(netPosition)
                        .build()
        )

        val position = ParticipantPositionDto.builder()
                .credit(credit)
                .debit(debit)
                .netPosition(netPosition)
                .build()

        val positionsDto: List<TotalPositionDto> = listOf(
                TotalPositionDto.builder()
                        .participant(participant)
                        .currentPosition(position)
                        .previousPosition(position)
                        .build()
        )

        val currentCycleId = "any_id"
        val currentCycleCutoffTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val currentCycleSettlementTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val currentCycleStatus = CycleStatus.COMPLETED

        val currentCycle = Cycle.builder()
                .id(currentCycleId)
                .cutOffTime(currentCycleCutoffTime)
                .settlementTime(currentCycleSettlementTime)
                .status(currentCycleStatus)
                .totalPositions(positions)
                .build()

        val previousCycleId = "anyId"
        val previousCycleCutoffTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val previousCycleSettlementTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val previousCycleStatus = CycleStatus.OPEN

        val previousCycle = Cycle.builder()
                .id(previousCycleId)
                .cutOffTime(previousCycleCutoffTime)
                .settlementTime(previousCycleSettlementTime)
                .totalPositions(positions)
                .status(previousCycleStatus)
                .build()

        val dto = MAPPER.toDto(currentCycle, previousCycle, positionsDto)

        assertThat(dto.currentCycle.id).isEqualTo(currentCycleId)
        assertThat(dto.currentCycle.cutOffTime).isEqualTo(currentCycleCutoffTime)
        assertThat(dto.currentCycle.settlementTime).isEqualTo(currentCycleSettlementTime)
        assertThat(dto.currentCycle.status).isEqualTo(currentCycleStatus)

        assertThat(dto.previousCycle.id).isEqualTo(previousCycleId)
        assertThat(dto.previousCycle.cutOffTime).isEqualTo(previousCycleCutoffTime)
        assertThat(dto.previousCycle.settlementTime).isEqualTo(previousCycleSettlementTime)
        assertThat(dto.previousCycle.status).isEqualTo(previousCycleStatus)

        assertThat(dto.positions.size).isEqualTo(1)

        assertThat(dto.positions[0].previousPosition.netPosition).isEqualTo(netPosition)
        assertThat(dto.positions[0].previousPosition.credit).isEqualTo(netPosition)
        assertThat(dto.positions[0].previousPosition.debit).isEqualTo(netPosition)

        assertThat(dto.positions[0].currentPosition.netPosition).isEqualTo(netPosition)
        assertThat(dto.positions[0].currentPosition.credit).isEqualTo(netPosition)
        assertThat(dto.positions[0].currentPosition.debit).isEqualTo(netPosition)
    }

    @Test
    fun `should map all Settlement Dashboard Dto fields with intra-days`() {
        val bic = "any_bic"
        val participantId = "any_id"
        val secondParticipantId = "any_id_2"

        val name = "any_name"
        val fundingBic = "any_funding_bic"
        val participantStatus = ParticipantStatus.ACTIVE
        val suspendedTime = LocalDateTime.of(2020, Month.JULY, 10, 10, 10)

        val credit = BigDecimal.TEN
        val debit = BigDecimal.TEN
        val netPosition = BigDecimal.TEN

        val participant1 = ParticipantDto.builder()
                .id(participantId)
                .bic(bic)
                .status(participantStatus)
                .name(name)
                .suspendedTime(suspendedTime)
                .fundingBic(fundingBic)
                .build()

        val positions = listOf(
                ParticipantPosition.builder()
                        .participantId(participantId)
                        .credit(credit)
                        .debit(debit)
                        .netPosition(netPosition)
                        .build(),
                ParticipantPosition.builder()
                        .participantId(secondParticipantId)
                        .credit(credit)
                        .debit(debit)
                        .netPosition(netPosition)
                        .build()
        )

        val position1 = ParticipantPositionDto.builder()
                .credit(credit)
                .debit(debit)
                .netPosition(netPosition)
                .build()

        val participant2 = ParticipantDto.builder()
                .id(secondParticipantId)
                .bic(bic)
                .status(participantStatus)
                .name(name)
                .suspendedTime(suspendedTime)
                .fundingBic(fundingBic)
                .build()

        val position2 = ParticipantPositionDto.builder()
                .credit(credit)
                .debit(debit)
                .netPosition(netPosition)
                .build()

        val debitCap = BigDecimal.TEN
        val debitPosition = BigDecimal.TEN

        val intraDay = IntraDayPositionGrossDto.builder()
                .debitCap(debitCap)
                .debitPosition(debitPosition)
                .build()

        val positionsDto: List<TotalPositionDto> = listOf(
                TotalPositionDto.builder()
                        .participant(participant1)
                        .currentPosition(position1)
                        .previousPosition(position1)
                        .intraDayPositionGross(intraDay)
                        .build(),
                TotalPositionDto.builder()
                        .participant(participant2)
                        .currentPosition(position2)
                        .previousPosition(position2)
                        .intraDayPositionGross(intraDay)
                        .build()
        )

        val currentCycleId = "any_id"
        val currentCycleCutoffTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val currentCycleSettlementTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val currentCycleStatus = CycleStatus.COMPLETED

        val currentCycle = Cycle.builder()
                .id(currentCycleId)
                .cutOffTime(currentCycleCutoffTime)
                .settlementTime(currentCycleSettlementTime)
                .status(currentCycleStatus)
                .totalPositions(positions)
                .build()

        val previousCycleId = "anyId"
        val previousCycleCutoffTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val previousCycleSettlementTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val previousCycleStatus = CycleStatus.OPEN

        val previousCycle = Cycle.builder()
                .id(previousCycleId)
                .cutOffTime(previousCycleCutoffTime)
                .settlementTime(previousCycleSettlementTime)
                .totalPositions(positions)
                .status(previousCycleStatus)
                .build()

        val intraDays: List<IntraDayPositionGross> = listOf(
                IntraDayPositionGross.builder()
                        .participantId(participantId)
                        .debitCap(debitCap)
                        .debitPosition(debitPosition)
                        .build(),
                IntraDayPositionGross.builder()
                        .participantId(secondParticipantId)
                        .debitCap(debitCap)
                        .debitPosition(debitPosition)
                        .build()
        )

        val dto = MAPPER.toDto(currentCycle, previousCycle, positionsDto, participant1, intraDays)

        assertThat(dto.currentCycle.id).isEqualTo(currentCycleId)
        assertThat(dto.currentCycle.cutOffTime).isEqualTo(currentCycleCutoffTime)
        assertThat(dto.currentCycle.settlementTime).isEqualTo(currentCycleSettlementTime)
        assertThat(dto.currentCycle.status).isEqualTo(currentCycleStatus)

        assertThat(dto.previousCycle.id).isEqualTo(previousCycleId)
        assertThat(dto.previousCycle.cutOffTime).isEqualTo(previousCycleCutoffTime)
        assertThat(dto.previousCycle.settlementTime).isEqualTo(previousCycleSettlementTime)
        assertThat(dto.previousCycle.status).isEqualTo(previousCycleStatus)

        assertThat(dto.positions.size).isEqualTo(2)

        assertThat(dto.positions[0].previousPosition.netPosition).isEqualTo(netPosition)
        assertThat(dto.positions[0].previousPosition.credit).isEqualTo(netPosition)
        assertThat(dto.positions[0].previousPosition.debit).isEqualTo(netPosition)

        assertThat(dto.positions[0].currentPosition.netPosition).isEqualTo(netPosition)
        assertThat(dto.positions[0].currentPosition.credit).isEqualTo(netPosition)
        assertThat(dto.positions[0].currentPosition.debit).isEqualTo(netPosition)
        assertThat(dto.positions[0].previousPosition.debit).isEqualTo(netPosition)

        assertThat(dto.positions[0].intraDayPositionGross.debitPosition).isEqualTo(debitPosition)
        assertThat(dto.positions[0].intraDayPositionGross.debitCap).isEqualTo(debitCap)

        assertThat(dto.fundingParticipant.bic).isEqualTo(bic)
        assertThat(dto.fundingParticipant.id).isEqualTo(participantId)
        assertThat(dto.fundingParticipant.name).isEqualTo(name)
        assertThat(dto.fundingParticipant.status).isEqualTo(participantStatus)
        assertThat(dto.fundingParticipant.suspendedTime).isEqualTo(suspendedTime)
        assertThat(dto.fundingParticipant.fundingBic).isEqualTo(fundingBic)
    }

    @Test
    fun `should map all Alert Reference fields`() {
        val priorityName = "Priority1"
        val threshold = 10
        val alertType = "alertType1"
        val model = AlertReferenceData.builder()
                .alertTypes(listOf(alertType))
                .priorities(listOf(
                        AlertPriority.builder()
                                .name(priorityName)
                                .threshold(threshold)
                                .build()
                ))
                .build()
        val result = MAPPER.toDto(model)

        assertEquals(1, result.alertTypes.size)
        assertEquals(1, result.priorities.size)
        assertEquals(alertType, result.alertTypes[0])
        assertEquals(priorityName, result.priorities[0].name)
        assertEquals(threshold, result.priorities[0].threshold)
    }

    @Test
    fun `should map all Alert stats fields`() {
        val priority = "high"
        val count = 10
        val total = 100
        val model = AlertStats.builder()
                .total(total)
                .items(listOf(AlertData.builder()
                        .count(count)
                        .priority(priority)
                        .build()))
                .build()
        val result = MAPPER.toDto(model)

        assertEquals(1, result.items.size)
        assertEquals(total, result.total)
        assertEquals(priority, result.items[0].priority)
        assertEquals(count, result.items[0].count)
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
        val dateRaised = LocalDateTime.now()
        val alert = Alert.builder()
                .alertId(3141)
                .priority("high")
                .dateRaised(dateRaised)
                .type("rejected-central-bank")
                .entities(listOf(ParticipantReference("NDEASESSXXX", "Nordea", ParticipantType.DIRECT_ONLY, null)))
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
        val sender =  EnquirySenderDetails.builder()
                .entityBic("sender_bic")
                .build()
        val file = File.builder()
                .createdAt(LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12))
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
                .createdAt(LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12))
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
        val sender =  EnquirySenderDetails.builder()
                .entityBic("sender_bic")
                .build()
        val batch = Batch.builder()
                .fileName("filename")
                .createdAt(LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12))
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
                .createdAt(LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12))
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
}
