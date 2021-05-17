package com.vocalink.crossproduct.infrastructure.bps.service

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.DOWNLOAD_FILE_PATH
import com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.DOWNLOAD_REPORT_PATH
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.io.ByteArrayOutputStream

@BPSTestConfiguration
@Import(BPSResourceService::class)
class BPSResourceServiceTest @Autowired constructor(
        var downloadService: BPSResourceService,
        var mockServer: WireMockServer,
        var properties: BPSProperties) {

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `should write to output stream report data for id` () {
        val fileId = "10000000004"
        val url = BPSPathUtils.resolve(DOWNLOAD_REPORT_PATH, properties, properties.schemeCode, fileId).path
        val outputStream = ByteArrayOutputStream()
        mockServer.stubFor(
                post(urlPathEqualTo(url))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                                .withHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=$fileId")
                                .withHeader(HttpHeaders.CONTENT_LENGTH, "3")
                                .withBody(byteArrayOf(123, 12, 15)))
        )

        downloadService.writeResourceToOutputStream(DOWNLOAD_REPORT_PATH, fileId, outputStream)

        assertThat(outputStream.toByteArray()).containsExactly(123, 12, 15)
    }

    @Test
    fun `should write to output stream file data for id` () {
        val fileName = "A27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800103.gz"
        val url = BPSPathUtils.resolve(DOWNLOAD_FILE_PATH, properties, properties.schemeCode, fileName).path
        val outputStream = ByteArrayOutputStream()
        mockServer.stubFor(
                post(urlPathEqualTo(url))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                                .withHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=$fileName")
                                .withHeader(HttpHeaders.CONTENT_LENGTH, "3")
                                .withBody(byteArrayOf(123, 12, 15)))
        )

        downloadService.writeResourceToOutputStream(DOWNLOAD_FILE_PATH, fileName, outputStream)

        assertThat(outputStream.toByteArray()).containsExactly(123, 12, 15)
    }
}