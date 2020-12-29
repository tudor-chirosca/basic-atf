package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.io.IODetailsRepository
import com.vocalink.crossproduct.domain.io.ParticipantIODataRepository
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException
import com.vocalink.crossproduct.mocks.MockIOData
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class InputOutputFacadeImplTest {

    private val participantIODataRepository = mock(ParticipantIODataRepository::class.java)!!
    private val ioDetailsRepository = mock(IODetailsRepository::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val participantRepository = mock(ParticipantRepository::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!

    private val inputOutputFacadeImpl = InputOutputFacadeImpl(
            participantIODataRepository,
            ioDetailsRepository,
            participantRepository,
            presenterFactory
    )

    @Test
    fun `should get participant IO data`() {
        val mockModel = MockIOData().ioDashboardDto
        val time = LocalDate.now()
        `when`(participantRepository.findAll(TestConstants.CONTEXT))
                .thenReturn(MockParticipants().participants)
        `when`(participantIODataRepository.findByTimestamp(TestConstants.CONTEXT, time))
                .thenReturn(MockIOData().getParticipantsIOData())
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
        `when`(uiPresenter.presentInputOutput(any(), any(), any()))
                .thenReturn(mockModel)

        val result = inputOutputFacadeImpl.getInputOutputDashboard(TestConstants.CONTEXT, ClientType.UI, time)

        assertEquals("2.00", result.batchesRejected)
        assertEquals("2.00", result.filesRejected)
        assertEquals("2.00", result.transactionsRejected)

        assertEquals(3, result.rows.size)

        assertEquals("ESSESESS", result.rows[0].participant.id)
        assertEquals("ESSESESS", result.rows[0].participant.bic)
        assertEquals("SEB Bank", result.rows[0].participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.rows[0].participant.status)
        assertNull(result.rows[0].participant.suspendedTime)

        assertEquals(1, result.rows[0].batches.submitted)
        assertEquals(1.00, result.rows[0].batches.rejected)
        assertEquals(1, result.rows[0].files.submitted)
        assertEquals(1.00, result.rows[0].files.rejected)
        assertEquals(1, result.rows[0].transactions.submitted)
        assertEquals(1.00, result.rows[0].transactions.rejected)

        assertEquals("HANDSESS", result.rows[1].participant.id)
        assertEquals("HANDSESS", result.rows[1].participant.bic)
        assertEquals("Svenska Handelsbanken", result.rows[1].participant.name)
        assertEquals(ParticipantStatus.SUSPENDED, result.rows[1].participant.status)
        assertNotNull(result.rows[1].participant.suspendedTime)

        assertEquals(10, result.rows[1].batches.submitted)
        assertEquals(1.00, result.rows[1].batches.rejected)
        assertEquals(10, result.rows[1].files.submitted)
        assertEquals(1.00, result.rows[1].files.rejected)
        assertEquals(10, result.rows[1].transactions.submitted)
        assertEquals(1.00, result.rows[1].transactions.rejected)

        assertEquals("NDEASESSXXX", result.rows[2].participant.id)
        assertEquals("NDEASESSXXX", result.rows[2].participant.bic)
        assertEquals("Nordea", result.rows[2].participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.rows[2].participant.status)
        assertNull(result.rows[2].participant.suspendedTime)

        assertEquals(0, result.rows[2].batches.submitted)
        assertEquals(0.00, result.rows[2].batches.rejected)
        assertEquals(0, result.rows[2].files.submitted)
        assertEquals(0.00, result.rows[2].files.rejected)
        assertEquals(0, result.rows[2].transactions.submitted)
        assertEquals(0.00, result.rows[2].transactions.rejected)
    }

    @Test
    fun `should get participant IO Details data`() {
        val mockModel = MockIOData().getIODetailsDto()
        val date = LocalDate.now()
        val participantId = "NDEASESSXXX"

        `when`(participantRepository
                .findBy(TestConstants.CONTEXT, participantId))
                .thenReturn(Optional.of(MockParticipants().getParticipant(false)))

        `when`(ioDetailsRepository
                .findIODetailsFor(TestConstants.CONTEXT, participantId, date))
                .thenReturn(listOf(MockIOData().getIODetails()))

        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)

        `when`(uiPresenter.presentIoDetails(any(), any(), any()))
                .thenReturn(mockModel)

        val result = inputOutputFacadeImpl
                .getInputOutputDetails(TestConstants.CONTEXT, ClientType.UI, date, participantId)

        verify(uiPresenter).presentIoDetails(any(), any(), any())
        verify(participantRepository).findBy(any(), any())
        verify(ioDetailsRepository).findIODetailsFor(any(), any(), any())

        assertNotNull(result)
    }

    @Test
    fun `should throw error on io details if no participants for given id`() {
        val participantId = "fake_id"
        val date = LocalDate.now()

        `when`(participantRepository.findBy(TestConstants.CONTEXT, participantId))
                .thenReturn(Optional.empty())
        assertThrows(EntityNotFoundException::class.java) {
            inputOutputFacadeImpl.getInputOutputDetails(TestConstants.CONTEXT, ClientType.UI, date, participantId)
        }
    }

    @Test
    fun `should throw error on io details if no io details for given id or date`() {
        val participantId = "NDEASESSXXX"
        val date = LocalDate.now()

        `when`(participantRepository
                .findBy(TestConstants.CONTEXT, participantId))
                .thenReturn(Optional.of(MockParticipants().getParticipant(false)))

        `when`(ioDetailsRepository
                .findIODetailsFor(TestConstants.CONTEXT, participantId, date))
                .thenReturn(emptyList())

        assertThrows(EntityNotFoundException::class.java) {
            inputOutputFacadeImpl.getInputOutputDetails(TestConstants.CONTEXT, ClientType.UI, date, participantId)
        }
    }
}
