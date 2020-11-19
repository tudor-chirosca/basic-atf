package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.domain.position.IntraDayPositionGross
import com.vocalink.crossproduct.domain.position.ParticipantPosition
import com.vocalink.crossproduct.domain.position.PositionDetails
import com.vocalink.crossproduct.shared.positions.CPParticipantPosition
import com.vocalink.crossproduct.shared.positions.CPPositionDetails
import com.vocalink.crossproduct.ui.dto.position.ParticipantPositionDto
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionTotalDto
import java.math.BigDecimal
import java.math.BigInteger
import java.util.stream.Collectors.toList

class MockPositions {
    fun getIntraDaysFor(participantIds: List<String>): List<IntraDayPositionGross> {
        return participantIds.stream()
                .map {
                    IntraDayPositionGross.builder()
                            .participantId(it)
                            .debitCap(BigDecimal.TEN)
                            .debitPosition(BigDecimal.ONE)
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
                    .participant(MockParticipants().getParticipant(false))
                    .build(),
            TotalPositionDto.builder()
                    .currentPosition(getPositionDto(true))
                    .previousPosition(getPositionDto(false))
                    .participant(MockParticipants().getParticipant(true))
                    .build())

    val cpPositionDetails = listOf(
            CPPositionDetails.builder()
                    .customerCreditTransfer(getCPParticipantPosition(false))
                    .paymentReturn(getCPParticipantPosition(true))
                    .build(),
            CPPositionDetails.builder()
                    .customerCreditTransfer(getCPParticipantPosition(true))
                    .paymentReturn(getCPParticipantPosition(false))
                    .build())

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
                    .build();

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

    fun getCPParticipantPosition(isNetZero: Boolean): CPParticipantPosition {
        return if (isNetZero)
            CPParticipantPosition.builder()
                    .participantId("HANDSESS")
                    .credit(BigDecimal.TEN)
                    .debit(BigDecimal.TEN)
                    .netPosition(BigDecimal.ZERO)
                    .build()
        else CPParticipantPosition.builder()
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
