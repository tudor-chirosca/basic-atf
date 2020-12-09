package com.vocalink.crossproduct.infrastructure.adapter

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.batch.BPSBatchesClient
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory
import com.vocalink.crossproduct.shared.CPPage
import com.vocalink.crossproduct.shared.batch.CPBatch
import com.vocalink.crossproduct.shared.cycle.CPCycle
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest
import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify

class BatchesAdapterTest {

    private val clientFactory = Mockito.mock(ClientFactory::class.java)
    private val batchClient = Mockito.mock(BPSBatchesClient::class.java)
    private val batchesAdapter = BatchesAdapter(clientFactory)

    @Test
    fun `should invoke batch client when finding Batches`() {
        val batches = CPPage<CPBatch>(0, emptyList())

        `when`(clientFactory.getBatchesClient(any()))
                .thenReturn(batchClient)
        `when`(batchClient.findBatches(any()))
                .thenReturn(batches)

        batchesAdapter.findBatchesPaginated(TestConstants.CONTEXT, BatchEnquirySearchRequest())

        verify(clientFactory, atLeastOnce()).getBatchesClient(TestConstants.CONTEXT)
        verify(batchClient, atLeastOnce()).findBatches(any())
    }

    @Test
    fun `should invoke batches client when finding batch by id`() {
        val cycle = CPCycle.builder().settlementTime(LocalDateTime.now()).build()
        val batch = CPBatch.builder().cycle(cycle).build();

        `when`(clientFactory.getBatchesClient(any()))
                .thenReturn(batchClient)
        `when`(batchClient.findBatchById(any()))
                .thenReturn(batch)

        batchesAdapter.findBatchById(TestConstants.CONTEXT, "")

        verify(clientFactory, atLeastOnce()).getBatchesClient(TestConstants.CONTEXT)
        verify(batchClient, atLeastOnce()).findBatchById(any())
    }
}