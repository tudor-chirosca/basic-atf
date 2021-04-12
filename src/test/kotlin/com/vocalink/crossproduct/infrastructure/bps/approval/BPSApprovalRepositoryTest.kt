package com.vocalink.crossproduct.infrastructure.bps.approval

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.vocalink.crossproduct.domain.approval.ApprovalChangeCriteria
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType.BATCH_CANCELLATION
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType.CONFIG_CHANGE
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria
import com.vocalink.crossproduct.domain.approval.ApprovalStatus.PENDING
import com.vocalink.crossproduct.domain.approval.ApprovalStatus.REJECTED
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@BPSTestConfiguration
@Import(BPSApprovalRepository::class)
class BPSApprovalRepositoryTest @Autowired constructor(var approvalRepository: BPSApprovalRepository,
                                                       var mockServer: WireMockServer){

    companion object {
        const val VALID_BATCH_CANCELLATION_REQUEST = """ {
           "requestType" : "BATCHCANCELLATION",
           "requestedChange" : {
                "batchId": "some_batch_id",
                "status": "some_new_status"
              },
           "notes" : "notes"
        }"""

        const val VALID_APPROVAL_DETAILS_REQUEST: String = """
            {
                "approvalId": "10000004"
            }"""

        const val VALID_APPROVALS_REQUEST: String = """
            {
                "approvalId": "10000000",
                "fromDate": "2021-02-01T10:10:00Z",
                "toDate": "2020-02-20T12:10:00Z",
                "schemeParticipantIdentifiers": ["P27"],
                "requestTypes": ["BATCHCANCELLATION"],
                "requestedBy": ["12a514"],
                "statuses": ["WAITING-FORAPPROVAL"],
                "sortingOrder": [
                    {
                        "sortOrderBy": "status",
                        "sortOrder": "ASC"
                    }]
            }"""

        const val VALID_APPROVAL_REQUEST_BY_RESPONSE: String = """
               [
                   {
                        "schemeParticipantIdentifier": "P27",
                        "firstName": "John",
                        "userId": "E109341",
                        "lastName": "Douglas"
                   }
               ]
            """

        const val VALID_APPROVAL_REQUEST_TYPE_RESPONSE: String = """
                [
                    "BATCHCANCELLATION",
                    "PARTICIPANTCONF",
                    "PARTICIPANT_SUSPEND",
                    "PARTICIPANT_UNSUSPEND"
                ]
            """

        const val VALID_APPROVAL_DETAILS_RESPONSE: String = """
            {
                "approvalId": "10000004",
                "requestType": "PARTICIPANTCONF",
                "participantIds": ["ELLFSESP"],
                "date": "2021-02-03T14:55:00Z",
                "requestedBy": {
                       "schemeParticipantIdentifier": "P27-SEK",
                       "firstName": "John",
                       "userId": "12a514",
                       "lastName": "Doe"
                },
                "status": "REJECTED",
                "requestComment": "This is the reason...",
                "rejectedBy": {
                       "schemeParticipantIdentifier": "P27-SEK",
                       "firstName": "John",
                       "userId": "12a514",
                       "lastName": "Doe"
                    },
                "originalData": {
                    "data": "some original data"
                },
                "requestedChange": {
                    "status": "suspended"
                },
                "oldData": "hashed data",
                "newData": "hashed data",
                "notes": "Notes"
            }"""

        const val VALID_APPROVALS_RESPONSE: String = """
            {
                "totalResults": 1,
                "items": [
                    {
                        "approvalId": "10000000",
                        "requestType": "BATCHCANCELLATION",
                        "participantIds": ["P27"],
                        "date": "2021-02-03T14:55:00Z",
                        "requestedBy": {
                            "schemeParticipantIdentifier": "P27-SEK",
                            "firstName": "John",
                            "userId": "12a514",
                            "lastName": "Doe"
                        },
                        "status": "WAITING-FORAPPROVAL",
                        "requestComment": "This is the reason...",
                        "originalData": {
                            "data": "some original data"
                        },
                        "requestedChange": {
                            "status": "suspended"
                        },
                        "oldData": "hashed data",
                        "newData": "hashed data",
                        "notes": "Notes"
                }]
            }"""
    }

    @Test
    fun `should return approval details by id`() {
        val date = ZonedDateTime.of(LocalDateTime.of(2021, 2, 3, 14, 55,0),  ZoneId.of("UTC"))
        mockServer.stubFor(
                post(urlEqualTo("/approvals/P27-SEK/read"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_APPROVAL_DETAILS_RESPONSE))
                        .withRequestBody(equalToJson(VALID_APPROVAL_DETAILS_REQUEST)))

        val result = approvalRepository.findByJobId("10000004")

        assertThat(result.approvalId).isEqualTo("10000004")
        assertThat(result.requestType).isEqualTo(CONFIG_CHANGE)
        assertThat(result.participantIds[0]).isEqualTo("ELLFSESP")
        assertThat(result.date).isEqualTo(date)
        assertThat(result.requestedBy.firstName).isEqualTo("John")
        assertThat(result.requestedBy.lastName).isEqualTo("Doe")
        assertThat(result.status).isEqualTo(REJECTED)
        assertThat(result.requestComment).isEqualTo("This is the reason...")
        assertThat(result.requestedChange["status"]).isEqualTo("suspended")
    }

    @Test
    fun `should return approvals list`() {
        mockServer.stubFor(
                post(urlPathEqualTo("/approvals/P27-SEK/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_APPROVALS_RESPONSE))
                        .withQueryParam("offset", WireMock.equalTo("0"))
                        .withQueryParam("pageSize", WireMock.equalTo("1")))

        val result = approvalRepository.findPaginated(ApprovalSearchCriteria(0, 1, null, null,
                null, null, null, null, null, emptyList()))


        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(1)
        assertThat(result.items).isNotEmpty

        val item = result.items[0]
        assertThat(item.approvalId).isEqualTo("10000000")
        assertThat(item.requestType).isEqualTo(BATCH_CANCELLATION)
        assertThat(item.participantIds[0]).isEqualTo("P27")
        assertThat(item.requestedBy.firstName).isEqualTo("John")
        assertThat(item.requestedBy.lastName).isEqualTo("Doe")
        assertThat(item.status).isEqualTo(PENDING)
        assertThat(item.requestComment).isEqualTo("This is the reason...")
        assertThat(item.requestedChange["status"]).isEqualTo("suspended")
    }

    @Test
    fun `should return approvals list with all parameters in search criteria`() {
        val fromDate = ZonedDateTime.of(2021, 2, 1, 10, 10, 0, 0, ZoneId.of("UTC"))
        val toDate = ZonedDateTime.of(2020, 2, 20, 12, 10, 0, 0, ZoneId.of("UTC"))
        mockServer.stubFor(
                post(urlPathEqualTo("/approvals/P27-SEK/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_APPROVALS_RESPONSE))
                        .withRequestBody(equalToJson(VALID_APPROVALS_REQUEST))
                        .withQueryParam("offset", WireMock.equalTo("0"))
                        .withQueryParam("pageSize", WireMock.equalTo("1")))

        val result = approvalRepository.findPaginated(ApprovalSearchCriteria(0, 1, "10000000",
                fromDate, toDate, listOf("P27"), listOf(BATCH_CANCELLATION),
                listOf("12a514"), listOf(PENDING), listOf("status")))


        assertThat(result).isNotNull
        assertThat(result.totalResults).isEqualTo(1)
        assertThat(result.items).isNotEmpty

        val item = result.items[0]
        assertThat(item.approvalId).isEqualTo("10000000")
        assertThat(item.requestType).isEqualTo(BATCH_CANCELLATION)
        assertThat(item.participantIds[0]).isEqualTo("P27")
        assertThat(item.requestedBy.firstName).isEqualTo("John")
        assertThat(item.requestedBy.lastName).isEqualTo("Doe")
        assertThat(item.status).isEqualTo(PENDING)
        assertThat(item.requestComment).isEqualTo("This is the reason...")
        assertThat(item.requestedChange["status"]).isEqualTo("suspended")
    }

    @Test
    fun `should return approval on create approval request`() {
        val date = ZonedDateTime.of(LocalDateTime.of(2021, 2, 3, 14, 55,0),  ZoneId.of("UTC"))
        mockServer.stubFor(
                post(urlEqualTo("/approvals/P27-SEK"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_APPROVAL_DETAILS_RESPONSE))
                        .withRequestBody(equalToJson(VALID_BATCH_CANCELLATION_REQUEST)))
        val criteria = ApprovalChangeCriteria(
                BATCH_CANCELLATION,
                mapOf("batchId" to "some_batch_id",
                        "status" to "some_new_status"),
                "notes"
        )
        val result = approvalRepository.requestApproval(criteria)

        assertThat(result.approvalId).isEqualTo("10000004")
        assertThat(result.requestType).isEqualTo(CONFIG_CHANGE)
        assertThat(result.participantIds[0]).isEqualTo("ELLFSESP")
        assertThat(result.date).isEqualTo(date)
        assertThat(result.requestedBy.firstName).isEqualTo("John")
        assertThat(result.requestedBy.lastName).isEqualTo("Doe")
        assertThat(result.status).isEqualTo(REJECTED)
        assertThat(result.requestComment).isEqualTo("This is the reason...")
        assertThat(result.requestedChange["status"]).isEqualTo("suspended")
    }

    @Test
    fun `should return approval request type list`() {
        mockServer.stubFor(
                post(urlEqualTo("/reference/approvals/requestTypes/P27-SEK/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_APPROVAL_REQUEST_TYPE_RESPONSE)))

        val result = approvalRepository.findApprovalRequestTypes()

        assertThat(result.size).isEqualTo(4)
        assertThat(result[0]).isEqualTo(ApprovalRequestType.BATCH_CANCELLATION)
        assertThat(result[1]).isEqualTo(ApprovalRequestType.CONFIG_CHANGE)
        assertThat(result[2]).isEqualTo(ApprovalRequestType.PARTICIPANT_SUSPEND)
        assertThat(result[3]).isEqualTo(ApprovalRequestType.PARTICIPANT_UNSUSPEND)
    }

    @Test
    fun `should return request by users reference list`() {
        mockServer.stubFor(
                post(urlEqualTo("/reference/approvals/requestedBy/P27-SEK/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_APPROVAL_REQUEST_BY_RESPONSE)))

        val result = approvalRepository.findRequestedDetails()

        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].participantId).isEqualTo("P27")
        assertThat(result[0].firstName).isEqualTo("John")
        assertThat(result[0].lastName).isEqualTo("Douglas")
        assertThat(result[0].userId).isEqualTo("E109341")
    }
}