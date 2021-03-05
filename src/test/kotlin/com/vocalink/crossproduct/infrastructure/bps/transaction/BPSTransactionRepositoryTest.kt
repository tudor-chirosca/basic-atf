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
            "createdDateFrom": "2021-02-18T16:00:00Z",
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
                "instructionId": "20210115SVEASES1B2215",
                "amount": {
                    "amount": 4563456345.43,
                    "currency": "SEK"
                },
                "fileName": "P27ISTXSVEASES1201911320191113135321990NCTSEK_PACS0082216",
                "batchId": "P27ISTXBANKSESS",
                "originator": "SVEASES1",
                "valueDate": "2021-01-14",
                "receiverParticipantIdentifier": "SWEDSES1",
                "settlementDate": "2021-01-14",
                "settlementCycleId": "20201209002",
                "createdDateTime": "2021-01-14T15:02:14Z",
                "status": "accepted",
                "reasonCode": null,
                "messageType": "camt.056",
                "senderParticipantIdentifier": "SVEASES1",
                "messageDirection": "sending"
        }"""
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
                0, 1, null, ZonedDateTime.of(2021, Month.FEBRUARY.value, 18, 16, 0, 0, 0, ZoneId.of("UTC")),
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
                post(urlEqualTo("/enquiry/transactions/P27-SEK/read"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_TRANSACTION_DETAILS_RESPONSE))
                        .withRequestBody(equalToJson(VALID_TRANSACTION_REQUEST)))

        val result = transactionRepository.findById("20210115SVEASES1B2215")
        assertThat(result.instructionId).isEqualTo("20210115SVEASES1B2215")
        assertThat(result.amount.amount).isEqualTo(BigDecimal.valueOf(4563456345.43))
        assertThat(result.amount.currency).isEqualTo("SEK")
        assertThat(result.batchId).isEqualTo("P27ISTXBANKSESS")
        assertThat(result.valueDate).isEqualTo(LocalDate.of(2021, 1, 14))
        assertThat(result.receiverParticipantIdentifier).isEqualTo("SWEDSES1")
        assertThat(result.settlementDate).isEqualTo(LocalDate.of(2021, 1, 14))
        assertThat(result.settlementCycleId).isEqualTo("20201209002")
        assertThat(result.createdAt).isEqualTo(ZonedDateTime.of(2021, Month.JANUARY.value, 14, 15, 2, 14, 0, ZoneId.of("UTC")))
        assertThat(result.status).isEqualTo("accepted")
        assertThat(result.reasonCode).isEqualTo(null)
        assertThat(result.messageType).isEqualTo("camt.056")
        assertThat(result.senderParticipantIdentifier).isEqualTo("SVEASES1")
    }
}