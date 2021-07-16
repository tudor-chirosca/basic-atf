package com.vocalink.crossproduct.infrastructure.bps.account

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@BPSTestConfiguration
@Import(BPSAccountRepository::class)
class BPSAccountRepositoryTest @Autowired constructor(var accountRepository: BPSAccountRepository,
                                                      var mockServer: WireMockServer) {
    companion object {
        const val VALID_ACCOUNT_REQUEST: String = """ 
        {
            "partyCode": "HANDSESS"
        }"""

        const val VALID_ACCOUNT_RESPONSE: String = """
            {
            "partyCode": "HANDSESS",
            "accountNo": "200002",
            "iban": "SE23 9999 9999 9999 9999 2150"
        }"""
    }

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `should return account by party code`() {
        mockServer.stubFor(
                post(urlEqualTo("/account/read"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_ACCOUNT_RESPONSE))
                        .withRequestBody(equalToJson(VALID_ACCOUNT_REQUEST)))

        val result = accountRepository.findByPartyCode("HANDSESS")
        assertThat(result.iban).isEqualTo("SE23 9999 9999 9999 9999 2150")
        assertThat(result.accountNo).isEqualTo("200002")
        assertThat(result.partyCode).isEqualTo("HANDSESS")
    }
}