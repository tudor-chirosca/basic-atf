package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.shared.participant.CPParticipant
import com.vocalink.crossproduct.shared.participant.ParticipantStatus
import com.vocalink.crossproduct.shared.participant.ParticipantType
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

    val cpParticipants = listOf(
            getCPParticipant(true),
            getCPParticipant(false),
            CPParticipant.builder()
                    .id("ESSESESS")
                    .bic("ESSESESS")
                    .name("SEB Bank")
                    .suspendedTime(null)
                    .status(ParticipantStatus.ACTIVE)
                    .build())

    fun getCPParticipant(isFirst: Boolean): CPParticipant {
        return if (isFirst) CPParticipant.builder()
                .id("HANDSESS")
                .bic("HANDSESS")
                .fundingBic("NA")
                .name("Svenska Handelsbanken")
                .suspendedTime(ZonedDateTime.now().plusDays(15))
                .status(ParticipantStatus.SUSPENDED)
                .build()
        else CPParticipant.builder()
                .id("NDEASESSXXX")
                .bic("NDEASESSXXX")
                .name("Nordea")
                .suspendedTime(null)
                .status(ParticipantStatus.ACTIVE)
                .build()
    }

    fun getParticipantDto(isFirst: Boolean): ParticipantDto {
        return if (isFirst) ParticipantDto.builder()
                .id("HANDSESS")
                .bic("HANDSESS")
                .name("Svenska Handelsbanken")
                .suspendedTime(ZonedDateTime.now().plusDays(15))
                .status(ParticipantStatus.SUSPENDED)
                .participantType(ParticipantType.DIRECT_ONLY)
                .build()
        else ParticipantDto.builder()
                .id("NDEASESSXXX")
                .bic("NDEASESSXXX")
                .name("Nordea")
                .suspendedTime(null)
                .status(ParticipantStatus.ACTIVE)
                .participantType(ParticipantType.DIRECT_ONLY)
                .build()
    }

    val fundingParticipant = Participant.builder()
            .id("DABASESX")
            .bic("DABASESX")
            .name("Danske Bank AG")
            .suspendedTime(ZonedDateTime.now().plusDays(15))
            .status(ParticipantStatus.SUSPENDED)
            .build()

    val fundingParticipantDto = ParticipantDto.builder()
            .id("NDEASESSXXX")
            .bic("NDEASESSXXX")
            .name("Nordea")
            .suspendedTime(ZonedDateTime.now().plusDays(15))
            .status(ParticipantStatus.ACTIVE)
            .build()

    fun getParticipant(isFirst: Boolean): Participant {
        return if (isFirst)
            Participant.builder()
                    .id("HANDSESS")
                    .bic("HANDSESS")
                    .fundingBic("NDEASESSXXX")
                    .name("Svenska Handelsbanken")
                    .suspendedTime(ZonedDateTime.now().plusDays(15))
                    .status(ParticipantStatus.SUSPENDED)
                    .participantType(ParticipantType.DIRECT_ONLY)
                    .build()
        else Participant.builder()
                .id("NDEASESSXXX")
                .bic("NDEASESSXXX")
                .fundingBic("NA")
                .name("Nordea")
                .suspendedTime(null)
                .status(ParticipantStatus.ACTIVE)
                .participantType(ParticipantType.DIRECT_ONLY)
                .build()
    }
}
