package com.vocalink.crossproduct.infrastructure.adapter

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.files.BPSFilesClient
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory
import com.vocalink.crossproduct.shared.CPPage
import com.vocalink.crossproduct.shared.files.CPFile
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class FilesAdapterTest {

    private val clientFactory = mock(ClientFactory::class.java)!!
    private val fileClient = mock(BPSFilesClient::class.java)!!
    private val filesAdapter = FilesAdapter(clientFactory)

    @Test
    fun `should invoke files client when finding Files`() {
        val files = CPPage<CPFile>(0, emptyList());

        `when`(clientFactory.getFilesClient(any()))
                .thenReturn(fileClient)
        `when`(fileClient.findFiles(any()))
                .thenReturn(files)

        filesAdapter.findFilesPaginated(TestConstants.CONTEXT, FileEnquirySearchRequest())

        verify(clientFactory, atLeastOnce()).getFilesClient(TestConstants.CONTEXT)
        verify(fileClient, atLeastOnce()).findFiles(any())
    }

    @Test
    fun `should invoke files client when finding file by id`() {
        val files = CPPage<CPFile>(0, emptyList());

        `when`(clientFactory.getFilesClient(any()))
                .thenReturn(fileClient)
        `when`(fileClient.findFilesByIds(any()))
                .thenReturn(files)

        filesAdapter.findFilesByIds(TestConstants.CONTEXT, listOf(""))

        verify(clientFactory, atLeastOnce()).getFilesClient(TestConstants.CONTEXT)
        verify(fileClient, atLeastOnce()).findFilesByIds(any())
    }

    @Test
    fun `should invoke files client when finding file reference`() {
        `when`(clientFactory.getFilesClient(any()))
                .thenReturn(fileClient)
        `when`(fileClient.findFileReferences())
                .thenReturn(emptyList())

        filesAdapter.findFileReferences(TestConstants.CONTEXT)

        verify(clientFactory, atLeastOnce()).getFilesClient(TestConstants.CONTEXT)
        verify(fileClient, atLeastOnce()).findFileReferences()
    }
}