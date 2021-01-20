package com.vocalink.crossproduct.infrastructure.bps.repository

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipantRepository
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
            "participantType": "DIRECT+ONLY",
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
            "participantType": "DIRECT+ONLY",
            "status": "ACTIVE",
            "effectiveFromDate": "2020-05-22T14:09:05Z",
            "participantName": "Nordea",
            "rcvngParticipantConnectionId": "NA",
            "participantConnectionId": "NA"
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
        assertThat(result.participantType).isEqualTo(ParticipantType.DIRECT_ONLY)
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
        assertThat(result.participantType).isEqualTo(ParticipantType.DIRECT_ONLY)
        assertThat(result.suspendedTime).isNull()
    }
}
