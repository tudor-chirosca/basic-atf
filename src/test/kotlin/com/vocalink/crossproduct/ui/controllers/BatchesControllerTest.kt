package com.vocalink.crossproduct.ui.controllers


import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto
import com.vocalink.crossproduct.ui.dto.batch.BatchDto
import com.vocalink.crossproduct.ui.dto.file.EnquirySenderDetailsDto
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto
import com.vocalink.crossproduct.ui.facade.BatchesFacade
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BatchesController::class)
class BatchesControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var batchesFacade: BatchesFacade

    private val UTF8_CONTENT_TYPE: MediaType = MediaType(MediaType.APPLICATION_JSON.type,
            MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

    private companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val VALID_PAGE_RESPONSE = """{
            "totalResults": 1,
            "items": [
                {
                    "id": "D27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800105.gz",
                    "createdAt": "2020-10-30T10:10:10",
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
            "fileSize": 3245234523,
            "settlementDate": "2020-11-03",
            "settlementCycleId": "04",
            "createdAt": "2020-10-30T10:10:10",
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
        val dateFrom = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        val dateTo = LocalDate.now().minusDays(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        `when`(batchesFacade.getBatches(any(), any(), any()))
                .thenReturn(PageDto(0, null))
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("date_from", dateFrom)
                .param("date_to", dateTo)
                .param("send_bic", "HANDSESS")
                .param("recv_bic", "NDEASESSSX")
                .param("status", "Accepted")
                .param("id", "2342")
                .param("limit", "20")
                .param("offset", "0")
                .param("sort", "-senderBic"))
                .andExpect(status().isOk)
    }

    @Test
    fun `should return 200 when cycle_ids and other params, without date_to are specified in request`() {
        val dateFrom = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        `when`(batchesFacade.getBatches(any(), any(), any()))
                .thenReturn(PageDto(0, null))
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("cycle_ids", "01")
                .param("date_from", dateFrom)
                .param("cycle_ids", "HANDSESS")
                .param("recv_bic", "NDEASESSSX")
                .param("status", "Accepted")
                .param("id", "_2342*")
                .param("limit", "20")
                .param("offset", "0")
                .param("sort", "-senderBic"))
                .andExpect(status().isOk)
    }

    @Test
    fun `should return 200 when required msg_direction param is specified in request`() {
        val batch = BatchDto.builder()
                .id("D27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800105.gz")
                .createdAt(LocalDateTime.of(2020, 10, 30, 10, 10, 10))
                .senderBic("HANDSESS")
                .messageType("admi.004")
                .nrOfTransactions(12)
                .status("Accepted")
                .build()
        `when`(batchesFacade.getBatches(any(), any(), any()))
                .thenReturn(PageDto(1, listOf(batch)))
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending"))
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
                .param("msg_direction", "Sending"))
                .andExpect(status().isOk)
    }

    @Test
    fun `should fail with 400 when required msg_direction is not specified`() {
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("msg_direction is missing in request params")))
    }

    @Test
    fun `should fail with 400 when cycleIds and date_to are both in request`() {
        val dateTo = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("cycle_ids", "01")
                .param("date_to", dateTo))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("cycle_ids and date_to are both included "
                        + "in request params, exclude one of them")))
    }

    @Test
    fun `should fail with 400 when send_bic and recv_bic are with same values`() {
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("send_bic", "HANDSESS")
                .param("recv_bic", "HANDSESS"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("send_big and recv_bic are the same")))
    }

    @Test
    fun `should fail with 400 when dateFrom is earlier than 30 days from today`() {
        val dateFrom = LocalDate.now().minusDays(31).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("date_from", dateFrom))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("date_from can't be earlier than 30 days")))
    }

    @Test
    fun `should fail with 400 when has reasonCode but missing status`() {
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("reason_code", "F02"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Have reason_code specified with missing "
                        + "status value, or value that is not 'pre-rejected' or 'post-rejected'")))
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
                .andExpect(content().string(containsString("Have reason_code specified with missing "
                        + "status value, or value that is not 'pre-rejected' or 'post-rejected'")))
    }

    @Test
    fun `should fail with 400 when id wildcard search string has * on middle of the word`() {
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("id", "BANK*YY"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("wildcard '*' can not be in the middle and "
                        + "id can't contain special symbols beside '.' and '_'")))
    }

    @Test
    fun `should fail with 400 when id wildcard search string has other special characters than allowed by the regex`() {
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("id", "BANK()[]"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("wildcard '*' can not be in the middle and "
                        + "id can't contain special symbols beside '.' and '_'")))
    }

    @Test
    fun `should return 200 when has reason code and status is pre-rejected`() {
        `when`(batchesFacade.getBatches(any(), any(), any()))
                .thenReturn(PageDto(0, null))
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("status", "pre-rejected")
                .param("reason_code", "F02"))
                .andExpect(status().isOk)
    }

    @Test
    fun `should return 200 when has reason code and status is post-rejected`() {
        `when`(batchesFacade.getBatches(any(), any(), any()))
                .thenReturn(PageDto(0, null))
        mockMvc.perform(get("/enquiry/batches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("status", "post-rejected")
                .param("reason_code", "F02"))
                .andExpect(status().isOk)
    }

    @Test
    fun `should return 200 on get batch by Id`() {
        val id = "A27ISTXBANKSESSXXX2"
        val details = BatchDetailsDto.builder()
                .batchId(id)
                .fileName("$id.gz")
                .nrOfTransactions(12)
                .fileSize(3245234523)
                .settlementDate(LocalDate.of(2020, 11, 3))
                .settlementCycleId("04")
                .createdAt(LocalDateTime.of(2020, 10, 30, 10, 10, 10))
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
    }
}