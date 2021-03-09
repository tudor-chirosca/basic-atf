package com.vocalink.crossproduct.infrastructure.bps.transaction

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@BPSTestConfiguration
@Import(BPSTransactionRepository::class)
class BPSTransactionRepositoryTest @Autowired constructor(var transactionRepository: BPSTransactionRepository,
                                                          var mockServer: WireMockServer) {

    companion object {
        const val VALID_TRANSACTIONS_REQUEST: String = """ 
        {
            "createdDateFrom": "2021-02-18T00:00:00Z",
            "createdDateTo": null,
            "cycleDay": null,
            "cycleName": null,
            "messageDirection": "sending",
            "sendingParticipant": null,
            "receivingParticipant": null,
            "messageType": null,
            "status": null,
            "reasonCode": null,
            "instructionIdentifier": null,
            "sendingAccount": null,
            "receivingAccount": null,
            "valueDate": null,
            "transactionRangeFrom": null,
            "transactionRangeTo": {
                "amount": 10,
                "currency": "SEK"
            },
            "sortingOrder" : [ ]
        }"""

        const val VALID_TRANSACTION_REQUEST: String = """ 
        {
            "txnsInstructionId": "20210115SVEASES1B2215"
        }"""

        const val VALID_TRANSACTION_RESULT_LIST_RESPONSE: String = """
        {
            "data": [
                {
                    "instructionId": "20210115MEEOSES1B2187",
                    "createdDateTime": "2021-02-18T16:00:00Z",
                    "originator": "MEEOSES1",
                    "messageType": "camt.056",
                    "amount": {
                        "amount": 4234523.43,
                        "currency": "SEK"
                    },
                    "status": "ACSC"
                }
            ],
            "summary": {
                "offset": 0,
                "pageSize": 1,
                "totalCount": 8
            }
        }"""

        const val VALID_TRANSACTION_DETAILS_RESPONSE: String = """
            {
                "txnsInstructionId": "20210115IKANSE21B2167",
                "messageType": "pacs.002",
                "sentDateTime": "2021-02-18T18:00:00Z",
                "transactionStatus": "ACSC",
                "reasonCode": null,
                "settlementCycle": "20210218002",
                "settlementDate": "2021-02-18T14:00:00Z",
                "fileName": "E27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800101.gz",
                "batchId": "F27ISTXBANKSESS",
                "transactionAmount": {
                    "amount": 777777.77,
                    "currency": "SEK"
                },
                "sender": {
                    "entityName": "Ikano Bank",
                    "entityBic": "IKANSE21",
                    "iban": "SE23 9999 9999 9999 9999 2170",
                    "fullName": "Mark Markson"
                },
                "receiver": {
                    "entityName": "Nordea",
                    "entityBic": "NDEASESSXXX",
                    "iban": "SE23 9999 9999 9999 9999 2142",
                    "fullName": "John Smith"
                }
            }
        """
    }

    @Test
    fun `should return the list of transactions`() {
        mockServer.stubFor(
                post(urlPathEqualTo("/enquiries/transactions/P27-SEK/readAll"))
                        .withRequestBody(equalToJson(VALID_TRANSACTIONS_REQUEST))
                        .withQueryParam("offset", equalTo("0"))
                        .withQueryParam("pageSize", equalTo("1"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_TRANSACTION_RESULT_LIST_RESPONSE)))

        val request = TransactionEnquirySearchCriteria(
                0, 1, null, LocalDate.of(2021, Month.FEBRUARY.value, 18),
                null, null, null, "sending", null,
                null, null, null, null, null, 
                null, null, null, null, BigDecimal.TEN
        )
        val result = transactionRepository.findPaginated(request)
        assertThat(result).isNotNull
        assertThat(result.summary.totalCount).isEqualTo(8)
        assertThat(result.data).isNotEmpty

        val item = result.data[0]
        assertThat(item.instructionId).isEqualTo("20210115MEEOSES1B2187")
        assertThat(item.createdAt).isEqualTo(ZonedDateTime.of(2021, Month.FEBRUARY.value, 18, 16, 0, 0, 0, ZoneId.of("UTC")))
        assertThat(item.originator).isEqualTo("MEEOSES1")
        assertThat(item.messageType).isEqualTo("camt.056")
        assertThat(item.amount.amount).isEqualTo(BigDecimal.valueOf(4234523.43))
        assertThat(item.amount.currency).isEqualTo("SEK")
        assertThat(item.status).isEqualTo("ACSC")
    }

    @Test
    fun `should return transaction by id`() {
        mockServer.stubFor(
                post(urlEqualTo("/enquiries/transactions/P27-SEK/read"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_TRANSACTION_DETAILS_RESPONSE))
                        .withRequestBody(equalToJson(VALID_TRANSACTION_REQUEST)))

        val result = transactionRepository.findById("20210115SVEASES1B2215")
        assertThat(result.instructionId).isEqualTo("20210115IKANSE21B2167")
        assertThat(result.messageType).isEqualTo("pacs.002")
        assertThat(result.createdAt).isEqualTo(ZonedDateTime.of(2021, Month.FEBRUARY.value, 18, 18, 0, 0, 0, ZoneId.of("UTC")))
        assertThat(result.status).isEqualTo("ACSC")
        assertThat(result.reasonCode).isEqualTo(null)
        assertThat(result.settlementCycleId).isEqualTo("20210218002")
        assertThat(result.settlementDate).isEqualTo(LocalDate.of(2021, Month.FEBRUARY.value, 18))
        assertThat(result.fileName).isEqualTo("E27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800101.gz")
        assertThat(result.batchId).isEqualTo("F27ISTXBANKSESS")
        assertThat(result.amount.amount).isEqualTo(BigDecimal.valueOf(777777.77))
        assertThat(result.amount.currency).isEqualTo("SEK")

        assertThat(result.sender.entityName).isEqualTo("Ikano Bank")
        assertThat(result.sender.entityBic).isEqualTo("IKANSE21")
        assertThat(result.sender.iban).isEqualTo("SE23 9999 9999 9999 9999 2170")
        assertThat(result.sender.fullName).isEqualTo("Mark Markson")

        assertThat(result.receiver.entityName).isEqualTo("Nordea")
        assertThat(result.receiver.entityBic).isEqualTo("NDEASESSXXX")
        assertThat(result.receiver.iban).isEqualTo("SE23 9999 9999 9999 9999 2142")
        assertThat(result.receiver.fullName).isEqualTo("John Smith")
    }
}