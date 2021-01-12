package com.vocalink.crossproduct.infrastructure.bps.repository

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFileRepository
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@BPSTestConfiguration
@Import(BPSFileRepository::class)
class BPSFileRepositoryTest @Autowired constructor(var fileRepository: BPSFileRepository,
                                                   var mockServer: WireMockServer) {

    companion object {
        const val VALID_FILES_REQUEST: String = """ 
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
             "totalResults": 1,
             "items": [
                  {
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
                     "nrOfBatches": 12,
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
        const val VALID_FILE_RESPONSE: String = """
        {
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
             "nrOfBatches": 12,
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
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_FILE_REFERENCES_RESPONSE)))

        val result = fileRepository.findFileReferences("type")

        assertThat(result).isNotEmpty
        assertThat(result[0].status).isEqualTo("Rejected")
        assertThat(result[0].isHasReason).isEqualTo(true)
        assertThat(result[1].status).isEqualTo("Accepted")
        assertThat(result[1].isHasReason).isEqualTo(false)
    }

    @Test
    fun `should return the list of file enquiries`() {
        mockServer.stubFor(
                post(urlEqualTo("/enquiry/files"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_FILE_RESULT_LIST_RESPONSE))
                        .withRequestBody(equalToJson(VALID_FILES_REQUEST)))

        val request = FileEnquirySearchCriteria(
                0, 20, null, LocalDate.of(2021,1,3),
                null, null, "Sending", null, null,
                null, null, null, null
        )
        val result = fileRepository.findPaginated(request)

        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(1)
        assertThat(result.items).isNotEmpty

        assertThat(result.items[0].settlementCycleId).isEqualTo("02")
        assertThat(result.items[0].sender.entityBic).isEqualTo("NDEASESSXXX")
        assertThat(result.items[0].reasonCode).isNull()
        assertThat(result.items[0].nrOfBatches).isEqualTo(12)
        assertThat(result.items[0].messageType).isEqualTo("prtp.005-prtp.006")
        assertThat(result.items[0].status).isEqualTo("Accepted")
    }

    @Test
    fun `should return the file for given id`() {
        mockServer.stubFor(
                post(urlEqualTo("/enquiry/files/read"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_FILE_RESPONSE))
                        .withRequestBody(equalToJson(VALID_FILE_REQUEST)))

        val result = fileRepository.findById("A27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800103.gz")

        assertThat(result).isNotNull
        assertThat(result.settlementCycleId).isEqualTo("02")
        assertThat(result.sender.entityBic).isEqualTo("NDEASESSXXX")
        assertThat(result.reasonCode).isNull()
        assertThat(result.nrOfBatches).isEqualTo(12)
        assertThat(result.messageType).isEqualTo("prtp.005-prtp.006")
        assertThat(result.status).isEqualTo("Accepted")
    }
}