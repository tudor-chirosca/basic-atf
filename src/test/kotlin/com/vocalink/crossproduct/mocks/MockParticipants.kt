package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.shared.participant.CPParticipant
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto
import java.time.LocalDateTime

class MockParticipants {
    var participants = listOf(
            getParticipant(true),
            getParticipant(false),
            Participant.builder()
                    .id("ESSESESS")
                    .bic("ESSESESS")
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
                    .status("ACTIVE")
                    .build())

    fun getCPParticipant(isFirst: Boolean): CPParticipant {
        return if (isFirst) CPParticipant.builder()
                .id("HANDSESS")
                .bic("HANDSESS")
                .fundingBic("NA")
                .name("Svenska Handelsbanken")
                .suspendedTime(LocalDateTime.now().plusDays(15))
                .status("SUSPENDED")
                .build()
        else CPParticipant.builder()
                .id("NDEASESSXXX")
                .bic("NDEASESSXXX")
                .name("Nordea")
                .suspendedTime(null)
                .status("ACTIVE")
                .build()
    }

    fun getParticipantDto(isFirst: Boolean): ParticipantDto {
        return if (isFirst) ParticipantDto.builder()
                .id("HANDSESS")
                .bic("HANDSESS")
                .name("Svenska Handelsbanken")
                .suspendedTime(LocalDateTime.now().plusDays(15))
                .status(ParticipantStatus.SUSPENDED)
                .build()
        else ParticipantDto.builder()
                .id("NDEASESSXXX")
                .bic("NDEASESSXXX")
                .name("Nordea")
                .suspendedTime(null)
                .status(ParticipantStatus.ACTIVE)
                .build()
    }

    val fundingParticipant = Participant.builder()
            .id("DABASESX")
            .bic("DABASESX")
            .name("Danske Bank AG")
            .suspendedTime(LocalDateTime.now().plusDays(15))
            .status(ParticipantStatus.SUSPENDED)
            .build()

    val fundingParticipantDto = ParticipantDto.builder()
            .id("DABASESX")
            .bic("DABASESX")
            .name("Danske Bank AG")
            .suspendedTime(LocalDateTime.now().plusDays(15))
            .status(ParticipantStatus.SUSPENDED)
            .build()

    fun getParticipant(isFirst: Boolean): Participant {
        return if (isFirst)
            Participant.builder()
                    .id("HANDSESS")
                    .bic("HANDSESS")
                    .fundingBic("NA")
                    .name("Svenska Handelsbanken")
                    .suspendedTime(LocalDateTime.now().plusDays(15))
                    .status(ParticipantStatus.SUSPENDED)
                    .build()
        else Participant.builder()
                .id("NDEASESSXXX")
                .bic("NDEASESSXXX")
                .name("Nordea")
                .suspendedTime(null)
                .status(ParticipantStatus.ACTIVE)
                .build()
    }
}