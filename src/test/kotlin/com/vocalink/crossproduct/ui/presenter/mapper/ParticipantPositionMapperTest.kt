package com.vocalink.crossproduct.ui.presenter.mapper

import com.vocalink.crossproduct.domain.position.ParticipantPosition
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigInteger

class ParticipantPositionMapperTest {

    @Test
    fun `should map all fields for participant position` () {
        val id = "anyId"

        val participantPosition = ParticipantPosition.builder()
                .participantId(id)
                .credit(BigInteger.TEN)
                .debit(BigInteger.TEN)
                .netPosition(BigInteger.ZERO)
                .build()

        val dto = MAPPER.toDto(participantPosition)

        assertThat(dto.credit).isEqualTo(participantPosition.credit)
        assertThat(dto.debit).isEqualTo(participantPosition.debit)
        assertThat(dto.netPosition).isEqualTo(participantPosition.netPosition)
    }
}