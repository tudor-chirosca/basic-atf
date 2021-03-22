package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.vocalink.crossproduct.domain.settlement.InstructionEnquirySearchCriteria
import com.vocalink.crossproduct.domain.settlement.SettlementEnquirySearchCriteria
import com.vocalink.crossproduct.domain.settlement.SettlementStatus
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import java.math.BigDecimal
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@BPSTestConfiguration
@Import(BPSSettlementsRepository::class)
class BPSSettlementsRepositoryTest @Autowired constructor(var repository: BPSSettlementsRepository, var mockServer: WireMockServer) {

    companion object {
        const val VALID_INSTRUCTION_REQUEST: String = """
        {
            "offset": 0,
            "limit": 10,
            "sort": null,
            "cycleId": "20201209001",
            "participantId": "HANDSESS"
        }
        """
        const val VALID_SETTLEMENT_REQUEST: String = """
        {
            "cycleId": "20201209001",
            "participantId": "HANDSESS"
        }
        """
        const val VALID_SETTLEMENT_RESPONSE: String = """
        {
          "cycleId": "20201209001",
          "status": "PARTIAL",
          "schemeParticipantIdentifier": "NDEASESSXXX"
        }
        """
        const val VALID_INSTRUCTION_PAGE_RESPONSE: String = """
        {
           "totalResults": 17,
            "items": [
            {
                "cycleId": "20201209001",
                "participantId": "NDEASESSXXY",
                "reference": "2342631",
                "status": "no-response",
                "counterpartyId": "SWEDSESS",
                "settlementCounterpartyId": null,
                "totalDebit": 10,
                "totalCredit": 10
            }
          ]
        }
        """

        const val VALID_SETTLEMENTS_REQUEST: String = """
        {
            "dateFrom": null,
            "dateTo": null,
            "sessionInstanceId": null,
            "participant": "HANDSESS",
            "sortingOrder" : [ ]
        }
        """

        const val VALID_SETTLEMENTS_RESPONSE: String = """
        {
            "data": [
                {
                    "cycleId": "20201209001",
                    "settlementStartDate": "2020-12-09T14:58:19Z",
                    "status": "partial",
                    "schemeParticipantIdentifier": "HANDSESS"
                },
                {   "cycleId": "20201209002",
                    "settlementStartDate": "2020-12-09T14:58:19Z",
                    "status": "no-response",
                    "schemeParticipantIdentifier": "HANDSESS"
                }    
            ],
            "summary": {
                "offset": 0,
                "pageSize": 20,
                "totalCount": 2
            }
        }
       """

        const val VALID_SCHEDULES_RESPONSE: String = """
        [
          {
            "weekDay": "Monday",
            "cycles": [
              {
                "sessionCode": "01",
                "startTime": "09:00",
                "endTime": "10:00",
                "settlementTime": "9:45"
              }
            ]
          }
        ]
       """
    }

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `should return settlement for given cycleId and participantId`() {
        mockServer.stubFor(
                post(urlEqualTo("/enquiry/settlements/read"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_SETTLEMENT_RESPONSE))
                        .withRequestBody(equalToJson(VALID_SETTLEMENT_REQUEST)))

        mockServer.stubFor(
                post(urlEqualTo("/enquiry/instructions"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_INSTRUCTION_PAGE_RESPONSE))
                        .withRequestBody(equalToJson(VALID_INSTRUCTION_REQUEST)))

        val criteria = InstructionEnquirySearchCriteria(0, 10, null, "20201209001", "HANDSESS")
        val result = repository.findBy(criteria)

        assertThat(result).isNotNull
        assertThat(result.cycleId).isEqualTo("20201209001")
        assertThat(result.status).isEqualTo(SettlementStatus.PARTIAL)
        assertThat(result.schemeParticipantIdentifier).isEqualTo("NDEASESSXXX")
        assertThat(result.instructions.items[0].cycleId).isEqualTo("20201209001")
        assertThat(result.instructions.items[0].participantId).isEqualTo("NDEASESSXXY")
        assertThat(result.instructions.items[0].reference).isEqualTo("2342631")
        assertThat(result.instructions.items[0].status).isEqualTo(SettlementStatus.NO_RESPONSE.name)
        assertThat(result.instructions.items[0].counterpartyId).isEqualTo("SWEDSESS")
        assertThat(result.instructions.items[0].settlementCounterpartyId).isEqualTo(null)
        assertThat(result.instructions.items[0].totalCredit).isEqualTo(BigDecimal.TEN)
        assertThat(result.instructions.items[0].totalCredit).isEqualTo(BigDecimal.TEN)
    }

    @Test
    fun `should return settlements for given parameters`() {
        mockServer.stubFor(
                post(urlPathEqualTo("/enquiries/settlement/enquiry/P27-SEK/readAll"))
                        .withRequestBody(equalToJson(VALID_SETTLEMENTS_REQUEST))
                        .withQueryParam("offset", equalTo("0"))
                        .withQueryParam("pageSize", equalTo("20"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_SETTLEMENTS_RESPONSE)))

        val criteria = SettlementEnquirySearchCriteria.builder()
                .offset(0)
                .limit(20)
                .participants(listOf("HANDSESS"))
                .build()

        val result = repository.findPaginated(criteria)

        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(2)
        assertThat(result.items.size).isEqualTo(2)
    }

    @Test
    fun `should return schedules`() {
        mockServer.stubFor(
                post(urlEqualTo("/enquiries/settlement/schedule/P27-SEK/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_SCHEDULES_RESPONSE)))

        val result = repository.findSchedules()
        assertThat(result[0].weekDay).isEqualTo("Monday")
        assertThat(result[0].cycles[0].cycleName).isEqualTo("01")
        assertThat(result[0].cycles[0].startTime).isEqualTo("09:00")
        assertThat(result[0].cycles[0].cutOffTime).isEqualTo("10:00")
        assertThat(result[0].cycles[0].settlementStartTime).isEqualTo("9:45")
    }
}
