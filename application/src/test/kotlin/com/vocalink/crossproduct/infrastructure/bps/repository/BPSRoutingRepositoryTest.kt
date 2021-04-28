package com.vocalink.crossproduct.infrastructure.bps.repository

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.domain.routing.RoutingRecordCriteria
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import com.vocalink.crossproduct.infrastructure.bps.routing.BPSRoutingRepository
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
@Import(BPSRoutingRepository::class)
class BPSRoutingRepositoryTest @Autowired constructor(var routingRepository: BPSRoutingRepository,
                                                      var mockServer: WireMockServer) {
    companion object {
        const val VALID_ROUTING_RECORDS_REQUEST: String = """ 
        {
            "offset": 0,
            "limit": 20,
            "sort": null,
            "bic": "HANDSESS"
        } 
        """

        const val VALID_ROUTING_RECORDS_RESULT_LIST_RESPONSE: String = """
        {
             "totalResults": 1,
             "items": [
                  {
                    "schemeParticipantIdentifier": "HANDSESS",
                    "reachableBic": "AVANSESX",
                    "validFrom": "2021-01-31T00:00:00Z",
                    "validTo": "2021-02-03T00:00:00Z",
                    "currency": "SEK"
                  }
            ]
        }
        """
    }

    @Test
    fun `should return the list of batches`() {
        mockServer.stubFor(
                post(urlEqualTo("/routing/P27-SEK/records"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_ROUTING_RECORDS_RESULT_LIST_RESPONSE))
                        .withRequestBody(equalToJson(VALID_ROUTING_RECORDS_REQUEST)))

        val request = RoutingRecordCriteria(
                0, 20, null, "HANDSESS"
        )

        val result = routingRepository.findPaginated(request)

        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(1)
        assertThat(result.items).isNotEmpty

        assertThat(result.items[0].reachableBic).isEqualTo("AVANSESX")
        assertThat(result.items[0].validFrom).isEqualTo(ZonedDateTime.of(2021, Month.JANUARY.value, 31, 0, 0, 0, 0, ZoneId.of("UTC")))
        assertThat(result.items[0].validTo).isEqualTo(ZonedDateTime.of(2021, Month.FEBRUARY.value, 3, 0, 0, 0, 0, ZoneId.of("UTC")))
        assertThat(result.items[0].currency).isEqualTo("SEK")
    }
}