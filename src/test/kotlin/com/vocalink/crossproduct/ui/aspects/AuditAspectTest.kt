package com.vocalink.crossproduct.ui.aspects

import com.vocalink.crossproduct.infrastructure.config.JacksonConfig
import com.vocalink.crossproduct.ui.aspects.EventType.SETTL_DETAILS
import com.vocalink.crossproduct.ui.facade.api.AuditFacade
import com.vocalink.crossproduct.ui.presenter.ClientType.SYSTEM
import org.apache.commons.lang.RandomStringUtils.random
import org.apache.commons.lang3.StringUtils.EMPTY
import org.aspectj.lang.ProceedingJoinPoint
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doCallRealMethod
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.Optional
import javax.servlet.http.HttpServletRequest

class AuditAspectTest {

    private val auditAspect = mock(AuditAspect::class.java)!!
    private lateinit var joinPoint: ProceedingJoinPoint
    private lateinit var auditable: Auditable
    private lateinit var positions: Positions

    private companion object {
        const val PRODUCT = "BPS"
        const val USER_ID = "User_ID"
        const val PARTICIPANT_ID = "Participant_ID"
        const val REQUEST_URL = "http://hostname"
        val CLIENT_TYPE = SYSTEM
        val EVENT_TYPE = SETTL_DETAILS
        val HTTP_REQUEST = mock(HttpServletRequest::class.java)!!
        val OPERATION_TYPE_REQUEST = OperationType.REQUEST
        val OPERATION_TYPE_RESPONSE = OperationType.RESPONSE
        val CONTENT = random(128)!!
        val ENDPOINT_PARAMETERS = arrayOf(PRODUCT, mapOf("limit" to "100", "offset" to "50"), CLIENT_TYPE, HTTP_REQUEST)
    }

    @BeforeEach
    internal fun setUp() {
        joinPoint = mock(ProceedingJoinPoint::class.java)
        auditable = mock(Auditable::class.java)
        positions = mock(Positions::class.java)
        `when`(auditable.params).thenReturn(positions)
        `when`(joinPoint.args).thenReturn(ENDPOINT_PARAMETERS)
    }

    @Test
    fun `should return empty string as product when parameter position not set`() {
        `when`(positions.context).thenReturn(Positions.POSITION_NOT_SET)
        doCallRealMethod().`when`(auditAspect).getProduct(joinPoint, auditable)

        val product = auditAspect.getProduct(joinPoint, auditable)

        assertThat(product).isEqualTo(EMPTY)
    }

    @Test
    fun `should return product when parameter position is set`() {
        `when`(positions.context).thenReturn(0)
        doCallRealMethod().`when`(auditAspect).getProduct(joinPoint, auditable)

        val product = auditAspect.getProduct(joinPoint, auditable)

        assertThat(product).isEqualTo(PRODUCT)
    }

    @Test
    fun `should return empty string as content when parameter position not set`() {
        `when`(positions.content).thenReturn(Positions.POSITION_NOT_SET)
        doCallRealMethod().`when`(auditAspect).getContent(joinPoint, auditable)

        val content = auditAspect.getContent(joinPoint, auditable)

        assertThat(content).isEqualTo(EMPTY)
    }

    @Test
    fun `should return content JSON when parameter position is set`() {
        val contentUtils = ContentUtils(JacksonConfig().objectMapper())
        `when`(positions.content).thenReturn(1)
        `when`(auditAspect.contentUtils).thenReturn(contentUtils)
        doCallRealMethod().`when`(auditAspect).getContent(joinPoint, auditable)

        val content = auditAspect.getContent(joinPoint, auditable)

        assertThat(content).isEqualTo(contentUtils.toJsonString(ENDPOINT_PARAMETERS[1]))
    }

    @Test
    fun `should return empty string as client type when parameter position not set`() {
        `when`(positions.clientType).thenReturn(Positions.POSITION_NOT_SET)
        doCallRealMethod().`when`(auditAspect).getClientType(joinPoint, auditable)

        val clientType = auditAspect.getClientType(joinPoint, auditable)

        assertThat(clientType).isEqualTo(EMPTY)
    }

    @Test
    fun `should return client type when parameter position is set`() {
        `when`(positions.clientType).thenReturn(2)
        doCallRealMethod().`when`(auditAspect).getClientType(joinPoint, auditable)

        val clientType = auditAspect.getClientType(joinPoint, auditable)

        assertThat(clientType).isEqualTo(CLIENT_TYPE.name)
    }

    @Test
    fun `should return not empty Optional of HTTP request when parameter position is set`() {
        `when`(positions.request).thenReturn(3)
        doCallRealMethod().`when`(auditAspect).getHttpRequest(joinPoint, auditable)

        val request = auditAspect.getHttpRequest(joinPoint, auditable)

        assertThat(request.isPresent).isTrue()
        assertThat(request.get()).isEqualTo(HTTP_REQUEST)
    }

    @Test
    fun `should return Optional empty as HTTP request when parameter position not set`() {
        `when`(positions.request).thenReturn(Positions.POSITION_NOT_SET)
        doCallRealMethod().`when`(auditAspect).getHttpRequest(joinPoint, auditable)

        val request = auditAspect.getHttpRequest(joinPoint, auditable)

        assertThat(request.isPresent).isFalse()
    }

    @Test
    fun `should audit request and response events`() {
        doCallRealMethod().`when`(auditAspect).log(joinPoint, auditable)
        val auditFacade = prepareEvent()
        val eventCaptor = ArgumentCaptor.forClass(OccurringEvent::class.java)

        auditAspect.log(joinPoint, auditable)

        verify(joinPoint).proceed()
        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        val requestEvent = eventCaptor.allValues[0]
        val responseEvent = eventCaptor.allValues[1]
        assertEvent(responseEvent)
        assertThat(requestEvent.operationType).isEqualTo(OPERATION_TYPE_REQUEST)
        assertThat(requestEvent.content).isEqualTo(CONTENT)
        assertEvent(requestEvent)
        assertThat(responseEvent.operationType).isEqualTo(OPERATION_TYPE_RESPONSE)
        assertThat(responseEvent.content).isEqualTo(AuditAspect.RESPONSE_SUCCESS)
    }

    @Test
    fun `should audit failed request event`() {
        doThrow(RuntimeException::class.java).`when`(joinPoint).proceed()
        doCallRealMethod().`when`(auditAspect).log(joinPoint, auditable)
        val auditFacade = prepareEvent()
        val eventCaptor = ArgumentCaptor.forClass(OccurringEvent::class.java)

        assertThatThrownBy { auditAspect.log(joinPoint, auditable) }
                .isExactlyInstanceOf(RuntimeException::class.java)

        verify(joinPoint).proceed()
        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        val requestEvent = eventCaptor.allValues[0]
        val responseEvent = eventCaptor.allValues[1]
        assertEvent(responseEvent)
        assertThat(requestEvent.operationType).isEqualTo(OPERATION_TYPE_REQUEST)
        assertThat(requestEvent.content).isEqualTo(CONTENT)
        assertEvent(requestEvent)
        assertThat(responseEvent.operationType).isEqualTo(OPERATION_TYPE_RESPONSE)
        assertThat(responseEvent.content).isEqualTo(AuditAspect.RESPONSE_FAILURE)
    }

    private fun prepareEvent(): AuditFacade {
        val auditFacade = mock(AuditFacade::class.java)!!
        val request = mock(HttpServletRequest::class.java)
        `when`(auditAspect.auditFacade).thenReturn(auditFacade)
        `when`(request.getHeader(AuditAspect.X_USER_ID_HEADER)).thenReturn(USER_ID)
        `when`(request.getHeader(AuditAspect.X_PARTICIPANT_ID_HEADER)).thenReturn(PARTICIPANT_ID)
        `when`(request.requestURL).thenReturn(StringBuffer(REQUEST_URL))
        `when`(auditAspect.mdcKey).thenReturn(random(10))
        `when`(auditable.type).thenReturn(EVENT_TYPE)
        `when`(auditAspect.getProduct(joinPoint, auditable)).thenReturn(PRODUCT)
        `when`(auditAspect.getContent(joinPoint, auditable)).thenReturn(CONTENT)
        `when`(auditAspect.getClientType(joinPoint, auditable)).thenReturn(CLIENT_TYPE.name)
        `when`(auditAspect.getHttpRequest(joinPoint, auditable)).thenReturn(Optional.of(request))
        return auditFacade
    }

    private fun assertEvent(event: OccurringEvent) {
        assertThat(event.participantId).isEqualTo(PARTICIPANT_ID)
        assertThat(event.userId).isEqualTo(USER_ID)
        assertThat(event.requestUrl).isEqualTo(REQUEST_URL)
        assertThat(event.eventType).isEqualTo(EVENT_TYPE)
        assertThat(event.product).isEqualTo(PRODUCT)
        assertThat(event.client).isEqualTo(CLIENT_TYPE.name)
    }

}
