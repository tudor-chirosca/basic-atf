package com.vocalink.crossproduct.infrastructure.bps.repository

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransactionRepository
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
            "offset": 0,
            "limit": 20,
            "sort": null,
            "dateFrom": "2021-01-03",
            "dateTo": null,
            "cycleIds": null,
            "messageDirection": "sending",
            "messageType": null,
            "sendingBic": null,
            "receivingBic": null,
            "status": null,
            "reasonCode": null,
            "id": null,
            "sendingAccount": null,
            "receivingAccount": null,
            "valueDate": null,
            "txnFrom": null,
            "txnTo": null
        }"""

        const val VALID_TRANSACTION_REQUEST: String = """ 
        {
            "txnsInstructionId": "20210115SVEASES1B2215"
        }"""

        const val VALID_TRANSACTION_RESULT_LIST_RESPONSE: String = """
            {
             "totalResults": 1,
             "items": [
               {
                "instructionId": "20210115SVEASES1B2215",
                "amount": {
                    "amount": 4563456345.43,
                    "currency": "SEK"
                },
                "fileName": "P27ISTXSVEASES1201911320191113135321990NCTSEK_PACS0082216",
                "batchId": "P27ISTXBANKSESS",
                "valueDate": "2021-01-14",
                "receiverParticipantIdentifier": "SWEDSES1",
                "settlementDate": "2021-01-14",
                "settlementCycleId": "20201209002",
                "createdAt": "2021-01-14T15:02:14Z",
                "status": "accepted",
                "reasonCode": null,
                "messageType": "camt.056",
                "senderParticipantIdentifier": "SVEASES1",
                "messageDirection": "sending"
                }
            ]
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
                "valueDate": "2021-01-14",
                "receiverParticipantIdentifier": "SWEDSES1",
                "settlementDate": "2021-01-14",
                "settlementCycleId": "20201209002",
                "createdAt": "2021-01-14T15:02:14Z",
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
                post(urlEqualTo("/enquiry/transactions/P27-SEK/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_TRANSACTION_RESULT_LIST_RESPONSE))
                        .withRequestBody(equalToJson(VALID_TRANSACTIONS_REQUEST)))

        val request = TransactionEnquirySearchCriteria(
                0, 20, null, LocalDate.of(2021,1,3),
                null, null, "sending", null, null,
                null, null, null, null, null, 
                null, null, null, null
        )

        val result = transactionRepository.findPaginated(request)
        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(1)
        assertThat(result.items).isNotEmpty

        val item = result.items[0]
        assertThat(item.instructionId).isEqualTo("20210115SVEASES1B2215")
        assertThat(item.amount.amount).isEqualTo(BigDecimal.valueOf(4563456345.43))
        assertThat(item.amount.currency).isEqualTo("SEK")
        assertThat(item.batchId).isEqualTo("P27ISTXBANKSESS")
        assertThat(item.valueDate).isEqualTo(LocalDate.of(2021, 1, 14))
        assertThat(item.receiverParticipantIdentifier).isEqualTo("SWEDSES1")
        assertThat(item.settlementDate).isEqualTo(LocalDate.of(2021, 1, 14))
        assertThat(item.settlementCycleId).isEqualTo("20201209002")
        assertThat(item.createdAt).isEqualTo(ZonedDateTime.of(2021, Month.JANUARY.value, 14, 15, 2, 14, 0, ZoneId.of("UTC")))
        assertThat(item.status).isEqualTo("accepted")
        assertThat(item.reasonCode).isEqualTo(null)
        assertThat(item.messageType).isEqualTo("camt.056")
        assertThat(item.senderParticipantIdentifier).isEqualTo("SVEASES1")
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