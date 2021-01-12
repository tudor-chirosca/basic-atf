package com.vocalink.crossproduct.infrastructure.bps.repository

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatchRepository
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@BPSTestConfiguration
@Import(BPSBatchRepository::class)
class BPSBatchRepositoryTest @Autowired constructor(var batchRepository: BPSBatchRepository,
                                                    var mockServer: WireMockServer) {
        companion object {
        const val VALID_BATCHES_REQUEST: String = """ 
        {
            "offset": 0,
            "limit": 20,
            "sort": null,
            "status": null,
            "id": null,
            "date_from": "2021-01-03",
            "date_to": null,
            "cycle_ids": null,
            "msg_direction": "Sending",
            "msg_type": null,
            "send_bic": null,
            "recv_bic": null,
            "reason_code": null
        }"""
        const val VALID_BATCH_REQUEST: String = """ 
        {
            "batchId": "C27ISTXBANKSESS"
        }"""
        const val VALID_BATCH_RESULT_LIST_RESPONSE: String = """
            {
             "totalResults": 1,
             "items": [
                  {
                     "batchId": "C27ISTXBANKSESS",
                     "name": "G27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800107.gz",
                     "createdAt": "2020-10-23T13:43:00Z",
                     "fileSize": 3245234523,
                     "cycle" : {
                        "cycleId": "02",
                        "status": "ACTIVE",
                        "settlementTime": "2020-11-03T16:58:19Z",
                        "fileSubmissionCutOffTime": "2020-12-09T15:58:19Z",
                        "isNextDayCycle": false,
                        "settlementConfirmationTime": "2020-12-09T15:58:19Z"
                     },
                     "receivingBic": "FORXSES1",
                     "messageType": "prtp.005-prtp.006",
                     "messageDirection": "Receiving",
                     "nrOfTransactions": 12,
                     "status": "Accepted",
                     "reasonCode": null,
                     "sender": {
                       "entityName": "Nordea Bank",
                       "entityBic": "NDEASESSXXX",
                       "iban": "SE91 9500 0099 6042 0638 7369",
                       "fullName": "Lisa Engstrøm"
                     }
                 }                
            ]
        }
    """
        const val VALID_BATCH_RESPONSE: String = """
        {
             "batchId": "C27ISTXBANKSESS",
             "name": "G27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800107.gz",
             "createdAt": "2020-10-23T13:43:00Z",
             "fileSize": 3245234523,
             "cycle" : {
               "cycleId": "02",
               "status": "ACTIVE",
               "settlementTime": "2020-11-03T16:58:19Z",
               "fileSubmissionCutOffTime": "2020-12-09T15:58:19Z",
               "isNextDayCycle": false,
               "settlementConfirmationTime": "2020-12-09T15:58:19Z"
             },
             "receivingBic": "FORXSES1",
             "messageType": "prtp.005-prtp.006",
             "messageDirection": "Receiving",
             "nrOfTransactions": 12,
             "status": "Accepted",
             "reasonCode": null,
             "sender": {
               "entityName": "Nordea Bank",
               "entityBic": "NDEASESSXXX",
               "iban": "SE91 9500 0099 6042 0638 7369",
               "fullName": "Lisa Engstrøm"
             }
        }      
    """
    }

    @Test
    fun `should return the list of batches`() {
        mockServer.stubFor(
                post(urlEqualTo("/enquiry/batches"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_BATCH_RESULT_LIST_RESPONSE))
                        .withRequestBody(equalToJson(VALID_BATCHES_REQUEST)))

        val request = BatchEnquirySearchCriteria(
                0, 20, null, LocalDate.of(2021,1,3),
                null, null, "Sending", null, null,
                null, null, null, null
        )

        val result = batchRepository.findPaginated(request)

        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(1)
        assertThat(result.items).isNotEmpty

        assertThat(result.items[0].settlementCycleId).isEqualTo("02")
        assertThat(result.items[0].sender.entityBic).isEqualTo("NDEASESSXXX")
        assertThat(result.items[0].reasonCode).isNull()
        assertThat(result.items[0].nrOfTransactions).isEqualTo(12)
        assertThat(result.items[0].messageType).isEqualTo("prtp.005-prtp.006")
        assertThat(result.items[0].status).isEqualTo("Accepted")
    }

    @Test
    fun `should return the list of batch for given id`() {
        mockServer.stubFor(
                post(urlEqualTo("/enquiry/batches/read"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_BATCH_RESPONSE))
                        .withRequestBody(equalToJson(VALID_BATCH_REQUEST)))

        val result = batchRepository.findById("C27ISTXBANKSESS")

        assertThat(result).isNotNull
        assertThat(result.batchId).isEqualTo("C27ISTXBANKSESS")
        assertThat(result.settlementCycleId).isEqualTo("02")
        assertThat(result.sender.entityBic).isEqualTo("NDEASESSXXX")
        assertThat(result.reasonCode).isNull()
        assertThat(result.nrOfTransactions).isEqualTo(12)
        assertThat(result.messageType).isEqualTo("prtp.005-prtp.006")
        assertThat(result.status).isEqualTo("Accepted")
        assertThat(result.sender.entityName).isEqualTo("Nordea Bank")
        assertThat(result.sender.entityBic).isEqualTo("NDEASESSXXX")
    }
}