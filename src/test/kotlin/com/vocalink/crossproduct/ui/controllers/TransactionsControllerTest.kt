package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto
import com.vocalink.crossproduct.ui.facade.TransactionsFacade
import java.math.BigDecimal
import java.nio.charset.Charset
import java.time.ZoneId
import java.time.ZonedDateTime
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TransactionsApi::class)
@ContextConfiguration(classes=[TestConfig::class])
class TransactionsControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var transactionsFacade: TransactionsFacade
    
    private val UTF8_CONTENT_TYPE: MediaType = MediaType(MediaType.APPLICATION_JSON.type,
            MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

    private companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val INVALID_BODY_REQUEST = """{}"""

        const val INVALID_DATE_AND_CYCLE_IDS_BODY_REQUEST = """{
            "messageDirection" : "sending",
            "dateFrom": "2020-10-23",
            "dateTo": "2020-10-23",
            "cycleIds": [
              "20190212004"
            ]
        }"""

        const val INVALID_DATE_FROM_EARLIER_THAN_30_DAYS_BODY_REQUEST = """{
            "messageDirection" : "sending",
            "dateFrom": "2020-10-23"
        }"""

        const val INVALID_LIMIT_BODY_REQUEST = """{
            "messageDirection" : "sending",
            "limit": "0"
        }"""

        const val INVALID_WILDCARD_IN_MIDDLE_BODY_REQUEST = """{
            "messageDirection" : "sending",
            "id": "BANK*YY"
        }"""

        const val INVALID_WILDCARD_SPECIAL_CHARACTERS_BODY_REQUEST = """{
            "messageDirection" : "sending",
            "id": "BANK()[]"
        }"""

        const val INVALID_REASON_CODE_WITH_NO_STATUS_DAYS_BODY_REQUEST = """{
            "messageDirection" : "sending",
            "reasonCode": "F01"
        }"""

        const val INVALID_REASON_CODE_WITH_ACCEPTED_STATUS_DAYS_BODY_REQUEST = """{
            "messageDirection" : "sending",
            "reasonCode": "F01",
            "status": "accepted"
        }"""

        const val INVALID_SAME_SENDING_AND_RECEIVING_BIC_BODY_REQUEST = """{
            "messageDirection" : "sending",
            "sendingBic": "AVANSESX",
            "receivingBic": "AVANSESX"
        }"""

        const val INVALID_BODY_PARAM_NAMES_REQUEST = """{
            "offset" : 0,
            "limit" : 20,
            "messageDirection" : "sending",
            "invalidName" : "xxxxx"
        }"""

        const val VALID_MIN_BODY_REQUEST = """{
            "offset" : 0,
            "limit" : 20,
            "messageDirection" : "sending"
        }"""

        const val VALID_REJECTED_BODY_REQUEST = """{
            "offset" : 0,
            "limit" : 20,
            "messageDirection" : "sending",
            "status": "rejected",
            "reasonCode": "F01"
        }"""

        const val VALID_BODY_REQUEST = """{
            "offset" : 0,
            "limit" : 20,
            "cycleIds": [
                "20190212004"
            ],
            "messageDirection": "sending",
            "messageType": "pacs.008",
            "sendingBic": "AVANSESX",
            "receivingBic": "CARNSES1",
            "status": "rejected",
            "reasonCode": "F04",
            "id": "BANK*",
            "sendingAccount": "SE91 9500 0099 6042 0638 7369",
            "receivingAccount": "SE91 9500 0099 6042 0638 7369",
            "txnFrom": 125.5,
            "txnTo": 510,
            "offset": 0,
            "limit": 20,
            "sort": [
              "+createdAt"
            ]
        }"""

        const val VALID_PAGE_RESPONSE = """{
            "totalResults": 1,
            "items": [
                {
                "instructionId": "20210115CARNSES1B2199",
                "createdAt": "2021-01-14T15:02:14Z",
                "senderBic": "CARNSES1",
                "messageType": "rocs.001",
                "amount": 3334343.80,
                "status": "accepted"
                }
            ]
        }"""
    }

    @Test
    fun `should return 200 with minimal body params and return valid response`() {
        val transaction = TransactionDto(
                "20210115CARNSES1B2199",
                ZonedDateTime.of(2021, 1, 14, 15, 2, 14 ,0, ZoneId.of("UTC")),
                "CARNSES1",
                "rocs.001",
                BigDecimal.valueOf(3334343.80),
                "accepted"
        )
        `when`(transactionsFacade.getPaginated(any(), any(), any()))
                .thenReturn(PageDto(1, listOf(transaction)))
        mockMvc.perform(post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_MIN_BODY_REQUEST))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_PAGE_RESPONSE))
    }

    @Test
    fun `should return 200 with full body request`() {
        `when`(transactionsFacade.getPaginated(any(), any(), any()))
                .thenReturn(PageDto(0, null))
        mockMvc.perform(post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_BODY_REQUEST))
                .andExpect(status().isOk)
    }

    @Test
    fun `should fail with 400 on invalid param names in body`() {
        mockMvc.perform(post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_BODY_PARAM_NAMES_REQUEST))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Unrecognized field")))
    }

    @Test
    fun `should fail with 400 when required msg_direction is not specified`() {
        mockMvc.perform(post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_BODY_REQUEST))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("msg_direction in request parameters in empty or missing")))
    }

    @Test
    fun `should fail with 400 when cycleIds and date_to are both in request`() {
        mockMvc.perform(post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_DATE_AND_CYCLE_IDS_BODY_REQUEST))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("cycle_ids and date_to are both included "
                        + "in request params, exclude one of them")))
    }

    @Test
    fun `should fail with 400 when send_bic and recv_bic are with same values`() {
        mockMvc.perform(post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_SAME_SENDING_AND_RECEIVING_BIC_BODY_REQUEST))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("send_bic and recv_bic should not be the same")))
    }

    @Test
    fun `should fail with 400 when dateFrom is earlier than 30 days from today`() {
        mockMvc.perform(post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_DATE_FROM_EARLIER_THAN_30_DAYS_BODY_REQUEST))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("date_from can not be earlier than 30 days")))
    }

    @Test
    fun `should fail with 400 when has reasonCode but missing status`() {
        mockMvc.perform(post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_REASON_CODE_WITH_NO_STATUS_DAYS_BODY_REQUEST))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Reason code should not be any of the rejected types")))
    }

    @Test
    fun `should fail with 400 when has reasonCode but status is other than Rejected`() {
        mockMvc.perform(post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_REASON_CODE_WITH_ACCEPTED_STATUS_DAYS_BODY_REQUEST))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Reason code should not be any of the rejected types")))
    }

    @Test
    fun `should fail with 400 when id wildcard search string has * on middle of the word`() {
        mockMvc.perform(post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_WILDCARD_IN_MIDDLE_BODY_REQUEST))
                .andExpect(status().is4xxClientError)
                .andExpect(content()
                        .string(containsString("wildcard '*' can not be in the middle and id should not contain special symbols beside '.' and '_'")))
    }

    @Test
    fun `should fail with 400 when id wildcard search string has other special characters than allowed by the regex`() {
        mockMvc.perform(post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_WILDCARD_SPECIAL_CHARACTERS_BODY_REQUEST))
                .andExpect(status().is4xxClientError)
                .andExpect(content()
                        .string(containsString("wildcard '*' can not be in the middle and id should not contain special symbols beside '.' and '_'")))
    }

    @Test
    fun `should return 200 when has reason code and status is rejected`() {
        `when`(transactionsFacade.getPaginated(any(), any(), any()))
                .thenReturn(PageDto(0, null))
        mockMvc.perform(post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_REJECTED_BODY_REQUEST))
                .andExpect(status().isOk)
    }

    @Test
    fun `should fail with 400 when limit is less than 1`() {
        mockMvc.perform(post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_LIMIT_BODY_REQUEST))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("Limit should be equal or higher than 1")))
    }
}
