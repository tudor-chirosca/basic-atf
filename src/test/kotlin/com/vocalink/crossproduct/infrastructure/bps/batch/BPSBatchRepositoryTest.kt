package com.vocalink.crossproduct.infrastructure.bps.batch

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.time.LocalDate

@BPSTestConfiguration
@Import(BPSBatchRepository::class)
class BPSBatchRepositoryTest @Autowired constructor(var batchRepository: BPSBatchRepository,
                                                    var mockServer: WireMockServer) {
    companion object {
        const val VALID_BATCHES_REQUEST: String = """ 
        {
          "createdFromDate": "2021-01-03T00:00:00Z",
          "createdToDate": "2021-01-04T23:59:59Z",
          "messageDirection" : "Sending"
        }"""

        const val VALID_BATCH_REQUEST: String = """ 
        {
            "messageIdentifier": "C27ISTXBANKSESS"
        }"""

        const val VALID_BATCH_RESULT_LIST_RESPONSE: String = """
             {
                "data": [
                    {
                        "instructionId": "003",
                        "messageIdentifier": "C27ISTXBANKSESS",
                        "createdDateTime": "2020-10-23T13:43:00Z",
                        "originator": "NDEASESSXXX",
                        "messageType": "prtp.005-prtp.006",
                        "nrOfTransactions": 12,
                        "status": "Accepted"
                    }
                ],
                "summary": {
                    "offset": 0,
                    "pageSize": 20,
                    "totalCount": 1
                }
            }
            """

        const val VALID_BATCH_RESPONSE: String = """
       {
            "instructionId": "001",
            "numberOfTransactions": 12,
            "batchId": "C27ISTXBANKSESS",
            "fileName": "G27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800107.gz",
            "sentDateAndTime": "2020-10-23T13:43:00Z",
            "messageType": "prtp.005-prtp.006",
            "status": "Accepted",
            "reasonCode": null,
            "settlementCycle": "02",
            "settlementDate": "2020-11-03T16:58:19Z",
            "senderBank": "Nordea Bank",
            "senderBic": "NDEASESSXXX",
            "senderIban": "SE91 9500 0099 6042 0638 7369"
       }
    """
    }

    @Test
    fun `should return the list of batches`() {
        mockServer.stubFor(
                post(urlPathEqualTo("/enquiries/batches/P27-SEK/readAll"))
                        .withRequestBody(equalToJson(VALID_BATCHES_REQUEST))
                        .withQueryParam("offset", WireMock.equalTo("0"))
                        .withQueryParam("pageSize", WireMock.equalTo("20"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_BATCH_RESULT_LIST_RESPONSE)))

        val request = BatchEnquirySearchCriteria(
                0, 20, LocalDate.of(2021, 1, 3), LocalDate.of(2021, 1, 4),
                null, "Sending", null,  null, null,
                null, null, null, listOf())

        val result = batchRepository.findPaginated(request)

        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(1)
        assertThat(result.items).isNotEmpty

        assertThat(result.items[0].batchId).isEqualTo("C27ISTXBANKSESS")
        assertThat(result.items[0].createdAt).isEqualTo("2020-10-23T13:43Z")
        assertThat(result.items[0].senderBic).isEqualTo("NDEASESSXXX")
        assertThat(result.items[0].messageType).isEqualTo("prtp.005-prtp.006")
        assertThat(result.items[0].nrOfTransactions).isEqualTo(12)
        assertThat(result.items[0].status).isEqualTo("Accepted")
    }

    @Test
    fun `should return the list of batch for given id`() {
        mockServer.stubFor(
                post(urlEqualTo("/enquiries/batches/P27-SEK/read"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_BATCH_RESPONSE))
                        .withRequestBody(equalToJson(VALID_BATCH_REQUEST)))

        val result = batchRepository.findById("C27ISTXBANKSESS")

        assertThat(result).isNotNull
        assertThat(result.batchId).isEqualTo("C27ISTXBANKSESS")
        assertThat(result.settlementCycleId).isEqualTo("02")
        assertThat(result.senderBic).isEqualTo("NDEASESSXXX")
        assertThat(result.reasonCode).isNull()
        assertThat(result.nrOfTransactions).isEqualTo(12)
        assertThat(result.messageType).isEqualTo("prtp.005-prtp.006")
        assertThat(result.status).isEqualTo("Accepted")
    }
}