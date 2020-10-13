package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.repository.ParticipantIODataRepository
import com.vocalink.crossproduct.repository.ParticipantRepository
import com.vocalink.crossproduct.domain.ParticipantStatus
import com.vocalink.crossproduct.mocks.MockIOData
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExtendWith(SpringExtension::class)
class InputOutputFacadeImplTest {

    private val participantIODataRepository = Mockito.mock(ParticipantIODataRepository::class.java)!!
    private val presenterFactory = Mockito.mock(PresenterFactory::class.java)!!
    private val participantRepository = Mockito.mock(ParticipantRepository::class.java)!!
    private val uiPresenter = Mockito.mock(UIPresenter::class.java)!!

    private var testingModule = InputOutputFacadeImpl(
            participantIODataRepository,
            participantRepository,
            presenterFactory
    )

    @Test
    fun `should get participant IO data`() {
        val mockModel = MockIOData().ioDashboardDto
        val time = LocalDate.now()
        Mockito.`when`(participantRepository.findAll(TestConstants.CONTEXT))
                .thenReturn(MockParticipants().participants)
        Mockito.`when`(participantIODataRepository.findByTimestamp(TestConstants.CONTEXT, time))
                .thenReturn(MockIOData().getParticipantsIOData())
        Mockito.`when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
        Mockito.`when`(uiPresenter.presentInputOutput(any(), any(), any()))
                .thenReturn(mockModel)

        val result = testingModule.getInputOutputDashboard(TestConstants.CONTEXT, ClientType.UI, time)

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
}