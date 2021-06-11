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
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType

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
                  "createdDate": "2021-04-01T17:00:00Z",
                  "originator": "NDEASESSXXX",
                  "messageType": "pacs.008",
                  "noOfBatches": "12",
                  "status": "DEBULKED",
                  "reasonCode": "F001",
                  "settlementCycle": "2021040103",
                  "settlementDate": "2021-04-01T17:00:00Z",
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
             "createdDate": "2021-04-01T17:00:00Z",
             "originator": "NDEASESSXXX",
             "messageType": "prtp.005-prtp.006",
             "nrOfBatches": "12",
             "status": "Accepted",
             "reasonCode": "F001",
             "settlementCycle": "20201113003",
             "settlementDate": "2021-04-01T17:00:00Z",
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
        val date = ZonedDateTime.of(2021, Month.APRIL.value, 1, 17, 0, 0, 0, ZoneId.of("UTC"))

        val result = fileRepository.findBy(request)

        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(1)

        assertThat(result.items).isNotEmpty
        assertThat(result.items[0].instructionId).isEqualTo("2641")
        assertThat(result.items[0].fileName).isEqualTo("P27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800102.gz")
        assertThat(result.items[0].fileSize).isEqualTo(20954L)
        assertThat(result.items[0].createdDate).isEqualTo(date)
        assertThat(result.items[0].settlementDate).isEqualTo(date)
        assertThat(result.items[0].originator).isEqualTo("NDEASESSXXX")
        assertThat(result.items[0].messageType).isEqualTo("pacs.008")
        assertThat(result.items[0].nrOfBatches).isEqualTo(12)
        assertThat(result.items[0].status).isEqualTo("DEBULKED")
        assertThat(result.items[0].reasonCode).isEqualTo("F001")
        assertThat(result.items[0].settlementCycle).isEqualTo("2021040103")
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
        val date = ZonedDateTime.of(2021, Month.APRIL.value, 1, 17, 0, 0, 0, ZoneId.of("UTC"))

        val result = fileRepository.findById("A27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800103.gz")

        assertThat(result).isNotNull
        assertThat(result.settlementCycle).isEqualTo("20201113003")
        assertThat(result.originator).isEqualTo("NDEASESSXXX")
        assertThat(result.reasonCode).isEqualTo("F001")
        assertThat(result.nrOfBatches).isEqualTo(12)
        assertThat(result.messageType).isEqualTo("prtp.005-prtp.006")
        assertThat(result.status).isEqualTo("Accepted")
        assertThat(result.createdDate).isEqualTo(date)
        assertThat(result.settlementDate).isEqualTo(date)
    }

    @Test
    fun `should return the empty list on 404 NOT_FOUND`() {
        mockServer.stubFor(
                post(urlPathEqualTo("/enquiries/file/P27-SEK/readAll"))
                        .withRequestBody(equalToJson(VALID_FILES_REQUEST))
                        .withQueryParam("offset", equalTo("0"))
                        .withQueryParam("pageSize", equalTo("20"))
                        .willReturn(aResponse()
                                .withStatus(404)
                                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
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
        assertThat(result.items).isEmpty()
        assertThat(result.totalResults).isEqualTo(0)
    }

    @Test
    fun `should return the empty list on 204 NO_CONTENT`() {
        mockServer.stubFor(
                post(urlPathEqualTo("/enquiries/file/P27-SEK/readAll"))
                        .withRequestBody(equalToJson(VALID_FILES_REQUEST))
                        .withQueryParam("offset", equalTo("0"))
                        .withQueryParam("pageSize", equalTo("20"))
                        .willReturn(aResponse()
                                .withStatus(204)
                                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
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
        assertThat(result.items).isEmpty()
        assertThat(result.totalResults).isEqualTo(0)
    }
}
