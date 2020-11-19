package com.vocalink.crossproduct.infrastructure.adapter

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.reference.BPSReferencesClient
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory
import com.vocalink.crossproduct.shared.reference.CPMessageDirectionReference
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ReferencesAdapterTest {

    private val clientFactory = mock(ClientFactory::class.java)!!
    private val client = mock(BPSReferencesClient::class.java)!!
    private val referencesAdapter = ReferencesAdapter(clientFactory)

    @Test
    fun `should invoke references client`() {
        val messageDirectionReferences = listOf(CPMessageDirectionReference.builder().build())

        `when`(clientFactory.getReferencesClient(any()))
                .thenReturn(client)
        `when`(client.findMessageDirectionReferences())
                .thenReturn(messageDirectionReferences)

        referencesAdapter.findMessageDirectionReferences(TestConstants.CONTEXT)

        verify(clientFactory, atLeastOnce()).getReferencesClient(TestConstants.CONTEXT)
        verify(client, atLeastOnce()).findMessageDirectionReferences()
    }
}