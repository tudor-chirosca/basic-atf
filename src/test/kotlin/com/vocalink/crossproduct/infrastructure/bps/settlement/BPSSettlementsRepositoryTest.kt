package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.vocalink.crossproduct.domain.settlement.InstructionStatus
import com.vocalink.crossproduct.domain.settlement.SettlementDetailsSearchCriteria
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
import java.time.ZoneId
import java.time.ZonedDateTime

@BPSTestConfiguration
@Import(BPSSettlementsRepository::class)
class BPSSettlementsRepositoryTest @Autowired constructor(var repository: BPSSettlementsRepository, var mockServer: WireMockServer) {

    companion object {
        const val VALID_SETTLEMENT_DETAILS_REQUEST: String = """
        {
            "cycleId": "20210322001",
            "participantId": "SWEDSES1",
            "sortingOrder": []               
        }
        """

        const val VALID_SETTLEMENT_DETAILS_RESPONSE: String = """
        {
              "data": [
            {
                "schemeParticipantIdentifier": "SWEDSES1",
                "settlementBank": "NA",
                "cycleId": "20210322001",
                "settlementCycleDate": "2021-03-22T14:00:00Z",
                "status": "partial",
                "settlementInstructionReference": 2342847,
                "statusDetail": "Rejected",
                "counterParty": "SWEDSESS",
                "counterPartySettlement": null,
                "totalAmountDebited": {
                    "amount": 10,
                    "currency": "SEK"
                },
                "totalAmountCredited": {
                    "amount": 10,
                    "currency": "SEK"
                }
            }
        ],
             "summary": {
                "offset": 0,
                "pageSize": 20,
                "totalCount": 1
            }
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
        {
            "updatedAt": "2021-01-11T12:00:00Z",
            "scheduleDayDetails": [
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
            post(urlPathEqualTo("/enquiries/settlement/enquiry/P27-SEK/read"))
                .withRequestBody(equalToJson(VALID_SETTLEMENT_DETAILS_REQUEST))
                .withQueryParam("offset", equalTo("0"))
                .withQueryParam("pageSize", equalTo("20"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(VALID_SETTLEMENT_DETAILS_RESPONSE)))

        val criteria = SettlementDetailsSearchCriteria.builder()
            .offset(0)
            .limit(20)
            .cycleId("20210322001")
            .participantId("SWEDSES1")
            .build()

        val result = repository.findDetails(criteria)

        assertThat(result).isNotNull
        assertThat(result.items[0].cycleId).isEqualTo("20210322001")
        assertThat(result.items[0].status).isEqualTo(SettlementStatus.PARTIAL)
        assertThat(result.items[0].schemeParticipantIdentifier).isEqualTo("SWEDSES1")
        assertThat(result.items[0].settlementInstructionReference).isEqualTo(2342847)
        assertThat(result.items[0].statusDetail).isEqualTo(InstructionStatus.REJECTED)
        assertThat(result.items[0].counterParty).isEqualTo("SWEDSESS")
        assertThat(result.items[0].counterPartySettlement).isNull()
        assertThat(result.items[0].totalAmountCredited.amount).isEqualTo(BigDecimal.TEN)
        assertThat(result.items[0].totalAmountCredited.currency).isEqualTo("SEK")
        assertThat(result.items[0].totalAmountDebited.amount).isEqualTo(BigDecimal.TEN)
        assertThat(result.items[0].totalAmountDebited.currency).isEqualTo("SEK")
        assertThat(result.items[0].settlementCycleDate).isEqualTo("2021-03-22T14:00:00Z")
        assertThat(result.items[0].settlementBank).isEqualTo("NA")
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

        val result = repository.findSchedule()
        assertThat(result.updatedAt).isEqualTo(ZonedDateTime.of(2021,1,11,12,0,0, 0, ZoneId.of("UTC")))
        assertThat(result.scheduleDayDetails[0].weekDay).isEqualTo("Monday")
        assertThat(result.scheduleDayDetails[0].cycles[0].cycleName).isEqualTo("01")
        assertThat(result.scheduleDayDetails[0].cycles[0].startTime).isEqualTo("09:00")
        assertThat(result.scheduleDayDetails[0].cycles[0].cutOffTime).isEqualTo("10:00")
        assertThat(result.scheduleDayDetails[0].cycles[0].settlementStartTime).isEqualTo("9:45")
    }
}
