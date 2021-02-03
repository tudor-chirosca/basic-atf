package com.vocalink.crossproduct.infrastructure.bps.repository

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.domain.alert.AlertPriorityType
import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertRepository
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Collections.singletonList
import kotlin.test.assertEquals

@BPSTestConfiguration
@Import(BPSAlertRepository::class)
class BPSAlertRepositoryTest @Autowired constructor(var alertRepository: BPSAlertRepository,
                                                    var mockServer: WireMockServer) {

    companion object {
        const val VALID_ALERT_REFERENCE_RESPONSE: String = """
        {
           "priorities": [
             {
               "name": "high",
               "threshold": 50
             },
             {
               "name": "medium",
               "threshold": 1000
             },
             {
               "name": "low",
               "threshold": 3000
             }
           ],
           "alertTypes": [
             "timeout-during-settlement",
             "rejected-central-bank",
             "outputs-not-on-schedule",
             "end-of-day-report-delay"
           ]
        }"""

        const val VALID_ALERT_STATS_RESPONSE: String = """ 
        {
          "total": 50000,
          "items": [
            {
              "priority": "high",
              "count": 25
            },
            {
              "priority": "medium",
              "count": 1732
            },
            {
              "priority": "low",
              "count": 23421
            }
          ]
        }
        """

        const val VALID_ALERT_REQUEST: String = """ 
        {
            "offset": 0,
            "limit": 20,
            "sort": null,
            "priorities": null,
            "types": null,
            "entities": null,
            "alertId": null,
            "date_from": null,
            "date_to": null
        }"""

        const val VALID_ALERT_REQUEST_DATE: String = """ 
        {
            "offset": 0,
            "limit": 20,
            "priorities": null,
            "date_from": "2020-10-30T12:24:12Z",
            "date_to": "2020-10-30T12:24:13Z",
            "types": null,
            "entities": null,
            "alertId": null,
            "sort": ["dateRaised"]
        }"""

        const val VALID_ALERT_REQUEST_ALL_PARAMS: String = """ 
        {
            "offset": 0,
            "limit": 20,
            "priorities": ["high"],
            "date_from": "2020-10-29T12:24:12Z",
            "date_to": "2020-11-01T12:24:12Z",
            "types": ["rejected-central-bank"],
            "entities": ["NDEASESSXXX"],
            "alertId": "3141",
            "sort": ["dateRaised"]
        }"""

        const val VALID_ALERT_RESPONSE: String = """ 
        {
            "totalResults": 2,
            "items":[
                {
                    "alertId": 3141,
                    "priority": "High",
                    "dateRaised": "2020-10-30T12:24:12Z",
                    "type": "outputs-not-on-schedule",
                    "entities": [
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
                     ]
                    },
                {
                    "alertId": 3142,
                    "priority": "High",
                    "dateRaised": "2020-10-23T12:44:12Z",
                    "type": "outputs-not-on-schedule",
                    "entities": [
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
                     ]
                }
            ]
        }"""

        const val INVALID_RESPONSE: String = """ 
        {
            
        """
    }

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `should pass alert reference success`() {
        mockServer.stubFor(
                post(urlEqualTo("/reference/alerts"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_ALERT_REFERENCE_RESPONSE)))

        val result = alertRepository.findAlertsReferenceData()

        assertEquals(3, result.priorities.size)
        assertEquals("high", result.priorities[0].name)
        assertEquals(50, result.priorities[0].threshold)

        assertEquals(4, result.alertTypes.size)
        assertEquals("timeout-during-settlement", result.alertTypes[0])
    }

    @Test
    fun `should pass alert stats with success`() {
        mockServer.stubFor(
                post(urlEqualTo("/alerts/stats"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_ALERT_STATS_RESPONSE)))

        val result = alertRepository.findAlertStats()

        assertEquals(50000, result.total)
        assertEquals(3, result.items.size)
        assertEquals(AlertPriorityType.HIGH, result.items[0].priority)
        assertEquals(25, result.items[0].count)
    }

    @Test
    fun `should return the list of alerts`() {
        mockServer.stubFor(
                post(urlEqualTo("/alerts"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_ALERT_RESPONSE))
                        .withRequestBody(equalToJson(VALID_ALERT_REQUEST)))

        val result = alertRepository.findPaginated(AlertSearchCriteria(
                0, 20, null, null, null,null,null,
                null, null
        ))


        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(2)
        assertThat(result.items).isNotEmpty

        assertThat(result.items[0].dateRaised).isEqualTo("2020-10-30T12:24:12Z")
        assertThat(result.items[0].type).isEqualTo("outputs-not-on-schedule")
        assertThat(result.items[0].priority).isEqualTo(AlertPriorityType.HIGH)
        assertThat(result.items[0].entities[0].name).isEqualTo("Nordea")
    }

    @Test
    fun `should return 200 for body containing local date time, timestamp format`() {
        mockServer.stubFor(
                post(urlEqualTo("/alerts"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_ALERT_RESPONSE))
                        .withRequestBody(equalToJson(VALID_ALERT_REQUEST_DATE, true, false)))

        val result = alertRepository.findPaginated(AlertSearchCriteria(
                0, 20, null,
                ZonedDateTime.of(2020, 10, 30, 12, 24, 12, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, 10, 30, 12, 24, 13, 0, ZoneId.of("UTC")),
                null,null, null,
                listOf("dateRaised")
        ))

        assertThat(result.totalResults).isEqualTo(2)
        assertThat(result.items[0].alertId).isEqualTo(3141)
        assertThat(result.items[0].entities[0].name).isEqualTo("Nordea")
    }

    @Test
    fun `should return 200 with completely filled`() {
        mockServer.stubFor(
                post(urlEqualTo("/alerts"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_ALERT_RESPONSE))
                        .withRequestBody(equalToJson(VALID_ALERT_REQUEST_ALL_PARAMS)))

        val result = alertRepository.findPaginated(AlertSearchCriteria(
                0, 20,
                singletonList("high"),
                ZonedDateTime.of(2020, 10, 29, 12, 24, 12, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2020, 11, 1, 12, 24, 12, 0, ZoneId.of("UTC")),
                singletonList("rejected-central-bank"),
                singletonList("NDEASESSXXX"),
                "3141",
                listOf("dateRaised")
        ))

        assertThat(result.totalResults).isEqualTo(2)
        assertThat(result.items[0].alertId).isEqualTo(3141)
        assertThat(result.items[0].priority).isEqualTo(AlertPriorityType.HIGH)
        assertThat(result.items[0].entities[0].name).isEqualTo("Nordea")
    }

    @Test
    fun `should fail if invalid jsonBody received and throw ClientException`() {
        mockServer.stubFor(
                post(urlEqualTo("/alerts"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(INVALID_RESPONSE)
                        )
                        .withRequestBody(equalToJson(VALID_ALERT_REQUEST)))

        assertThatExceptionOfType(InfrastructureException::class.java).isThrownBy {
            alertRepository.findPaginated(AlertSearchCriteria(
                    0, 20, null, null, null,null,null,
                    null, listOf("-dateRaised")
            ))
        }
    }

    @Test
    fun `should fail if 500 returned and throw ClientException`() {
        mockServer.stubFor(
                post(urlEqualTo("/alerts"))
                        .willReturn(aResponse()
                                .withStatus(500)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_ALERT_RESPONSE)
                        )
                        .withRequestBody(equalToJson(VALID_ALERT_REQUEST)))

        assertThatExceptionOfType(InfrastructureException::class.java).isThrownBy {
            alertRepository.findPaginated(AlertSearchCriteria(
                    0, 20, null, null, null,null,null,
                    null, listOf("-dateRaised")
            ))
        }
    }

    @Test
    fun `should fail if 404 returned and throw ClientException`() {
        mockServer.stubFor(
                post(urlEqualTo("/alerts"))
                        .willReturn(aResponse()
                                .withStatus(404)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_ALERT_RESPONSE)
                        )
                        .withRequestBody(equalToJson(VALID_ALERT_REQUEST)))

        assertThatExceptionOfType(InfrastructureException::class.java).isThrownBy {
            alertRepository.findPaginated(AlertSearchCriteria(
                    0, 20, null, null, null,null,null,
                    null, listOf("-dateRaised")
            ))
        }.withMessageContaining("Timeout!")
    }
}