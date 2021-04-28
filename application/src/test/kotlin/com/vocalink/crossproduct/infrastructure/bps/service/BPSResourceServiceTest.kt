package com.vocalink.crossproduct.infrastructure.bps.service

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@BPSTestConfiguration
@Import(BPSResourceService::class)
class BPSResourceServiceTest @Autowired constructor(
        var downloadService: BPSResourceService,
        var mockServer: WireMockServer) {

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `should return report data for id` () {
        val fileId = "10000000004"
        val url = "/reports/downloadFile/P27-SEK/$fileId"
        mockServer.stubFor(
                post(urlEqualTo(url))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                                .withHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=$fileId")
                                .withHeader(HttpHeaders.CONTENT_LENGTH, "3")
                                .withBody(byteArrayOf(123, 12, 15)))
        )
        val result = downloadService.getResource(ResourcePath.DOWNLOAD_REPORT_PATH, fileId)
        assertThat(result).hasBinaryContent(byteArrayOf(123, 12, 15))
    }

    @Test
    fun `should return file data for id` () {
        val fileName = "A27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800103.gz"
        val url = "/enquiries/file/downloadFile/P27-SEK/$fileName"
        mockServer.stubFor(
                post(urlEqualTo(url))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                                .withHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=$fileName")
                                .withHeader(HttpHeaders.CONTENT_LENGTH, "3")
                                .withBody(byteArrayOf(123, 12, 15)))
        )
        val result = downloadService.getResource(ResourcePath.DOWNLOAD_FILE_PATH, fileName)
        assertThat(result).hasBinaryContent(byteArrayOf(123, 12, 15))
    }
}