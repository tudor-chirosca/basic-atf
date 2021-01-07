package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.domain.io.IOBatchesMessageTypes
import com.vocalink.crossproduct.domain.io.IOData
import com.vocalink.crossproduct.domain.io.IODataAmountDetails
import com.vocalink.crossproduct.domain.io.IODataDetails
import com.vocalink.crossproduct.domain.io.IODetails
import com.vocalink.crossproduct.domain.io.IOTransactionsMessageTypes
import com.vocalink.crossproduct.domain.io.ParticipantIOData
import com.vocalink.crossproduct.shared.io.CPIOBatchesMessageTypes
import com.vocalink.crossproduct.shared.io.CPIOData
import com.vocalink.crossproduct.shared.io.CPIODataAmountDetails
import com.vocalink.crossproduct.shared.io.CPIODataDetails
import com.vocalink.crossproduct.shared.io.CPIODetails
import com.vocalink.crossproduct.shared.io.CPIOTransactionsMessageTypes
import com.vocalink.crossproduct.shared.io.CPParticipantIOData
import com.vocalink.crossproduct.shared.participant.ParticipantStatus
import com.vocalink.crossproduct.shared.participant.ParticipantType
import com.vocalink.crossproduct.ui.dto.IODashboardDto
import com.vocalink.crossproduct.ui.dto.io.IODataDetailsDto
import com.vocalink.crossproduct.ui.dto.io.IODataDto
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto
import com.vocalink.crossproduct.ui.dto.io.ParticipantIODataDto
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto
import java.time.LocalDate

class MockIOData {
    fun getIODetails() = IODetails.builder()
            .batches(getIOBatchesMessageTypes())
            .files(getIODataDetails())
            .transactions(getIOTransactionsMessageTypes())
            .build()

    val ioDashboardDto = IODashboardDto.builder()
            .rows(getParticipantsIODataDto())
            .batchesRejected("2.00")
            .filesRejected("2.00")
            .transactionsRejected("2.00")
            .dateFrom(LocalDate.now())
            .build()

    fun getIODetailsDto() = IODetailsDto.builder()
            .participant(MockParticipants().getParticipantDto(false))
            .dateFrom(LocalDate.now())
            .batches(emptyList())
            .transactions(emptyList())
            .files(IODataDetailsDto.builder().build())
            .build()

    fun getCPIODetails() = listOf(
            CPIODetails.builder()
                    .files(getCPIODataDetails())
                    .schemeParticipantIdentifier("NDEASESSXXX")
                    .batches(getCPIOBatchesMessageTypes())
                    .transactions(getCPIOTransactionsMessageTypes())
                    .build()
    )

    fun getCPIOBatchesMessageTypes() = listOf(
            CPIOBatchesMessageTypes.builder()
                    .name("Customer Credit Transfer")
                    .code("Pacs.008")
                    .data(getCPIODataDetails())
                    .build(),
            CPIOBatchesMessageTypes.builder()
                    .name("Payment Return")
                    .code("Pacs.004")
                    .data(getCPIODataDetails())
                    .build(),
            CPIOBatchesMessageTypes.builder()
                    .name("Payment Reversal")
                    .code("Pacs.002")
                    .data(getCPIODataDetails())
                    .build()
    )

    fun getIOBatchesMessageTypes() = listOf(
            IOBatchesMessageTypes.builder()
                    .name("Customer Credit Transfer")
                    .code("Pacs.008")
                    .data(getIODataDetails())
                    .build(),
            IOBatchesMessageTypes.builder()
                    .name("Payment Return")
                    .code("Pacs.004")
                    .data(getIODataDetails())
                    .build(),
            IOBatchesMessageTypes.builder()
                    .name("Payment Reversal")
                    .code("Pacs.002")
                    .data(getIODataDetails())
                    .build()
    )

    fun getCPIOTransactionsMessageTypes() = listOf(
            CPIOTransactionsMessageTypes.builder()
                    .name("Customer Credit Transfer")
                    .code("Pacs.008")
                    .data(getCPIODataAmountDetails())
                    .build(),
            CPIOTransactionsMessageTypes.builder()
                    .name("Payment Return")
                    .code("Pacs.004")
                    .data(getCPIODataAmountDetails())
                    .build(),
            CPIOTransactionsMessageTypes.builder()
                    .name("Payment Reversal")
                    .code("Pacs.002")
                    .data(getCPIODataAmountDetails())
                    .build()
    )

    fun getIOTransactionsMessageTypes() = listOf(
            IOTransactionsMessageTypes.builder()
                    .name("Customer Credit Transfer")
                    .code("Pacs.008")
                    .data(getIODataAmountDetails())
                    .build(),
            IOTransactionsMessageTypes.builder()
                    .name("Payment Return")
                    .code("Pacs.004")
                    .data(getIODataAmountDetails())
                    .build(),
            IOTransactionsMessageTypes.builder()
                    .name("Payment Reversal")
                    .code("Pacs.002")
                    .data(getIODataAmountDetails())
                    .build()
    )

    fun getCPIODataAmountDetails() = CPIODataAmountDetails.builder()
            .amountAccepted(10)
            .amountOutput(10)
            .accepted(10)
            .output(10)
            .rejected(1.50)
            .submitted(10)
            .build()

    fun getCPIODataDetails() = CPIODataDetails.builder()
            .accepted(10)
            .output(10)
            .rejected(1.50)
            .submitted(10)
            .build()

    fun getIODataAmountDetails() = IODataAmountDetails.builder()
            .amountAccepted(10)
            .amountOutput(10)
            .accepted(10)
            .output(10)
            .rejected(1.50)
            .submitted(10)
            .build()

    fun getIODataDetails() = IODataDetails.builder()
            .accepted(10)
            .output(10)
            .rejected(1.50)
            .submitted(10)
            .build()

    val cpParticipantIOData = listOf(
            CPParticipantIOData.builder()
                    .participantId("ESSESESS")
                    .batches(CPIOData.builder()
                            .submitted(1)
                            .rejected(1.00)
                            .build())
                    .files(CPIOData.builder()
                            .submitted(1)
                            .rejected(1.00)
                            .build())
                    .transactions(CPIOData.builder()
                            .submitted(1)
                            .rejected(1.00)
                            .build())
                    .build(),
            CPParticipantIOData.builder()
                    .participantId("HANDSESS")
                    .batches(CPIOData.builder()
                            .submitted(10)
                            .rejected(1.00)
                            .build())
                    .files(CPIOData.builder()
                            .submitted(10)
                            .rejected(1.00)
                            .build())
                    .transactions(CPIOData.builder()
                            .submitted(10)
                            .rejected(1.00)
                            .build())
                    .build(),
            CPParticipantIOData.builder()
                    .participantId("NDEASESSXXX")
                    .batches(CPIOData.builder()
                            .submitted(0)
                            .rejected(0.00)
                            .build())
                    .files(CPIOData.builder()
                            .submitted(0)
                            .rejected(0.00)
                            .build())
                    .transactions(CPIOData.builder()
                            .submitted(0)
                            .rejected(0.00)
                            .build())
                    .build())

    fun getParticipantsIODataDto() = listOf(
            ParticipantIODataDto.builder()
                    .participant(ParticipantDto.builder()
                            .id("ESSESESS")
                            .bic("ESSESESS")
                            .name("SEB Bank")
                            .suspendedTime(null)
                            .status(ParticipantStatus.ACTIVE)
                            .participantType(ParticipantType.DIRECT_ONLY)
                            .build())
                    .batches(IODataDto.builder()
                            .submitted(1)
                            .rejected(1.00)
                            .build())
                    .files(IODataDto.builder()
                            .submitted(1)
                            .rejected(1.00)
                            .build()).transactions(IODataDto.builder()
                            .submitted(1)
                            .rejected(1.00)
                            .build())
                    .build(),
            ParticipantIODataDto.builder()
                    .participant(MockParticipants().getParticipantDto(true))
                    .batches(IODataDto.builder()
                            .submitted(10)
                            .rejected(1.00)
                            .build())
                    .files(IODataDto.builder()
                            .submitted(10)
                            .rejected(1.00)
                            .build()).transactions(IODataDto.builder()
                            .submitted(10)
                            .rejected(1.00)
                            .build())
                    .build(),
            ParticipantIODataDto.builder()
                    .participant(MockParticipants().getParticipantDto(false))
                    .batches(IODataDto.builder()
                            .submitted(0)
                            .rejected(0.00)
                            .build())
                    .files(IODataDto.builder()
                            .submitted(0)
                            .rejected(0.00)
                            .build()).transactions(IODataDto.builder()
                            .submitted(0)
                            .rejected(0.00)
                            .build())
                    .build())

    fun getParticipantsIOData() = listOf(
            ParticipantIOData.builder()
                    .participantId("ESSESESS")
                    .batches(IOData.builder()
                            .submitted(1)
                            .rejected(1.00)
                            .build())
                    .files(IOData.builder()
                            .submitted(1)
                            .rejected(1.00)
                            .build())
                    .transactions(IOData.builder()
                            .submitted(1)
                            .rejected(1.00)
                            .build())
                    .build(),
            ParticipantIOData.builder()
                    .participantId("HANDSESS")
                    .batches(IOData.builder()
                            .submitted(10)
                            .rejected(1.00)
                            .build())
                    .files(IOData.builder()
                            .submitted(10)
                            .rejected(1.00)
                            .build())
                    .transactions(IOData.builder()
                            .submitted(10)
                            .rejected(1.00)
                            .build())
                    .build(),
            ParticipantIOData.builder()
                    .participantId("NDEASESSXXX")
                    .batches(IOData.builder()
                            .submitted(0)
                            .rejected(0.00)
                            .build())
                    .files(IOData.builder()
                            .submitted(0)
                            .rejected(0.00)
                            .build())
                    .transactions(IOData.builder()
                            .submitted(0)
                            .rejected(0.00)
                            .build())
                    .build())
}