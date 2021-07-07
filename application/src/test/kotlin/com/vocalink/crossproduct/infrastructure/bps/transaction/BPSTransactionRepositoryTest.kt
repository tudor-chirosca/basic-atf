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
import java.time.LocalTime
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
            "createdDateTo": "2021-03-18T00:00:00Z",
            "sendingParticipant": "BARCGB22XXX",
            "transactionRangeTo": {
                "amount": 10,
                "currency": "SEK"
            }
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
                    "debtor": "SWEDENBB",
                    "creditor": "ATFTEST2XXX",
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
                "settlementDate": "2021-02-18T18:00:00Z",
                "fileName": "E27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800101.gz",
                "batchId": "F27ISTXBANKSESS",
                "transactionAmount": {
                    "amount": 777777.77,
                    "currency": "SEK"
                },
                "senderBank": "Ikano Bank",
                "senderBic": "IKANSE21",
                "senderIBAN": "SE23 9999 9999 9999 9999 2170",
                "senderFullName": "Mark Markson",
                "receiverBank": "Nordea",
                "receiverBic": "NDEASESSXXX",
                "receiverIBAN": "SE23 9999 9999 9999 9999 2142",
                "receiverFullName": "John Smith",
                "debtorName": "Thomas Mark",
                "debtorBic": "ATFTEST2XXX",
                "creditorName": "Bill Bilson",
                "creditorBic": "BSUISESSXXX"
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
                0, 1, null,
                ZonedDateTime.of(LocalDate.of(2021, 2, 18), LocalTime.MIN, ZoneId.of("UTC")),
                ZonedDateTime.of(LocalDate.of(2021, 3, 18), LocalTime.MIN, ZoneId.of("UTC")),
                null, null, "BARCGB22XXX",
                null, null, null, null, null,
                null, null, null, null, null, BigDecimal.TEN
        )
        val result = transactionRepository.findPaginated(request)
        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(8)
        assertThat(result.items).isNotEmpty

        val item = result.items[0]
        assertThat(item.instructionId).isEqualTo("20210115MEEOSES1B2187")
        assertThat(item.createdAt).isEqualTo(ZonedDateTime.of(2021, Month.FEBRUARY.value, 18, 16, 0, 0, 0, ZoneId.of("UTC")))
        assertThat(item.debtorBic).isEqualTo("SWEDENBB")
        assertThat(item.creditorBic).isEqualTo("ATFTEST2XXX")
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

        val date = ZonedDateTime.of(2021, Month.FEBRUARY.value, 18, 18, 0, 0, 0, ZoneId.of("UTC"))
        val result = transactionRepository.findById("20210115SVEASES1B2215")
        assertThat(result.instructionId).isEqualTo("20210115IKANSE21B2167")
        assertThat(result.messageType).isEqualTo("pacs.002")
        assertThat(result.createdAt).isEqualTo(date)
        assertThat(result.status).isEqualTo("ACSC")
        assertThat(result.reasonCode).isEqualTo(null)
        assertThat(result.settlementCycleId).isEqualTo("20210218002")
        assertThat(result.settlementDate).isEqualTo(date)
        assertThat(result.fileName).isEqualTo("E27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800101.gz")
        assertThat(result.batchId).isEqualTo("F27ISTXBANKSESS")
        assertThat(result.amount.amount).isEqualTo(BigDecimal.valueOf(777777.77))
        assertThat(result.amount.currency).isEqualTo("SEK")

        assertThat(result.senderBank).isEqualTo("Ikano Bank")
        assertThat(result.senderBic).isEqualTo("IKANSE21")
        assertThat(result.senderIBAN).isEqualTo("SE23 9999 9999 9999 9999 2170")
        assertThat(result.senderFullName).isEqualTo("Mark Markson")

        assertThat(result.receiverBank).isEqualTo("Nordea")
        assertThat(result.receiverBic).isEqualTo("NDEASESSXXX")
        assertThat(result.receiverIBAN).isEqualTo("SE23 9999 9999 9999 9999 2142")
        assertThat(result.receiverFullName).isEqualTo("John Smith")

        assertThat(result.debtorName).isEqualTo("Thomas Mark")
        assertThat(result.debtorBic).isEqualTo("ATFTEST2XXX")
        assertThat(result.creditorBic).isEqualTo("BSUISESSXXX")
        assertThat(result.creditorName).isEqualTo("Bill Bilson")
    }

    @Test
    fun `should return the empty list on 204 NO_CONTENT`() {
        mockServer.stubFor(
            post(urlPathEqualTo("/enquiries/transactions/P27-SEK/readAll"))
                .withRequestBody(equalToJson(VALID_TRANSACTIONS_REQUEST))
                .withQueryParam("offset", equalTo("0"))
                .withQueryParam("pageSize", equalTo("1"))
                .willReturn(aResponse()
                    .withStatus(204)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)))

        val request = TransactionEnquirySearchCriteria(
            0, 1, null,
            ZonedDateTime.of(LocalDate.of(2021, 2, 18), LocalTime.MIN, ZoneId.of("UTC")),
            ZonedDateTime.of(LocalDate.of(2021, 3, 18), LocalTime.MIN, ZoneId.of("UTC")),
            null, null, "BARCGB22XXX",
            null, null, null, null, null,
            null, null, null, null, null, BigDecimal.TEN
        )

        val result = transactionRepository.findPaginated(request)
        assertThat(result).isNotNull
        assertThat(result.items).isEmpty()
        assertThat(result.totalResults).isEqualTo(0)
    }

    @Test
    fun `should return the empty list on 404 NOT_FOUND`() {
        mockServer.stubFor(
            post(urlPathEqualTo("/enquiries/transactions/P27-SEK/readAll"))
                .withRequestBody(equalToJson(VALID_TRANSACTIONS_REQUEST))
                .withQueryParam("offset", equalTo("0"))
                .withQueryParam("pageSize", equalTo("1"))
                .willReturn(aResponse()
                    .withStatus(404)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)))

        val request = TransactionEnquirySearchCriteria(
            0, 1, null,
            ZonedDateTime.of(LocalDate.of(2021, 2, 18), LocalTime.MIN, ZoneId.of("UTC")),
            ZonedDateTime.of(LocalDate.of(2021, 3, 18), LocalTime.MIN, ZoneId.of("UTC")),
            null, null, "BARCGB22XXX",
            null, null, null, null, null,
            null, null, null, null, null, BigDecimal.TEN
        )

        val result = transactionRepository.findPaginated(request)
        assertThat(result).isNotNull
        assertThat(result.items).isEmpty()
        assertThat(result.totalResults).isEqualTo(0)
    }
}