package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.domain.position.IntraDayPositionGross
import com.vocalink.crossproduct.domain.position.ParticipantPosition
import com.vocalink.crossproduct.domain.position.PositionDetails
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionTotalDto
import com.vocalink.crossproduct.ui.dto.position.ParticipantPositionDto
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER
import java.math.BigDecimal
import java.util.stream.Collectors.toList

class MockPositions {
    fun getIntraDaysFor(participantIds: List<String>): List<IntraDayPositionGross> {
        return participantIds.stream()
                .map {
                    IntraDayPositionGross.builder()
                            .debitParticipantId(it)
                            .debitCapAmount(BPSAmount(BigDecimal.TEN, "SEK"))
                            .debitPositionAmount(BPSAmount(BigDecimal.ONE, "SEK"))
                            .build()
                }.collect(toList())
    }

    fun getPositionDetailsTotalsDto(isNetZero: Boolean): PositionDetailsTotalsDto {
        return if (isNetZero) PositionDetailsTotalsDto.builder()
                .totalCredit(BigDecimal.TEN)
                .totalDebit(BigDecimal.TEN)
                .totalNetPosition(BigDecimal.ZERO)
                .build()
        else PositionDetailsTotalsDto.builder()
                .totalCredit(BigDecimal.ZERO)
                .totalDebit(BigDecimal.TEN)
                .totalNetPosition(BigDecimal.TEN)
                .build()
    }

    var positionsDto = listOf(
            TotalPositionDto.builder()
                    .currentPosition(getPositionDto(false))
                    .previousPosition(getPositionDto(true))
                    .participant(MAPPER.toDto(MockParticipants().getParticipant(false)))
                    .build(),
            TotalPositionDto.builder()
                    .currentPosition(getPositionDto(true))
                    .previousPosition(getPositionDto(false))
                    .participant(MAPPER.toDto(MockParticipants().getParticipant(true)))
                    .build()
    )

    val positionDetails = listOf(
            PositionDetails.builder()
                    .customerCreditTransfer(getParticipantPosition(false))
                    .paymentReturn(getParticipantPosition(true))
                    .build(),
            PositionDetails.builder()
                    .customerCreditTransfer(getParticipantPosition(true))
                    .paymentReturn(getParticipantPosition(false))
                    .build())

    val positionDetailsDto =
            PositionDetailsDto.builder()
                    .customerCreditTransfer(getPositionDto(false))
                    .paymentReturn(getPositionDto(true))
                    .build()

    fun getParticipantPosition(isNetZero: Boolean): ParticipantPosition {
        return if (isNetZero)
            ParticipantPosition.builder()
                    .participantId("HANDSESS")
                    .credit(BigDecimal.TEN)
                    .debit(BigDecimal.TEN)
                    .netPosition(BigDecimal.ZERO)
                    .build()
        else ParticipantPosition.builder()
                .participantId("NDEASESSXXX")
                .credit(BigDecimal.ONE)
                .debit(BigDecimal.TEN)
                .netPosition(BigDecimal.valueOf(9))
                .build()
    }

    fun getPositionDto(isNetZero: Boolean): ParticipantPositionDto {
        return if (isNetZero) ParticipantPositionDto.builder()
                .credit(BigDecimal.TEN)
                .debit(BigDecimal.TEN)
                .netPosition(BigDecimal.ZERO)
                .build()
        else ParticipantPositionDto.builder()
                .credit(BigDecimal.ONE)
                .debit(BigDecimal.TEN)
                .netPosition(BigDecimal.valueOf(9))
                .build()
    }

    fun getIntraDayPositionTotalDto(isNetZero: Boolean): IntraDayPositionTotalDto {
        return if (isNetZero) IntraDayPositionTotalDto.builder()
                .totalDebitCap(BigDecimal.TEN)
                .totalDebitPosition(BigDecimal.ONE)
                .build()
        else IntraDayPositionTotalDto.builder ()
                .totalDebitCap(BigDecimal.TEN)
                .totalDebitPosition(BigDecimal.ZERO)
                .build()
    }
}
