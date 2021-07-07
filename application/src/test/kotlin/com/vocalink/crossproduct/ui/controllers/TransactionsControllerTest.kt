package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.aspects.EventType
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDetailsDto
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto
import com.vocalink.crossproduct.ui.dto.transaction.TransactionReceiverDetailsDto
import com.vocalink.crossproduct.ui.dto.transaction.TransactionSenderDetailsDto
import com.vocalink.crossproduct.ui.facade.api.TransactionsFacade
import java.math.BigDecimal
import java.time.ZoneId
import java.time.ZonedDateTime
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class TransactionsControllerTest : ControllerTest() {

    @MockBean
    private lateinit var transactionsFacade: TransactionsFacade

    private companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val INVALID_DATE_FROM_EARLIER_THAN_DATE_LIMIT_BODY_REQUEST = """{
            "dateFrom": "2020-10-23T23:59:59Z"
        }"""

        const val INVALID_LIMIT_BODY_REQUEST = """{
            "limit": "0"
        }"""
        const val INVALID_SORT_BODY_REQUEST = """{
            "offset" : 0,
            "limit" : 20,
            "sort" : ["-wrong_param"]
        }"""
        const val INVALID_WILDCARD_IN_MIDDLE_BODY_REQUEST = """{
            "id": "BANK*YY"
        }"""

        const val INVALID_WILDCARD_SPECIAL_CHARACTERS_BODY_REQUEST = """{
            "id": "BANK()[]"
        }"""

        const val INVALID_CREDITOR_REGEX_BODY_REQUEST = """{
            "creditor": "374593847"
        }"""

        const val INVALID_DEBTOR_REGEX_BODY_REQUEST = """{
            "debtor": "AANKX"
        }"""

        const val INVALID_TOO_LONG_CREDITOR_REGEX_BODY_REQUEST = """{
            "creditor": "AABDJAIDUESKSIFJSBSK"
        }"""

        const val INVALID_REASON_CODE_WITH_NO_STATUS_DAYS_BODY_REQUEST = """{
            "reasonCode": "F01"
        }"""

        const val INVALID_REASON_CODE_WITH_ACCEPTED_STATUS_DAYS_BODY_REQUEST = """{
            "reasonCode": "F01",
            "status": "accepted"
        }"""

        const val VALID_SAME_SENDING_AND_RECEIVING_BIC_BODY_REQUEST = """{
            "cycleId" : "20190212004",
            "sendingBic": "AVANSESX",
            "receivingBic": "AVANSESX"
        }"""

        const val INVALID_BODY_PARAM_NAMES_REQUEST = """{
            "offset" : 0,
            "limit" : 20,
            "invalidName" : "xxxxx", 
            "cycleId" : "20190212004"
        }"""

        const val VALID_MIN_BODY_REQUEST = """{
            "offset" : 0,
            "limit" : 20,
            "cycleId" : "20190212004"
        }"""

        const val INVALID_CYCLE_ID_BODY_REQUEST = """{
            "limit": "1"
        }"""

        const val VALID_REJECTED_BODY_REQUEST = """{
            "offset" : 0,
            "limit" : 20,
            "status": "PRE-RJCT",
            "reasonCode": "F01",
            "cycleId" : "20190212004"
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
            "status": "PRE-RJCT",
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
            ],
            "cycleId": "20190212004"
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

        const val VALID_DETAILS_RESPONSE = """{
            "instructionId": "20210115SCADSE21B2191",
            "amount": 11112222.33,
            "currency": "SEK",
            "fileName": "P27ISTXSCADSE21201911320191113135321990NCTSEK_PACS0082192",
            "batchId": "P27ISTXBANKSESS",
            "receiver": {
                "entityName": "Meetoo",
                "entityBic": "MEEOSES1",
                "iban": "SE23 9999 9999 9999 9999 2193"
            },
            "settlementDate": "2021-01-14T15:02:14Z",
            "settlementCycleId": "20201209001",
            "createdAt": "2021-01-14T15:02:14Z",
            "status": "rejected",
            "reasonCode": "004",
            "messageType": "pacs.002",
            "sender": {
                "entityName": "Scandem",
                "entityBic": "SCADSE21",
                "iban": "SE23 9999 9999 9999 9999 2194",
                "fullName": "Vallery Valtez"
            }
        }"""
    }

    @Test
    fun `should return 200 with minimal body params and return valid response`() {
        val transaction = TransactionDto(
            "20210115CARNSES1B2199",
            ZonedDateTime.of(2021, 1, 14, 15, 2, 14, 0, ZoneId.of("UTC")),
            "CARNSES1",
            "NDEASSESS",
            "rocs.001",
            BigDecimal.valueOf(3334343.80),
            "accepted"
        )
        `when`(transactionsFacade.getPaginated(any(), any(), any()))
            .thenReturn(PageDto(1, listOf(transaction)))
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_MIN_BODY_REQUEST)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(VALID_PAGE_RESPONSE))

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(
            eventCaptor.allValues[0],
            eventCaptor.allValues[1],
            EventType.TRANSACTION_ENQUIRY
        )
    }

    @Test
    fun `should return 200 with full body request`() {
        `when`(transactionsFacade.getPaginated(any(), any(), any()))
            .thenReturn(PageDto(0, null))
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_BODY_REQUEST)
        )
            .andExpect(status().isOk)

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(
            eventCaptor.allValues[0],
            eventCaptor.allValues[1],
            EventType.TRANSACTION_ENQUIRY
        )
    }

    @Test
    fun `should ignore unrecognized param and read valid params`() {
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_BODY_PARAM_NAMES_REQUEST)
        )
            .andExpect(status().isOk)

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(
            eventCaptor.allValues[0], eventCaptor.allValues[1],
            EventType.TRANSACTION_ENQUIRY
        )
    }

    @Test
    fun `should fail with 400 when required cicle_id or date_from, date_to are not specified`() {
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_CYCLE_ID_BODY_REQUEST)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("CycleId either both dateFrom and dateTo must not be null")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should succeed with 200 when send_bic and recv_bic are with same values`() {
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_SAME_SENDING_AND_RECEIVING_BIC_BODY_REQUEST)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should fail with 400 when dateFrom is earlier than DAYS_LIMIT from today`() {
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_DATE_FROM_EARLIER_THAN_DATE_LIMIT_BODY_REQUEST)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("date_from can not be earlier than DAYS_LIMIT")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 when has reasonCode but missing status`() {
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_REASON_CODE_WITH_NO_STATUS_DAYS_BODY_REQUEST)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("Reason code should not be any of the rejected types")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 when has reasonCode but status is other than Rejected`() {
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_REASON_CODE_WITH_ACCEPTED_STATUS_DAYS_BODY_REQUEST)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("Reason code should not be any of the rejected types")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 when id wildcard search string has wildcard on middle of the word`() {
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_WILDCARD_IN_MIDDLE_BODY_REQUEST)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("wildcard '*' can not be in " +
                    "the middle and id should not contain special symbols beside '.' and '_'"))
            )

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 when id wildcard search string has other special characters than allowed by the regex`() {
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_WILDCARD_SPECIAL_CHARACTERS_BODY_REQUEST)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("wildcard '*' can not be in the" +
                    " middle and id should not contain special symbols beside '.' and '_'"))
            )

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 on invalid creditor regex`() {
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_CREDITOR_REGEX_BODY_REQUEST)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("Invalid creditor regex!")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 on invalid debtor regex`() {
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_DEBTOR_REGEX_BODY_REQUEST)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("Invalid debtor regex!")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 on invalid too long creditor regex`() {
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_TOO_LONG_CREDITOR_REGEX_BODY_REQUEST)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("Invalid creditor regex!")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should return 200 when has reason code and status is rejected`() {
        `when`(transactionsFacade.getPaginated(any(), any(), any()))
            .thenReturn(PageDto(0, null))
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_REJECTED_BODY_REQUEST)
        )
            .andExpect(status().isOk)

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(
            eventCaptor.allValues[0], eventCaptor.allValues[1],
            EventType.TRANSACTION_ENQUIRY
        )
    }

    @Test
    fun `should fail with 400 when limit is less than 1`() {
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_LIMIT_BODY_REQUEST)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("Limit should be equal or higher than 1")))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 on wrong sorting param`() {
        mockMvc.perform(
            post("/enquiry/transactions/searches")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(INVALID_SORT_BODY_REQUEST)
        )
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("Wrong sorting parameter")))
    }

    @Test
    fun `should return 200 with valid response on fetch with id`() {
        val id = "20210115SVEASES1B2215"
        val date = ZonedDateTime.of(2021, 1, 14, 15, 2, 14, 0, ZoneId.of("UTC"))

        val receiver = TransactionReceiverDetailsDto(
            "Meetoo", "MEEOSES1", "SE23 9999 9999 9999 9999 2193",
            "fullname", "creditorName", "creditorBic"
        )
        val sender = TransactionSenderDetailsDto(
            "Scandem", "SCADSE21", "SE23 9999 9999 9999 9999 2194",
            "Vallery Valtez", "debtorName", "debtorBic"
        )
        val detailsDto = TransactionDetailsDto(
            "20210115SCADSE21B2191",
            BigDecimal.valueOf(11112222.33), "SEK",
            "P27ISTXSCADSE21201911320191113135321990NCTSEK_PACS0082192",
            "P27ISTXBANKSESS", date,
            "20201209001", date,
            "rejected",
            "004",
            "pacs.002",
            sender, receiver

        )

        `when`(transactionsFacade.getDetailsById(any(), any(), any()))
            .thenReturn(detailsDto)
        mockMvc.perform(
            get("/enquiry/transactions/${id}")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(VALID_DETAILS_RESPONSE))

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(
            eventCaptor.allValues[0], eventCaptor.allValues[1],
            EventType.TRANSACTION_DETAILS
        )
    }
}
