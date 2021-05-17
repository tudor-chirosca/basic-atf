package com.vocalink.crossproduct.infrastructure.bps.file

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime

@BPSTestConfiguration
@Import(BPSFileRepository::class)
class BPSFileRepositoryTest @Autowired constructor(var fileRepository: BPSFileRepository,
                                                   var mockServer: WireMockServer) {

    companion object {
        const val VALID_FILES_REQUEST: String = """ 
        {
            "createdFromDate": "2021-01-03T00:00:00Z",
            "createdToDate": "2021-01-04T23:59:59Z",
            "messageDirection": "Sending"
        }"""
        const val VALID_FILE_REQUEST: String = """ 
        {
            "fileName": "A27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800103.gz"
        }"""
        const val VALID_FILE_RESULT_LIST_RESPONSE: String = """
            {
            "data": [
                {
                  "instructionId": "2641",
                  "fileName": "P27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800102.gz",
                  "fileSize": "20954",
                  "createdDate": "2020-12-28T17:32:28Z",
                  "originator": "NDEASESSXXX",
                  "messageType": "pacs.008",
                  "nrOfBatches": "12",
                  "status": "DEBULKED",
                  "reasonCode": "F001",
                  "settlementCycle": "20201113003",
                  "schemeParticipantIdentifier": "AABASESSXXX"
                }
            ],
            "summary": {
                "pageSize": 20,
                "offset": 0,
                "totalCount": 1
            }
        }
    """
        const val VALID_FILE_RESPONSE: String = """
        {
             "instructionId": "2641",
             "fileName": "P27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800102.gz",
             "fileSize": "20954",
             "createdDate": "2020-12-28T17:32:28Z",
             "originator": "NDEASESSXXX",
             "messageType": "prtp.005-prtp.006",
             "nrOfBatches": "12",
             "status": "Accepted",
             "reasonCode": "F001",
             "settlementCycle": "20201113003",
             "schemeParticipantIdentifier": "AABASESSXXX"
        }               
    """
    }

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `should return the list of file enquiries`() {
        mockServer.stubFor(
                post(urlPathEqualTo("/enquiries/file/P27-SEK/readAll"))
                        .withRequestBody(equalToJson(VALID_FILES_REQUEST))
                        .withQueryParam("offset", equalTo("0"))
                        .withQueryParam("pageSize", equalTo("20"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_FILE_RESULT_LIST_RESPONSE))
        )
        val request = FileEnquirySearchCriteria.builder()
                .offset(0)
                .limit(20)
                .dateFrom(ZonedDateTime.of(LocalDate.of(2021, 1, 3), LocalTime.MIN, ZoneId.of("UTC")))
                .dateTo(ZonedDateTime.of(2021, Month.JANUARY.value, 4, 23, 59, 59, 0, ZoneId.of("UTC")))
                .messageDirection("Sending")
                .build()

        val result = fileRepository.findBy(request)

        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(1)

        assertThat(result.items).isNotEmpty
        assertThat(result.items[0].instructionId).isEqualTo("2641")
        assertThat(result.items[0].fileName).isEqualTo("P27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800102.gz")
        assertThat(result.items[0].fileSize).isEqualTo(20954L)
        assertThat(result.items[0].createdDate).isEqualTo("2020-12-28T17:32:28Z")
        assertThat(result.items[0].originator).isEqualTo("NDEASESSXXX")
        assertThat(result.items[0].messageType).isEqualTo("pacs.008")
        assertThat(result.items[0].nrOfBatches).isEqualTo(12)
        assertThat(result.items[0].status).isEqualTo("DEBULKED")
        assertThat(result.items[0].reasonCode).isEqualTo("F001")
        assertThat(result.items[0].settlementCycle).isEqualTo("20201113003")
        assertThat(result.items[0].schemeParticipantIdentifier).isEqualTo("AABASESSXXX")
    }

    @Test
    fun `should return the file for given id`() {
        mockServer.stubFor(
                post(urlEqualTo("/enquiries/file/P27-SEK/read"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_FILE_RESPONSE))
                        .withRequestBody(equalToJson(VALID_FILE_REQUEST)))

        val result = fileRepository.findById("A27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800103.gz")

        assertThat(result).isNotNull
        assertThat(result.settlementCycle).isEqualTo("20201113003")
        assertThat(result.originator).isEqualTo("NDEASESSXXX")
        assertThat(result.reasonCode).isEqualTo("F001")
        assertThat(result.nrOfBatches).isEqualTo(12)
        assertThat(result.messageType).isEqualTo("prtp.005-prtp.006")
        assertThat(result.status).isEqualTo("Accepted")
    }
}
