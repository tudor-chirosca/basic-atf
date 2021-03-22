package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto
import java.time.ZonedDateTime

class MockParticipants {
    var participants = listOf(
            getParticipant(true),
            getParticipant(false),
            Participant.builder()
                    .id("ESSESESS")
                    .bic("ESSESESS")
                    .fundingBic("NA")
                    .name("SEB Bank")
                    .suspendedTime(null)
                    .status(ParticipantStatus.ACTIVE)
                    .build())

    fun getParticipantDto(isFirst: Boolean): ParticipantDto {
        return if (isFirst) ParticipantDto.builder()
                .id("HANDSESS")
                .bic("HANDSESS")
                .name("Svenska Handelsbanken")
                .suspendedTime(ZonedDateTime.now().plusDays(15))
                .status(ParticipantStatus.SUSPENDED)
                .participantType(ParticipantType.DIRECT)
                .build()
        else ParticipantDto.builder()
                .id("NDEASESSXXX")
                .bic("NDEASESSXXX")
                .name("Nordea")
                .suspendedTime(null)
                .status(ParticipantStatus.ACTIVE)
                .participantType(ParticipantType.DIRECT)
                .build()
    }

    fun getParticipant(isFirst: Boolean): Participant {
        return if (isFirst)
            Participant.builder()
                    .id("HANDSESS")
                    .bic("HANDSESS")
                    .fundingBic("NDEASESSXXX")
                    .name("Svenska Handelsbanken")
                    .suspendedTime(ZonedDateTime.now().plusDays(15))
                    .status(ParticipantStatus.SUSPENDED)
                    .participantType(ParticipantType.DIRECT)
                    .build()
        else Participant.builder()
                .id("NDEASESSXXX")
                .bic("NDEASESSXXX")
                .fundingBic("NA")
                .name("Nordea")
                .suspendedTime(null)
                .status(ParticipantStatus.ACTIVE)
                .participantType(ParticipantType.DIRECT)
                .build()
    }
}
