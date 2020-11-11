package com.vocalink.crossproduct.infrastructure.adapter

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.cycle.BPSCyclesClient
import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory
import com.vocalink.crossproduct.mocks.MockCycles
import com.vocalink.crossproduct.shared.cycle.CPCycle
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CycleRepositoryAdapterTest {

    private val clientFactory = mock(ClientFactory::class.java)!!
    private val cyclesClient = mock(BPSCyclesClient::class.java)!!
    private val cycleRepositoryAdapter = CycleRepositoryAdapter(clientFactory)

    @Test
    fun `should find all cycles`() {
        `when`(clientFactory.getCyclesClient(TestConstants.CONTEXT))
                .thenReturn(cyclesClient)
        `when`(cyclesClient.findAll()).thenReturn(MockCycles().cpCycles)

        val result = cycleRepositoryAdapter.findAll(TestConstants.CONTEXT)

        assertEquals(2, result.size)
        assertTrue(result[0] is Cycle)

        assertEquals("01", result[0].id)
        assertEquals(CycleStatus.COMPLETED, result[0].status)

        assertEquals("02", result[1].id)
        assertEquals(CycleStatus.OPEN, result[1].status)
    }

    @Test
    fun `should find cycles by id`() {
        `when`(clientFactory.getCyclesClient(TestConstants.CONTEXT))
                .thenReturn(cyclesClient)
        val cycleIds = listOf("01", "03")
        val cpCycles = listOf(
                CPCycle.builder()
                        .cutOffTime(LocalDateTime.of(2019, 12, 10, 10, 10))
                        .settlementTime(LocalDateTime.of(2019, 12, 10, 12, 10))
                        .id("01")
                        .status("COMPLETED")
                        .build(),
                CPCycle.builder()
                        .cutOffTime(LocalDateTime.of(2019, 12, 10, 15, 10))
                        .settlementTime(LocalDateTime.of(2019, 12, 10, 18, 10))
                        .id("03")
                        .status(null)
                        .build())

        `when`(cyclesClient.findByIds(cycleIds)).thenReturn(cpCycles)

        val result = cycleRepositoryAdapter.findByIds(TestConstants.CONTEXT, cycleIds)

        assertEquals(2, result.size)
        assertTrue(result[0] is Cycle)

        assertEquals("01", result[0].id)
        assertEquals(CycleStatus.COMPLETED, result[0].status)

        assertEquals("03", result[1].id)
        assertNull(result[1].status)
    }
}