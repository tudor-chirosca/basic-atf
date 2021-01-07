package com.vocalink.crossproduct.ui.presenter.mapper;

import com.vocalink.crossproduct.domain.io.IOBatchesMessageTypes
import com.vocalink.crossproduct.domain.io.IODataDetails
import com.vocalink.crossproduct.domain.io.IODetails
import com.vocalink.crossproduct.domain.io.IOTransactionsMessageTypes
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.shared.participant.ParticipantStatus
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month.APRIL
import java.time.ZoneId
import java.time.ZonedDateTime

class IODetailsMapperTest {

    @Test
    fun `should map all fields`() {
        val id = "anyId"

        val files = IODataDetails.builder()
                .submitted(1)
                .accepted(1)
                .output(1)
                .rejected(1.2)
                .build()

        val batches = listOf(IOBatchesMessageTypes.builder().build())

        val transactions = listOf(IOTransactionsMessageTypes.builder().build())

        val ioDetails = IODetails.builder()
                .schemeParticipantIdentifier(id)
                .files(files)
                .batches(batches)
                .transactions(transactions)
                .build()

        val participant = Participant.builder()
                .id(id)
                .bic("bic")
                .fundingBic("fundingBic")
                .name("name")
                .status(ParticipantStatus.ACTIVE)
                .suspendedTime(ZonedDateTime.of(2020, APRIL.value, 1, 1, 1, 0, 0, ZoneId.of("UTC")))
                .build()

        val date = LocalDate.of(2020, APRIL, 1)

        val dto = MAPPER.toDto(ioDetails, participant, date)

        assertThat(dto.participant.id).isEqualTo(participant.id)
        assertThat(dto.participant.bic).isEqualTo(participant.bic)
        assertThat(dto.participant.fundingBic).isEqualTo(participant.fundingBic)
        assertThat(dto.participant.name).isEqualTo(participant.name)
        assertThat(dto.participant.status).isEqualTo(participant.status)
        assertThat(dto.participant.suspendedTime).isEqualTo(participant.suspendedTime)

        assertThat(dto.dateFrom).isEqualTo(date)

        assertThat(dto.files.accepted).isEqualTo(files.accepted)
        assertThat(dto.files.output).isEqualTo(files.output)
        assertThat(dto.files.rejected).isEqualTo(files.rejected)
        assertThat(dto.files.submitted).isEqualTo(files.submitted)

        assertThat(dto.batches[0].code).isEqualTo(batches[0].code)
        assertThat(dto.batches[0].data).isEqualTo(batches[0].data)
        assertThat(dto.batches[0].name).isEqualTo(batches[0].name)

        assertThat(dto.transactions[0].code).isEqualTo(transactions[0].code)
        assertThat(dto.transactions[0].data).isEqualTo(transactions[0].data)
        assertThat(dto.transactions[0].name).isEqualTo(transactions[0].name)
    }
}
