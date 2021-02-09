package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.vocalink.crossproduct.domain.settlement.InstructionEnquirySearchCriteria
import com.vocalink.crossproduct.domain.settlement.SettlementEnquirySearchCriteria
import com.vocalink.crossproduct.domain.settlement.SettlementStatus
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.math.BigDecimal

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
          "participantId": "NDEASESSXXX"
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
            "offset": 0,
            "limit": 10,
            "sort": null,
            "dateFrom": null,
            "dateTo": null,
            "cycleIds": null,
            "participants": ["HANDSESS", "NDEASESSXXX"]
        }
        """

        const val VALID_SETTLEMENTS_RESPONSE: String = """
            {
                "totalResults": 2,
                "items": [
                    {
                        "cycleId": "20201209001",
                        "settlementTime": "2020-12-09T14:58:19Z",
                        "status": "partial",
                        "participantId": "NDEASESSXXY"
                    },
                    {   "cycleId": "20201209002",
                        "settlementTime": "2020-12-09T14:58:19Z",
                        "status": "no-response",
                        "participantId": "HANDSESS"
                    }    
                ]
            }
    """
    }

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `should return settlement for given cycleId and participantId`() {
        mockServer.stubFor(
                WireMock.post(WireMock.urlEqualTo("/enquiry/settlements/read"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_SETTLEMENT_RESPONSE))
                        .withRequestBody(WireMock.equalToJson(VALID_SETTLEMENT_REQUEST)))

        mockServer.stubFor(
                WireMock.post(WireMock.urlEqualTo("/enquiry/instructions"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_INSTRUCTION_PAGE_RESPONSE))
                        .withRequestBody(WireMock.equalToJson(VALID_INSTRUCTION_REQUEST)))

        val criteria = InstructionEnquirySearchCriteria(0, 10, null, "20201209001", "HANDSESS")
        val result = repository.findBy(criteria)

        assertThat(result).isNotNull
        assertThat(result.cycleId).isEqualTo("20201209001")
        assertThat(result.status).isEqualTo(SettlementStatus.PARTIAL)
        assertThat(result.participantId).isEqualTo("NDEASESSXXX")
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
                WireMock.post(WireMock.urlEqualTo("/enquiry/settlements"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_SETTLEMENTS_RESPONSE))
                        .withRequestBody(WireMock.equalToJson(VALID_SETTLEMENTS_REQUEST)))

        val criteria = SettlementEnquirySearchCriteria.builder()
                .offset(0)
                .limit(10)
                .sort(null)
                .dateFrom(null)
                .dateTo(null)
                .cycleIds(null)
                .participants(listOf("HANDSESS", "NDEASESSXXX"))
                .build()

        val result = repository.findBy(criteria)

        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(2)
        assertThat(result.items.size).isEqualTo(2)
    }
}
