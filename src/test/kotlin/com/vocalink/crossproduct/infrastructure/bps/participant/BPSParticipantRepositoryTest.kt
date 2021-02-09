package com.vocalink.crossproduct.infrastructure.bps.participant

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.domain.participant.ManagedParticipantsSearchCriteria
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipantRepository
import java.math.BigDecimal
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
            "participantConnectionId": "NA"
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
            "participantConnectionId": "NDEASESSXXX"
        }] 
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
            "participantConnectionId": "NA"
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
                    "organizationId": "894819924"
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
                    "organizationId": "77777777",
                    "tpspName": "Forex Bank",
                    "tpspId": "940404004"
                }
            ]
        }
        """

        const val VALID_PARTICIPANT_CONFIGURATION_RESPONSE: String = """ 
        {
            "schemeParticipantIdentifier": "NDEASESSXXX",
            "txnVolume": 100,
            "outputFileTimeLimit": 40,
            "networkName": "SWIFT",
            "gatewayName": "Swift Alliance G004",
            "requestorDN": "VL BPS",
            "responderDN": "SWCTSES1",
            "preSettlementAckType": "pacs.004.001.03",
            "preSettlementActGenerationLevel": "BATCH",
            "postSettlementAckType": "pacs.004.001.03",
            "postSettlementAckGenerationLevel": "BATCH",
            "debitCapLimit": 59099,
            "debitCapLimitThresholds": [
                0.1
            ]
        } 
        """
    }

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `should return 200 and all participants with success`() {
        mockServer.stubFor(
                post(urlEqualTo("/participants/P27-SEK/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_PARTICIPANTS_RESPONSE))
                        .withRequestBody(equalToJson(VALID_ALL_PARTICIPANTS_REQUEST)))

        val result = participantRepository.findAll()

        assertThat(result.size).isEqualTo(2)
        assertThat(result[0].bic).isEqualTo("NDEASESSXXX")
        assertThat(result[0].id).isEqualTo("NDEASESSXXX")
        assertThat(result[0].name).isEqualTo("Nordea")
        assertThat(result[0].status).isEqualTo(ParticipantStatus.ACTIVE)
        assertThat(result[0].suspendedTime).isNull()

        assertThat(result[1].bic).isEqualTo("HANDSESS")
        assertThat(result[1].id).isEqualTo("HANDSESS")
        assertThat(result[1].name).isEqualTo("Svenska Handelsbanken")
        assertThat(result[1].status).isEqualTo(ParticipantStatus.SUSPENDED)
        assertThat(result[1].suspendedTime).isNotNull()
    }

    @Test
    fun `should return 200 and all participants with given connectionParty and participantType with success`() {
        mockServer.stubFor(
                post(urlEqualTo("/participants/P27-SEK/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_PARTICIPANTS_RESPONSE))
                        .withRequestBody(equalToJson(VALID_PARTICIPANTS_REQUEST)))

        val result = participantRepository.findByConnectingPartyAndType("NDEASESSXXX", "FUNDING")

        assertThat(result.size).isEqualTo(2)
        assertThat(result[0].bic).isEqualTo("NDEASESSXXX")
        assertThat(result[0].id).isEqualTo("NDEASESSXXX")
        assertThat(result[0].name).isEqualTo("Nordea")
        assertThat(result[0].status).isEqualTo(ParticipantStatus.ACTIVE)
        assertThat(result[0].suspendedTime).isNull()

        assertThat(result[1].bic).isEqualTo("HANDSESS")
        assertThat(result[1].id).isEqualTo("HANDSESS")
        assertThat(result[1].name).isEqualTo("Svenska Handelsbanken")
        assertThat(result[1].status).isEqualTo(ParticipantStatus.SUSPENDED)
        assertThat(result[1].suspendedTime).isNotNull()
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
                post(urlEqualTo("/participants/P27-SEK/configuration"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_PARTICIPANT_CONFIGURATION_RESPONSE))
                        .withRequestBody(equalToJson(VALID_PARTICIPANTS_WITH_ID_REQUEST)))

        val result = participantRepository.findConfigurationById(participantId)
        assertThat(result.schemeParticipantIdentifier).isEqualTo("NDEASESSXXX")
        assertThat(result.txnVolume).isEqualTo(100)
        assertThat(result.outputFileTimeLimit).isEqualTo(40)
        assertThat(result.networkName).isEqualTo("SWIFT")
        assertThat(result.gatewayName).isEqualTo("Swift Alliance G004")
        assertThat(result.requestorDN).isEqualTo("VL BPS")
        assertThat(result.responderDN).isEqualTo("SWCTSES1")
        assertThat(result.preSettlementAckType).isEqualTo("pacs.004.001.03")
        assertThat(result.preSettlementActGenerationLevel).isEqualTo("BATCH")
        assertThat(result.postSettlementAckType).isEqualTo("pacs.004.001.03")
        assertThat(result.postSettlementAckGenerationLevel).isEqualTo("BATCH")
        assertThat(result.debitCapLimit).isEqualTo(BigDecimal.valueOf(59099))
        assertThat(result.debitCapLimitThresholds).isEqualTo(listOf(0.1))
    }
}
