package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleRepository
import com.vocalink.crossproduct.infrastructure.exception.NonConsistentDataException
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto
import com.vocalink.crossproduct.ui.dto.settlement.LatestSettlementCyclesDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class SettlementsFacadeImplTest {

    private val repositoryFactory = mock(RepositoryFactory::class.java)
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val cycleRepository = mock(CycleRepository::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!

    private val settlementsServiceFacadeImpl = SettlementsFacadeImpl(
            presenterFactory,
            repositoryFactory
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getCycleRepository(anyString()))
                .thenReturn(cycleRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository on get settlement cycles`() {
        val latestCycles = listOf(Cycle.builder().build(), Cycle.builder().build())
        val latestCyclesDto = LatestSettlementCyclesDto.builder()
                .previousCycle(CycleDto.builder().build())
                .currentCycle(CycleDto.builder().build())
                .build()
        `when`(cycleRepository.findLatest(anyInt()))
                .thenReturn(latestCycles)
        `when`(uiPresenter.presentLatestCycles(any()))
                .thenReturn(latestCyclesDto)
        val result = settlementsServiceFacadeImpl.getLatestCycles(TestConstants.CONTEXT, ClientType.UI)

        verify(cycleRepository).findLatest(2)
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentLatestCycles(latestCycles)

        assertNotNull(result)
    }

    @Test
    fun `should throw NonConsistentData exception if repository returns less than 2 cycles`() {
        val latestCycles = listOf(Cycle.builder().build())
        `when`(cycleRepository.findLatest(anyInt()))
                .thenReturn(latestCycles)

        assertFailsWith<NonConsistentDataException>("Cycles repository returned less than 2 cycles") {
            settlementsServiceFacadeImpl.getLatestCycles(TestConstants.CONTEXT, ClientType.UI)
        }
    }
}
