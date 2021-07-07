package com.vocalink.crossproduct.ui.controllers


import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.aspects.EventType
import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto
import com.vocalink.crossproduct.ui.dto.batch.BatchDto
import com.vocalink.crossproduct.ui.dto.file.EnquirySenderDetailsDto
import com.vocalink.crossproduct.ui.facade.api.BatchesFacade
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class BatchesControllerTest : ControllerTest() {

    @MockBean
    private lateinit var batchesFacade: BatchesFacade

    private companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val VALID_PAGE_RESPONSE = """{
            "totalResults": 1,
            "items": [
                {
                    "id": "D27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800105.gz",
                    "createdAt": "2020-10-30T10:10:10Z",
                    "senderBic": "HANDSESS",
                    "messageType": "admi.004",
                    "nrOfTransactions": 12,
                    "status": "Accepted"
                }
            ]
        }"""

        const val VALID_DETAILS_RESPONSE = """{
            "batchId": "A27ISTXBANKSESSXXX2",
            "fileName": "A27ISTXBANKSESSXXX2.gz",
            "nrOfTransactions": 12,
            "settlementDate": "2020-10-30T10:10:10Z",
            "settlementCycleId": "04",
            "createdAt": "2020-10-30T10:10:10Z",
            "status": "Accepted",
            "messageType": "prtp.001SO",
            "sender": {
                "entityName": "Nordea Bank",
                "entityBic": "NDEASESSXXX"
            }
        }"""
    }

    @Test
    fun `should return 200 when date_to, date_from and other params without cycle_ids are specified in request`() {
        val dateFrom = ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_DATE_TIME)
        val dateTo = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5)
                .format(DateTimeFormatter.ISO_DATE_TIME)

        `when`(batchesFacade.getPaginated(any(), any(), any()))
                .thenReturn(PageDto(0, null))
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("date_from", dateFrom)
                .param("date_to", dateTo)
                .param("participant_bic", "NDEASESSSX")
                .param("status", "Accepted")
                .param("id", "2342")
                .param("limit", "20")
                .param("offset", "0")
                .param("sort", "-senderBic"))
                .andExpect(status().isOk)
    }

    @Test
    fun `should return 200 when cycle_ids and other params, without date_to are specified in request`() {
        val dateFrom = ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
        `when`(batchesFacade.getPaginated(any(), any(), any()))
                .thenReturn(PageDto(0, null))
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("cycle_id", "01")
                .param("date_from", dateFrom)
                .param("cycle_id", "HANDSESS")
                .param("participant_bic", "NDEASESSSX")
                .param("status", "Accepted")
                .param("id", "_2342*")
                .param("limit", "20")
                .param("offset", "0")
                .param("sort", "-senderBic"))
                .andExpect(status().isOk)
    }

    @Test
    fun `should return 200 when required msg_direction and participant_id param is specified in request`() {
        val batch = BatchDto.builder()
                .id("D27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800105.gz")
                .createdAt(ZonedDateTime.of(2020, 10, 30, 10, 10, 10, 0, ZoneId.of("UTC")))
                .senderBic("HANDSESS")
                .messageType("admi.004")
                .nrOfTransactions(12)
                .status("Accepted")
                .build()
        `when`(batchesFacade.getPaginated(any(), any(), any()))
                .thenReturn(PageDto(1, listOf(batch)))
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("cycle_id", "20190212004"))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_PAGE_RESPONSE))
    }

    @Test
    fun `should ignore wrong param names and use only expected params`() {
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("not_expected_param_name", "some_value")
                .param("participant_bic", "NDEASESSSX")
                .param("msg_direction", "Sending")
                .param("cycle_id", "20190212004"))
                .andExpect(status().isOk)

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.BATCH_ENQUIRY)
    }

    @Test
    fun `should fail with 400 when required msg_direction is not specified`() {
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("cycle_id", "20190212004")
                .param("participant_bic", "NDEASESSSX"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Message direction in request is empty or missing")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 when required cicle_id or date_from, date_to are not specified`() {
        mockMvc.perform(get("/enquiry/batches")
            .contentType(UTF8_CONTENT_TYPE)
            .header(CONTEXT_HEADER, TestConstants.CONTEXT)
            .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
            .param("msg_direction", "Sending")
            .param("participant_bic", "NDEASESSSX"))
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("CycleId either both dateFrom and dateTo must not be null")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 when dateFrom is earlier than DAYS_LIMIT from today`() {
        val dateFrom = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(
                (getDefault(DtoProperties.DAYS_LIMIT).toLong()) + 1).format(DateTimeFormatter.ISO_DATE_TIME)
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("participant_bic", "NDEASESSSX")
                .param("date_from", dateFrom))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("date_from can not be earlier than DAYS_LIMIT")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 when has reasonCode but missing status`() {
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("participant_bic", "NDEASESSSX")
                .param("reason_code", "F02"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Reason code should not be any of the rejected types")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 when has reasonCode but status is other than Rejected`() {
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("status", "Accepted")
                .param("reason_code", "F02"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Reason code should not be any of the rejected types")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 when limit is less than 1`() {
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("participant_bic", "NDEASESSSX")
                .param("limit", "0"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Limit should be equal or higher than 1")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 when id wildcard search string has * on middle of the word`() {
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("participant_bic", "NDEASESSSX")
                .param("id", "BANK*YY"))
                .andExpect(status().is4xxClientError)
                .andExpect(content()
                        .string(containsString("wildcard '*' can not be in the middle and id should not contain special symbols beside '.' and '_'")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 when id wildcard search string has other special characters than allowed by the regex`() {
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("participant_bic", "NDEASESSSX")
                .param("id", "BANK()[]"))
                .andExpect(status().is4xxClientError)
                .andExpect(content()
                        .string(containsString("wildcard '*' can not be in the middle and id should not contain special symbols beside '.' and '_'")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 on wrong sorting param`() {
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("limit", "20")
                .param("offset", "0")
                .param("msg_direction", "Sending")
                .param("participant_bic", "NDEASESSSX")
                .param("sort", "-wrong_param"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Wrong sorting parameter")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should return 200 when has reason code and status is pre-rejected`() {
        `when`(batchesFacade.getPaginated(any(), any(), any()))
                .thenReturn(PageDto(0, null))
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("participant_bic", "NDEASESSSX")
                .param("status", "PRE-RJCT")
                .param("reason_code", "F02")
                .param("cycle_id", "20190212004"))
                .andExpect(status().isOk)

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.BATCH_ENQUIRY)
    }

    @Test
    fun `should return 200 when has reason code and status is post-rejected`() {
        `when`(batchesFacade.getPaginated(any(), any(), any()))
                .thenReturn(PageDto(0, null))
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("participant_bic", "NDEASESSSX")
                .param("status", "POST-RJCT")
                .param("reason_code", "F02")
                .param("cycle_id", "20190212004"))
                .andExpect(status().isOk)

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.BATCH_ENQUIRY)
    }

    @Test
    fun `should return 200 on get batch by Id`() {
        val dateTime = ZonedDateTime.of(2020, 10, 30, 10, 10, 10, 0, ZoneId.of("UTC"))

        val id = "A27ISTXBANKSESSXXX2"
        val details = BatchDetailsDto.builder()
                .batchId(id)
                .fileName("$id.gz")
                .nrOfTransactions(12)
                .settlementDate(dateTime)
                .settlementCycleId("04")
                .createdAt(dateTime)
                .status("Accepted")
                .messageType("prtp.001SO")
                .sender(EnquirySenderDetailsDto.builder()
                        .entityName("Nordea Bank")
                        .entityBic("NDEASESSXXX")
                        .build())
                .build()
        `when`(batchesFacade.getDetailsById(any(), any(), any())).thenReturn(details)
        mockMvc.perform(get("/enquiry/batches/$id")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_DETAILS_RESPONSE))

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.BATCH_DETAILS)
    }
}
