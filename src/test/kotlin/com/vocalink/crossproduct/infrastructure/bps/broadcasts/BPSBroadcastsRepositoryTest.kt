package com.vocalink.crossproduct.infrastructure.bps.broadcasts;

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.domain.broadcasts.BroadcastsSearchCriteria
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.time.ZonedDateTime

@BPSTestConfiguration
@Import(BPSBroadcastsRepository::class)
class BPSBroadcastsRepositoryTest @Autowired constructor(var repository: BPSBroadcastsRepository,
                                                         var mockServer: WireMockServer) {

    companion object {

        var SIMPLE_REQUEST = """
            {
            "offset": 0,
            "limit": 20
            }
        """
        var FULL_REQUEST = """
            {
            "offset": 0,
            "limit": 20,
            "dateFrom": "2021-01-22T00:00:00Z",
            "dateTo": "2021-01-23T00:00:00Z",
            "msg": "any",
            "recipient": "rspnt"
            }
        """
        var SIMPLE_RESPONSE = """
            {
            "totalResults": 1,
            "items": []
            }
        """
        var FULL_RESPONSE = """
            {
            "totalResults": 1,
            "items": [
                {
                    "createdAt": "2021-01-25T00:00:00Z",
                    "broadcastId": "0000 0100",
                    "msg": "The world is slowly recovering from the aftermath. Please assure you have sufficient funds",
                    "recipients": ["NDEASESSXXX"]
                }
            ]
        }
        """
    }

    @Test
    fun `should return 200 on simple request`() {
        val request = BroadcastsSearchCriteria.builder().offset(0).limit(20).build()

        mockServer.stubFor(
                post(urlEqualTo("/broadcasts"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(SIMPLE_RESPONSE))
                        .withRequestBody(equalToJson(SIMPLE_REQUEST, true, false)))

        repository.findPaginated(request)
    }

    @Test
    fun `should map full request body and full response and return 200`() {
        val request = BroadcastsSearchCriteria.builder()
                .offset(0)
                .limit(20)
                .dateFrom(ZonedDateTime.parse("2021-01-22T00:00:00Z"))
                .dateTo(ZonedDateTime.parse("2021-01-23T00:00:00Z"))
                .msg("any")
                .recipient("rspnt")
                .build()

        mockServer.stubFor(
                post(urlEqualTo("/broadcasts"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(FULL_RESPONSE))
                        .withRequestBody(equalToJson(FULL_REQUEST, true, false)))

        repository.findPaginated(request)
    }

}