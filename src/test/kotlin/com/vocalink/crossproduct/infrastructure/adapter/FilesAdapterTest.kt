package com.vocalink.crossproduct.infrastructure.adapter

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.files.BPSFilesClient
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory
import com.vocalink.crossproduct.shared.CPPage
import com.vocalink.crossproduct.shared.cycle.CPCycle
import com.vocalink.crossproduct.shared.files.CPFile
import com.vocalink.crossproduct.shared.files.CPFileReference
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
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
        val cycle = CPCycle.builder().settlementTime(LocalDateTime.now()).build()
        val file = CPFile.builder().cycle(cycle).build();

        `when`(clientFactory.getFilesClient(any()))
                .thenReturn(fileClient)
        `when`(fileClient.findFileById(any()))
                .thenReturn(file)

        filesAdapter.findFileById(TestConstants.CONTEXT, "")

        verify(clientFactory, atLeastOnce()).getFilesClient(TestConstants.CONTEXT)
        verify(fileClient, atLeastOnce()).findFileById(any())
    }

    @Test
    fun `should invoke files client, filter for specified type and sort by status on file reference call`() {
        val filesType = "FILES"
        val batchesType = "BATCH"

        val refFilesAaa = CPFileReference()
        refFilesAaa.enquiryType = filesType
        refFilesAaa.status = "aaaaa"

        val refFilesBbb = CPFileReference()
        refFilesBbb.enquiryType = filesType
        refFilesBbb.status = "bbbb"

        val refBatches = CPFileReference()
        refBatches.enquiryType = batchesType
        refBatches.status = "bbbb"

        val references = listOf(refFilesBbb, refBatches, refFilesAaa)

        `when`(clientFactory.getFilesClient(any()))
                .thenReturn(fileClient)
        `when`(fileClient.findFileReferences())
                .thenReturn(references)

        val result = filesAdapter.findFileReferences(TestConstants.CONTEXT, filesType)

        verify(clientFactory, atLeastOnce()).getFilesClient(TestConstants.CONTEXT)
        verify(fileClient, atLeastOnce()).findFileReferences()

        assertThat(result.size).isEqualTo(2)
        assertThat(result[0].enquiryType).isEqualTo(filesType)
        assertThat(result[0].status).isEqualTo("aaaaa")
        assertThat(result[1].enquiryType).isEqualTo(filesType)
        assertThat(result[1].status).isEqualTo("bbbb")
    }
}