package com.vocalink.crossproduct.infrastructure.bps.participant

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.vocalink.crossproduct.domain.participant.ManagedParticipantsSearchCriteria
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.participant.SuspensionLevel
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import java.math.BigDecimal
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@BPSTestConfiguration
@Import(BPSParticipantRepository::class)
class BPSParticipantRepositoryTest @Autowired constructor(var participantRepository: BPSParticipantRepository,
                                                          var mockServer: WireMockServer) {

    companion object {
        const val VALID_ALL_PARTICIPANTS_REQUEST: String = """ { } """
        const val VALID_PARTICIPANTS_REQUEST: String = """
        {
            "connectingParty": "NDEASESSXXX",
            "participantType": "FUNDING"
        }
        """

        const val VALID_PARTICIPANTS_WITH_ID_REQUEST: String = """
        {
            "schemeParticipantIdentifier" : "NDEASESSXXX"
        }
        """

        const val VALID_MANAGED_PARTICIPANTS_REQUEST: String = """
        {
            "offset": 0,
            "limit": 2
        }
        """

        const val VALID_PARTICIPANTS_RESPONSE: String = """
        {
            "data":
            [{
            "schemeCode": "P27-SEK",
            "schemeParticipantIdentifier": "NDEASESSXXX",
            "countryCode": "SWE",
            "partyCode": "NDEASESSXXX",
            "participantType": "FUNDED",
            "connectingParty": "NA",
            "status": "ACTIVE",
            "effectiveFromDate": "2020-05-22T14:09:05Z",
            "effectiveTillDate": null,
            "participantName": "Nordea",
            "rcvngParticipantConnectionId": "NA",
            "participantConnectionId": "NA",
            "partyExternalIdentifier": "99999999"
        },
        {
            "schemeCode": "P27-SEK",
            "schemeParticipantIdentifier": "HANDSESS",
            "countryCode": "SWE",
            "partyCode": "HANDSESS",
            "participantType": "FUNDED",
            "connectingParty": "AABASESSXXX",
            "status": "SUSPENDED",
            "effectiveFromDate": "2020-09-22T14:09:05Z",
            "effectiveTillDate": "2019-12-22T14:09:05Z",
            "participantName": "Svenska Handelsbanken",
            "rcvngParticipantConnectionId": "NDEASESSXXX",
            "participantConnectionId": "NDEASESSXXX",
            "suspensionLevel": "SELF",
            "partyExternalIdentifier": "77777777"
        }],
        "summary": {
            "offset": 0,
            "pageSize": 999,
            "totalCount": 23
        }
    } 
        """

        const val VALID_PARTICIPANTS_BY_ID_RESPONSE: String = """
        {
            "schemeCode": "P27-SEK",
            "schemeParticipantIdentifier": "NDEASESSXXX",
            "countryCode": "SWE",
            "partyCode": "NDEASESSXXX",
            "participantType": "DIRECT",
            "connectingParty": "NA",
            "status": "ACTIVE",
            "effectiveFromDate": "2020-05-22T14:09:05Z",
            "effectiveTillDate": null,
            "participantName": "Nordea",
            "rcvngParticipantConnectionId": "NA",
            "participantConnectionId": "NA",
            "partyExternalIdentifier": "77777777"
        }
        """

        const val VALID_PARTICIPANTS_WO_CONN_EFFECTIVE_TILL: String = """
        {
            "schemeCode": "P27-SEK",
            "schemeParticipantIdentifier": "NDEASESSXXX",
            "countryCode": "SWE",
            "partyCode": "NDEASESSXXX",
            "participantType": "DIRECT",
            "status": "ACTIVE",
            "effectiveFromDate": "2020-05-22T14:09:05Z",
            "participantName": "Nordea",
            "rcvngParticipantConnectionId": "NA",
            "participantConnectionId": "NA"
        }
        """

        const val VALID_MANAGED_PARTICIPANTS_RESPONSE: String = """
        {
            "totalResults": 23,
            "items": [
                {
                    "schemeCode": "P27-SEK",
                    "schemeParticipantIdentifier": "AVANSESX",
                    "countryCode": "SWE",
                    "partyCode": "AVANSESX",
                    "connectingParty": "NA",
                    "participantType": "DIRECT",
                    "status": "ACTIVE",
                    "effectiveFromDate": "2020-10-21T18:09:05Z",
                    "effectiveTillDate": null,
                    "participantName": "Avanza Bank",
                    "rcvngParticipantConnectionId": "NA",
                    "participantConnectionId": "NA",
                    "partyExternalIdentifier": "894819924"
                },
                {
                    "schemeCode": "P27-SEK",
                    "schemeParticipantIdentifier": "NDEASESSXXX",
                    "countryCode": "SWE",
                    "partyCode": "NDEASESSXXX",
                    "connectingParty": "NA",
                    "participantType": "DIRECT+FUNDING",
                    "status": "ACTIVE",
                    "effectiveFromDate": "2020-05-22T14:09:05Z",
                    "effectiveTillDate": null,
                    "participantName": "Nordea",
                    "rcvngParticipantConnectionId": "NA",
                    "participantConnectionId": "NA",
                    "partyExternalIdentifier": "77777777",
                    "tpspName": "Forex Bank",
                    "tpspId": "940404004"
                }
            ]
        }
        """

        const val VALID_PARTICIPANT_CONFIGURATION_RESPONSE: String = """
        {
            "schemeParticipantIdentifier": "NDEASESSXXX",
            "participantName": "Nordea",
            "participantBic": "NDEASESSXXX",
            "partyExternalIdentifier": "77777777",
            "participantType": "DIRECT",
            "connectingParty": "NA",
            "participantConnectionId": "NA",
            "suspensionLevel": "SCHEME",
            "settlementAccount": "6789123456789",
            "tpspId": "589328",
            "tpspName": "Danske Bank",
            "networkName": "SWIFT",
            "outputChannel": "XXXXXX",
            "status": "ACTIVE",
            "debitCapThreshold": [
                {
                    "debitCapLimitId": "absd1240",
                    "limitThresholdAmounts": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "warningThresholdPercentage": 0.55
                }
            ],
            "outputFlow": [
                {
                    "messageType": "pacs.008.001.02",
                    "txnVolume": 100000
                },
                {
                    "messageType": "pacs.004.001.02",
                    "txnVolume": 15000
                },
                {
                    "messageType": "camt.056.001.01",
                    "txnVolume": 10000,
                    "txnTimeLimit": 30
                },
                {
                    "messageType": "camt.029.001.03",
                    "txnVolume": 20,
                    "txnTimeLimit": 15
                },
                {
                    "messageType": "camt.029.001.08",
                    "txnVolume": 1,
                    "txnTimeLimit": 20
                },
                {
                    "messageType": "camt.027.001.06",
                    "txnTimeLimit": 10
                },
                {
                    "messageType": "camt.087.001.05",
                    "txnVolume": 1,
                    "txnTimeLimit": 60
                }
            ],
            "updatedAt": "2021-07-26T16:00:00Z",
            "updatedBy": "12a511"
        }
        """
    }

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `should return 200 and all participants with success`() {
        val maxInteger = Integer.MAX_VALUE
        mockServer.stubFor(
                post(urlPathEqualTo("/participants/P27-SEK/readAll"))
                        .withRequestBody(equalToJson(VALID_ALL_PARTICIPANTS_REQUEST))
                        .withQueryParam("offset", equalTo("0"))
                        .withQueryParam("pageSize", equalTo("$maxInteger"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_PARTICIPANTS_RESPONSE))
                        )

        val result = participantRepository.findAll()

        assertThat(result.items.size).isEqualTo(2)
        val items = result.items
        assertThat(items[0].bic).isEqualTo("NDEASESSXXX")
        assertThat(items[0].id).isEqualTo("NDEASESSXXX")
        assertThat(items[0].name).isEqualTo("Nordea")
        assertThat(items[0].status).isEqualTo(ParticipantStatus.ACTIVE)
        assertThat(items[0].suspendedTime).isNull()

        assertThat(items[1].bic).isEqualTo("HANDSESS")
        assertThat(items[1].id).isEqualTo("HANDSESS")
        assertThat(items[1].name).isEqualTo("Svenska Handelsbanken")
        assertThat(items[1].suspensionLevel).isEqualTo(SuspensionLevel.SELF)
        assertThat(items[1].status).isEqualTo(ParticipantStatus.SUSPENDED)
        assertThat(items[1].suspendedTime).isNotNull()
    }

    @Test
    fun `should return 200 and all participants with given connectionParty and participantType with success`() {
        val maxInteger = Integer.MAX_VALUE
        mockServer.stubFor(
                post(urlPathEqualTo("/participants/P27-SEK/readAll"))
                        .withRequestBody(equalToJson(VALID_PARTICIPANTS_REQUEST))
                        .withQueryParam("offset", equalTo("0"))
                        .withQueryParam("pageSize", equalTo("$maxInteger"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_PARTICIPANTS_RESPONSE))
                       )

        val result = participantRepository.findByConnectingPartyAndType("NDEASESSXXX", "FUNDING")
        val items = result.items

        assertThat(items.size).isEqualTo(2)
        assertThat(items[0].bic).isEqualTo("NDEASESSXXX")
        assertThat(items[0].id).isEqualTo("NDEASESSXXX")
        assertThat(items[0].name).isEqualTo("Nordea")
        assertThat(items[0].status).isEqualTo(ParticipantStatus.ACTIVE)
        assertThat(items[0].suspendedTime).isNull()

        assertThat(items[1].bic).isEqualTo("HANDSESS")
        assertThat(items[1].id).isEqualTo("HANDSESS")
        assertThat(items[1].name).isEqualTo("Svenska Handelsbanken")
        assertThat(items[1].status).isEqualTo(ParticipantStatus.SUSPENDED)
        assertThat(items[1].suspendedTime).isNotNull()
    }

    @Test
    fun `should return 200 for body with participant identifier with success`() {
        val participantId = "NDEASESSXXX"
        mockServer.stubFor(
                post(urlEqualTo("/participants/P27-SEK/read"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_PARTICIPANTS_BY_ID_RESPONSE))
                        .withRequestBody(equalToJson(VALID_PARTICIPANTS_WITH_ID_REQUEST)))

        val result = participantRepository.findById(participantId)

        assertThat(result.bic).isEqualTo("NDEASESSXXX")
        assertThat(result.id).isEqualTo("NDEASESSXXX")
        assertThat(result.name).isEqualTo("Nordea")
        assertThat(result.status).isEqualTo(ParticipantStatus.ACTIVE)
        assertThat(result.participantType).isEqualTo(ParticipantType.DIRECT)
        assertThat(result.suspendedTime).isNull()
    }

    @Test
    fun `should return 200, participant w-o connecting_party and effective_till`() {
        val participantId = "NDEASESSXXX"
        mockServer.stubFor(
                post(urlEqualTo("/participants/P27-SEK/read"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_PARTICIPANTS_WO_CONN_EFFECTIVE_TILL))
                        .withRequestBody(equalToJson(VALID_PARTICIPANTS_WITH_ID_REQUEST)))

        val result = participantRepository.findById(participantId)

        assertThat(result.bic).isEqualTo("NDEASESSXXX")
        assertThat(result.id).isEqualTo("NDEASESSXXX")
        assertThat(result.name).isEqualTo("Nordea")
        assertThat(result.status).isEqualTo(ParticipantStatus.ACTIVE)
        assertThat(result.participantType).isEqualTo(ParticipantType.DIRECT)
        assertThat(result.suspendedTime).isNull()
    }

    @Test
    fun `should return the list of managed participants`() {
        mockServer.stubFor(
                post(urlEqualTo("/participants/managed"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_MANAGED_PARTICIPANTS_RESPONSE))
                        .withRequestBody(equalToJson(VALID_MANAGED_PARTICIPANTS_REQUEST)))

        val result = participantRepository.findPaginated(ManagedParticipantsSearchCriteria(0, 2, null, null))

        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(23)
        assertThat(result.items).isNotEmpty

        assertThat(result.items[0].bic).isEqualTo("AVANSESX")
        assertThat(result.items[0].fundingBic).isEqualTo("NA")
        assertThat(result.items[0].participantType).isEqualTo(ParticipantType.DIRECT)
    }

    @Test
    fun `should return 200, on participant configuration`() {
        val participantId = "NDEASESSXXX"
        mockServer.stubFor(
                post(urlEqualTo("/participants/P27-SEK/aggregate/configuration"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_PARTICIPANT_CONFIGURATION_RESPONSE))
                        .withRequestBody(equalToJson(VALID_PARTICIPANTS_WITH_ID_REQUEST)))

        val result = participantRepository.findConfigurationById(participantId)
        assertThat(result.schemeParticipantIdentifier).isEqualTo("NDEASESSXXX")
        assertThat(result.participantName).isEqualTo("Nordea")
        assertThat(result.participantBic).isEqualTo("NDEASESSXXX")
        assertThat(result.partyExternalIdentifier).isEqualTo("77777777")
        assertThat(result.participantType).isEqualTo(ParticipantType.DIRECT)
        assertThat(result.suspensionLevel).isEqualTo("SCHEME")
        assertThat(result.connectingParty).isEqualTo("NA")
        assertThat(result.participantConnectionId).isEqualTo("NA")
        assertThat(result.settlementAccount).isEqualTo("6789123456789")
        assertThat(result.tpspId).isEqualTo("589328")
        assertThat(result.tpspName).isEqualTo("Danske Bank")
        assertThat(result.networkName).isEqualTo("SWIFT")
        assertThat(result.outputChannel).isEqualTo("XXXXXX")
        assertThat(result.status).isEqualTo("ACTIVE")
        assertThat(result.updatedAt).isEqualTo(ZonedDateTime.parse("2021-07-26T16:00:00Z"))
        assertThat(result.updatedBy).isEqualTo("12a511")
        assertThat(result.debitCapLimit).isEqualTo(2145.41.toBigDecimal())
        assertThat(result.debitCapLimitThresholds).isEqualTo(listOf(0.55))
        assertThat(result.outputFlow[0].messageType).isEqualTo("pacs.008.001.02")
        assertThat(result.outputFlow[2].txnTimeLimit).isEqualTo(30)
        assertThat(result.outputFlow[0].txnVolume).isEqualTo(100000)
    }
}
