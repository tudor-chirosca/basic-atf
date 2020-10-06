package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.domain.ParticipantPosition
import com.vocalink.crossproduct.domain.PositionDetails
import com.vocalink.crossproduct.shared.positions.CPParticipantPosition
import com.vocalink.crossproduct.shared.positions.CPPositionDetails
import com.vocalink.crossproduct.ui.dto.ParticipantPositionDto
import com.vocalink.crossproduct.ui.dto.PositionDetailsDto
import com.vocalink.crossproduct.ui.dto.PositionDetailsTotalsDto
import com.vocalink.crossproduct.ui.dto.TotalPositionDto
import java.math.BigInteger

class MockPositions {
    fun getPositionDetailsTotalsDto(isNetZero: Boolean): PositionDetailsTotalsDto {
        return if (isNetZero) PositionDetailsTotalsDto.builder()
                .totalCredit(BigInteger.TEN)
                .totalDebit(BigInteger.TEN)
                .totalNetPosition(BigInteger.ZERO)
                .build()
        else PositionDetailsTotalsDto.builder()
                .totalCredit(BigInteger.ZERO)
                .totalDebit(BigInteger.TEN)
                .totalNetPosition(BigInteger.TEN)
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
                    .previousPosition(getPositionDto(false))
                    .currentPosition(getPositionDto(true))
                    .build();

    fun getParticipantPosition(isNetZero: Boolean): ParticipantPosition {
        return if (isNetZero)
            ParticipantPosition.builder()
                    .participantId("HANDSESS")
                    .credit(BigInteger.TEN)
                    .debit(BigInteger.TEN)
                    .netPosition(BigInteger.ZERO)
                    .build()
        else ParticipantPosition.builder()
                .participantId("NDEASESSXXX")
                .credit(BigInteger.ONE)
                .debit(BigInteger.TEN)
                .netPosition(BigInteger.valueOf(9))
                .build()
    }

    fun getCPParticipantPosition(isNetZero: Boolean): CPParticipantPosition {
        return if (isNetZero)
            CPParticipantPosition.builder()
                    .participantId("HANDSESS")
                    .credit(BigInteger.TEN)
                    .debit(BigInteger.TEN)
                    .netPosition(BigInteger.ZERO)
                    .build()
        else CPParticipantPosition.builder()
                .participantId("NDEASESSXXX")
                .credit(BigInteger.ONE)
                .debit(BigInteger.TEN)
                .netPosition(BigInteger.valueOf(9))
                .build()
    }

    fun getPositionDto(isNetZero: Boolean): ParticipantPositionDto {
        return if (isNetZero) ParticipantPositionDto.builder()
                .credit(BigInteger.TEN)
                .debit(BigInteger.TEN)
                .netPosition(BigInteger.ZERO)
                .build()
        else ParticipantPositionDto.builder()
                .credit(BigInteger.ONE)
                .debit(BigInteger.TEN)
                .netPosition(BigInteger.valueOf(9))
                .build()
    }
}