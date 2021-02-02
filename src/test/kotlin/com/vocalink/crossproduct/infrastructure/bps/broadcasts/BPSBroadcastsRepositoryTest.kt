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

@BPSTestConfiguration
@Import(BPSBroadcastsRepository::class)
class BPSBroadcastsRepositoryTest @Autowired constructor(var repository: BPSBroadcastsRepository,
                                                         var mockServer: WireMockServer) {

    companion object {
        var request = BroadcastsSearchCriteria(0, 20, null, null, null, null, null)

        var SIMPLE_REQUEST = """
            {
            "offset": 0,
            "limit": 20
            }
        """

        var SIMPLE_RESPONSE = """
            {
            }
        """
    }

    @Test
    fun run() {
        mockServer.stubFor(
                post(urlEqualTo("/broadcasts"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(SIMPLE_RESPONSE)
                                )
                        .withRequestBody(equalToJson(SIMPLE_REQUEST)))
        repository.findPaginated(request)
    }

}