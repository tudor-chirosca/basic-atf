package com.vocalink.crossproduct.infrastructure.config;

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doCallRealMethod
import org.mockito.Mockito.mock
import javax.servlet.http.HttpServletRequest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppFilterTest {

    private var filter = mock(ClientContextFilter::class.java) as AppFilter
    private var servletRequest = mock(HttpServletRequest::class.java)

    @BeforeAll
    fun init() {
        filter.whitelistedEndpoints = arrayOf("/actuator/**")
        filter.contextPath = "/international-suite-service"
    }

    @Test
    fun `should not filter whitelisted endpoints`() {
        `when`(servletRequest.requestURI).thenReturn("/international-suite-service/actuator/info")

        doCallRealMethod().`when`(filter).shouldNotFilter(servletRequest)

        val filterResult = filter.shouldNotFilter(servletRequest)

        assertThat(filterResult).isTrue()
    }

    @Test
    fun `should filter non-whitelisted endpoints`() {
        `when`(servletRequest.requestURI).thenReturn("/international-suite-service/notWhiteListed")

        doCallRealMethod().`when`(filter).shouldNotFilter(servletRequest)

        val filterResult = filter.shouldNotFilter(servletRequest)

        assertThat(filterResult).isFalse()
    }
}
