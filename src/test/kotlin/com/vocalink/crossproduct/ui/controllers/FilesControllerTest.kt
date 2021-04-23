package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.config.ControllerFeatures
import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.file.EnquirySenderDetailsDto
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto
import com.vocalink.crossproduct.ui.dto.file.FileDto
import com.vocalink.crossproduct.ui.facade.api.FilesFacade
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ofPattern
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.togglz.junit5.AllDisabled
import org.togglz.junit5.AllEnabled

@WebMvcTest(FilesController::class)
@ContextConfiguration(classes=[TestConfig::class])
class FilesControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var filesFacade: FilesFacade

    private val UTF8_CONTENT_TYPE: MediaType = MediaType(MediaType.APPLICATION_JSON.type,
            MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

    private companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val VALID_PAGE_RESPONSE = """{
            "totalResults": 1,
            "items": [
                {
                    "name": "D27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800105.gz",
                    "createdAt": "2020-10-30T10:10:10Z",
                    "senderBic": "HANDSESS",
                    "messageType": "admi.004",
                    "nrOfBatches": 12,
                    "status": "ACK"
                }
            ]
        }"""

        const val VALID_DETAILS_RESPONSE = """{
            "fileName": "A27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800103.gz",
            "nrOfBatches": 12,
            "fileSize": 3245234523,
            "settlementCycleId": "04",
            "createdAt": "2020-10-30T10:10:10Z",
            "status": "ACK",
            "messageType": "prtp.001SO",
            "sender": {
                "entityName": "Nordea Bank",
                "entityBic": "NDEASESSXXX"
            }
        }"""
    }

    @Test
    fun `should return 200 when date_to, date_from and other params without cycle_ids are specified in request`() {
        val dateFrom = LocalDate.now().format(ofPattern("yyyy-MM-dd"))
        val dateTo = LocalDate.now().minusDays(5).format(ofPattern("yyyy-MM-dd"))

        `when`(filesFacade.getPaginated(any(), any(), any()))
                .thenReturn(PageDto(0, null))
        mockMvc.perform(get("/enquiry/files")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("date_from", dateFrom)
                .param("date_to", dateTo)
                .param("send_bic", "HANDSESS")
                .param("recv_bic", "NDEASESSSX")
                .param("status", "ACK")
                .param("id", "2342")
                .param("limit", "20")
                .param("offset", "0")
                .param("sort", "-senderBic"))
                .andExpect(status().isOk)
    }

    @Test
    fun `should return 200 when cycle_ids and other params, without date_to are specified in request`() {
        val dateFrom = LocalDate.now().format(ofPattern("yyyy-MM-dd"))
        `when`(filesFacade.getPaginated(any(), any(), any()))
                .thenReturn(PageDto(0, null))
        mockMvc.perform(get("/enquiry/files")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("cycle_ids", "01")
                .param("date_from", dateFrom)
                .param("cycle_ids", "HANDSESS")
                .param("recv_bic", "NDEASESSSX")
                .param("status", "ACK")
                .param("id", "*2342.gz")
                .param("limit", "20")
                .param("offset", "0")
                .param("sort", "-senderBic"))
                .andExpect(status().isOk)
    }

    @Test
    fun `should return 200 when required msg_direction param is specified in request`() {
        val file = FileDto.builder()
                .name("D27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800105.gz")
                .createdAt(ZonedDateTime.of(2020, 10, 30, 10, 10, 10, 0, ZoneId.of("UTC")))
                .senderBic("HANDSESS")
                .messageType("admi.004")
                .nrOfBatches(12)
                .status("ACK")
                .build()
        `when`(filesFacade.getPaginated(any(), any(), any()))
                .thenReturn(PageDto(1, listOf(file)))
        mockMvc.perform(get("/enquiry/files")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending"))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_PAGE_RESPONSE))
    }

    @Test
    fun `should ignore wrong param names and use only expected params`() {
        mockMvc.perform(get("/enquiry/files")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("not_expected_param_name", "some_value")
                .param("msg_direction", "Sending"))
                .andExpect(status().isOk)
    }

    @Test
    fun `should fail with 400 when required msg_direction is not specified`() {
        mockMvc.perform(get("/enquiry/files")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("msg_direction in request parameters in empty or missing")))
    }

    @Test
    fun `should fail with 400 when dateFrom is earlier than DAYS_LIMIT`() {
        val dateFrom = LocalDate.now().minusDays((getDefault(DtoProperties.DAYS_LIMIT).toLong())+1)
                .format(ofPattern("yyyy-MM-dd"))
        mockMvc.perform(get("/enquiry/files")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("date_from", dateFrom))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("date_from can not be earlier than DAYS_LIMIT")))
    }

    @Test
    fun `should fail with 400 when has reasonCode but missing status`() {
        mockMvc.perform(get("/enquiry/files")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("reason_code", "F02"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Reason code should not be any of the rejected types")))
    }

    @Test
    fun `should fail with 400 when limit is less than 1`() {
        mockMvc.perform(get("/enquiry/files")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("limit", "0"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Limit should be equal or higher than 1")))
    }

    @Test
    fun `should fail with 400 when has reasonCode but status is other than Rejected`() {
        mockMvc.perform(get("/enquiry/files")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("status", "Accepted")
                .param("reason_code", "F02"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Reason code should not be any of the rejected types")))
    }


    @Test
    fun `should fail with 400 when id wildcard search string has wildcard on middle of the word`() {
        mockMvc.perform(get("/enquiry/files")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("id", "BANK*YY"))
                .andExpect(status().is4xxClientError)
                .andExpect(content()
                        .string(containsString("wildcard '*' can not be in the middle and id should not contain special symbols beside '.' and '_'")))
    }

    @Test
    fun `should fail with 400 when id wildcard search string has other special characters than allowed by the regex`() {
        mockMvc.perform(get("/enquiry/files")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("id", "BANK()[]"))
                .andExpect(status().is4xxClientError)
                .andExpect(content()
                        .string(containsString("wildcard '*' can not be in the middle and id should not contain special symbols beside '.' and '_'")))
    }

    @Test
    fun `should fail with 400 on wrong sorting param`() {
        mockMvc.perform(get("/enquiry/files")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("limit", "20")
                .param("offset", "0")
                .param("sort", "-wrong_param"))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Wrong sorting parameter")))
    }

    @Test
    fun `should return 200 when has reason code and status is Rejected`() {
        `when`(filesFacade.getPaginated(any(), any(), any()))
                .thenReturn(PageDto(0, null))
        mockMvc.perform(get("/enquiry/files")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .param("msg_direction", "Sending")
                .param("status", "NAK")
                .param("reason_code", "F02"))
                .andExpect(status().isOk)
    }

    @Test
    fun `should return 200 on get file by Id`() {
        val id = "A27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800103.gz"
        val details = FileDetailsDto.builder()
                .fileName(id)
                .nrOfBatches(12)
                .fileSize(3245234523)
                .settlementCycleId("04")
                .createdAt(ZonedDateTime.of(2020, 10, 30, 10, 10, 10, 0, ZoneId.of("UTC")))
                .status("ACK")
                .messageType("prtp.001SO")
                .sender(EnquirySenderDetailsDto.builder()
                        .entityName("Nordea Bank")
                        .entityBic("NDEASESSXXX")
                        .build())
                .build()
        `when`(filesFacade.getDetailsById(any(), any(), any())).thenReturn(details)
        mockMvc.perform(get("/enquiry/files/$id")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_DETAILS_RESPONSE))
    }

    @Test
    @AllDisabled(ControllerFeatures::class)
    fun `should return 415 on download file by Id when file download is disabled`() {
        val id = "A27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800103.gz"
        val stream = InputStreamResource(ByteArrayInputStream(byteArrayOf(125, 12)))
        `when`(filesFacade.getFile(any(), any(), any())).thenReturn(stream)
        mockMvc.perform(get("/enquiry/files/$id")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isUnsupportedMediaType)
    }

    @Test
    @AllEnabled(ControllerFeatures::class)
    fun `should return 415 on download file by Id when file download is enabled`() {
        val id = "A27ISTXBANKSESSXXX201911320191113135321990.NCTSEK_PACS00800103.gz"
        val stream = InputStreamResource(ByteArrayInputStream(byteArrayOf(125, 12)))
        `when`(filesFacade.getFile(any(), any(), any())).thenReturn(stream)
        mockMvc.perform(get("/enquiry/files/$id")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk)
                .andExpect(content().bytes(byteArrayOf(125, 12)))
    }
}
