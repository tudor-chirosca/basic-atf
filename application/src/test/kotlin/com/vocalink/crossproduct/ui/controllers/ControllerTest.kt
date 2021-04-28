package com.vocalink.crossproduct.ui.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.aspects.AuditAspect
import com.vocalink.crossproduct.ui.aspects.EventType
import com.vocalink.crossproduct.ui.aspects.OccurringEvent
import com.vocalink.crossproduct.ui.aspects.OperationType
import com.vocalink.crossproduct.ui.facade.api.AuditFacade
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Fail
import org.mockito.ArgumentCaptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import java.nio.charset.Charset

@SpringBootTest
@ContextConfiguration(classes = [TestConfig::class])
@AutoConfigureMockMvc
open class ControllerTest {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var auditFacade: AuditFacade

    val UTF8_CONTENT_TYPE: MediaType = MediaType(MediaType.APPLICATION_JSON.type,
            MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

    val eventCaptor: ArgumentCaptor<OccurringEvent> = ArgumentCaptor.forClass(OccurringEvent::class.java)

    private fun assertAuditEvents(eventRequest: OccurringEvent, eventResponse: OccurringEvent,
                                  eventType: EventType, responseContent: String) {
        assertThat(eventRequest.product).isEqualTo(TestConstants.CONTEXT)
        assertThat(eventResponse.product).isEqualTo(TestConstants.CONTEXT)
        assertThat(eventRequest.client).isEqualTo(TestConstants.CLIENT_TYPE)
        assertThat(eventResponse.client).isEqualTo(TestConstants.CLIENT_TYPE)
        assertThat(eventRequest.requestUrl).isEqualTo(eventResponse.requestUrl)
        assertThat(eventRequest.operationType).isEqualTo(OperationType.REQUEST)
        assertThat(eventResponse.operationType).isEqualTo(OperationType.RESPONSE)
        assertThat(eventResponse.correlationId).isEqualTo(eventResponse.correlationId)
        assertThat(eventRequest.eventType).isEqualTo(eventType)
        assertThat(eventRequest.content).isNotEmpty()
        assertThat(eventResponse.content).isEqualTo(responseContent)
        try {
            objectMapper.readValue(eventRequest.content, eventType.getRequestType())
        } catch (e: Exception) {
            Fail.fail<Exception>("Cannot cast request event content", e)
        }
    }

    fun assertAuditEventsSuccess(eventRequest: OccurringEvent, eventResponse: OccurringEvent,
                                         eventType: EventType) {
        assertAuditEvents(eventRequest, eventResponse, eventType, AuditAspect.RESPONSE_SUCCESS)
    }

    fun assertAuditEventsFailed(eventRequest: OccurringEvent, eventResponse: OccurringEvent,
                                eventType: EventType) {
        assertAuditEvents(eventRequest, eventResponse, eventType, AuditAspect.RESPONSE_FAILURE)
    }

}
