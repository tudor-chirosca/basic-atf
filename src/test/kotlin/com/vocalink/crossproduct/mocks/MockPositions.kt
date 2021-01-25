package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionTotalDto
import com.vocalink.crossproduct.ui.dto.position.ParticipantPositionDto
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER
import java.math.BigDecimal

class MockPositions {
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

    val positionDetailsDto = PositionDetailsDto(getPositionDto(false), getPositionDto(true))

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
