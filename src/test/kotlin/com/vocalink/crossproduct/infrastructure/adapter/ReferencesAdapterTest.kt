package com.vocalink.crossproduct.infrastructure.adapter

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.reference.BPSReferencesClient
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory
import com.vocalink.crossproduct.shared.reference.CPMessageDirectionReference
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.atLeastOnce

class ReferencesAdapterTest {

    private val clientFactory = Mockito.mock(ClientFactory::class.java)!!
    private val client = Mockito.mock(BPSReferencesClient::class.java)!!
    private val testingModule = ReferencesAdapter(clientFactory)

    @Test
    fun `should find alert references`() {
        val messageDirectionReferences = listOf(CPMessageDirectionReference.builder().build())

        Mockito.`when`(clientFactory.getReferencesClient(any()))
                .thenReturn(client)
        Mockito.`when`(client.findMessageDirectionReferences())
                .thenReturn(messageDirectionReferences)

        testingModule.findMessageDirectionReferences(TestConstants.CONTEXT)

        Mockito.verify(clientFactory, atLeastOnce()).getReferencesClient(TestConstants.CONTEXT)
        Mockito.verify(client, atLeastOnce()).findMessageDirectionReferences()
    }
}