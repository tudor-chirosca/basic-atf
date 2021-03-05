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
            "fileId": "A27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800103.gz"
        }"""
        const val VALID_FILE_REFERENCES_RESPONSE: String = """
         [
            {
                "status": "Rejected",
                "hasReason": true,
                "reasonCodes": [
                    "F01",
                    "F02",
                    "F03",
                    "F04",
                    "F05",
                    "F06"
                ],
                "enquiryType": "FILES"
            },
            {
                "status": "Accepted",
                "hasReason": false,
                "enquiryType": "FILES"
            }
        ]
        """

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
                  "settlementDate": "2020-11-13T10:00:28Z",
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
             "settlementDate": "2020-11-13T10:00:28Z",
             "schemeParticipantIdentifier": "AABASESSXXX"
        }               
    """
    }

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `post for file references with success`() {
        mockServer.stubFor(
                post(urlEqualTo("/reference/files"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_FILE_REFERENCES_RESPONSE)))

        val result = fileRepository.findFileReferences()

        assertThat(result).isNotEmpty
        assertThat(result[0].status).isEqualTo("Rejected")
        assertThat(result[0].isHasReason).isEqualTo(true)
        assertThat(result[1].status).isEqualTo("Accepted")
        assertThat(result[1].isHasReason).isEqualTo(false)
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
                .dateFrom(LocalDate.of(2021, 1, 3))
                .dateTo(LocalDate.of(2021, 1, 4))
                .messageDirection("Sending")
                .build()

        val result = fileRepository.findBy(request)

        assertThat(result).isNotNull
        assertThat(result.summary.totalCount).isEqualTo(1)
        assertThat(result.summary.offset).isEqualTo(0)
        assertThat(result.summary.pageSize).isEqualTo(20)

        assertThat(result.data).isNotEmpty
        assertThat(result.data[0].instructionId).isEqualTo("2641")
        assertThat(result.data[0].fileName).isEqualTo("P27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800102.gz")
        assertThat(result.data[0].fileSize).isEqualTo(20954L)
        assertThat(result.data[0].createdDate).isEqualTo("2020-12-28T17:32:28Z")
        assertThat(result.data[0].originator).isEqualTo("NDEASESSXXX")
        assertThat(result.data[0].messageType).isEqualTo("pacs.008")
        assertThat(result.data[0].nrOfBatches).isEqualTo(12)
        assertThat(result.data[0].status).isEqualTo("DEBULKED")
        assertThat(result.data[0].reasonCode).isEqualTo("F001")
        assertThat(result.data[0].settlementCycle).isEqualTo("20201113003")
        assertThat(result.data[0].settlementDate).isEqualTo("2020-11-13T10:00:28Z")
        assertThat(result.data[0].schemeParticipantIdentifier).isEqualTo("AABASESSXXX")
    }

    @Test
    fun `should return the file for given id`() {
        mockServer.stubFor(
                post(urlEqualTo("/enquiry/files/read"))
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
