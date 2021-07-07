package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants.CLIENT_TYPE
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.participant.ParticipantStatus.*
import com.vocalink.crossproduct.domain.participant.ParticipantType.*
import com.vocalink.crossproduct.domain.participant.SuspensionLevel.*
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto
import com.vocalink.crossproduct.ui.dto.permission.UserInfoDto
import com.vocalink.crossproduct.ui.dto.permission.CurrentUserInfoDto
import com.vocalink.crossproduct.ui.facade.api.UserFacade
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.charset.Charset
import java.time.ZoneId
import java.time.ZonedDateTime
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString

@WebMvcTest(UserController::class)
@ContextConfiguration(classes = [TestConfig::class])
class UserControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var userFacade: UserFacade

    private val UTF8_CONTENT_TYPE: MediaType = MediaType(MediaType.APPLICATION_JSON.type,
            MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

    private companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"
        const val X_USER_ID_HEADER = "x-user-id"
        const val X_PARTICIPANT_ID_HEADER = "x-participant-id"
        const val X_ROLES_HEADER = "x-roles"

        const val VALID_USER_PERMISSIONS_RESPONSE = """
            {
            "permissions": [
              "read.alerts-dashboard"
            ],
            "participation": {
              "bic": "HANDSESS",
              "fundingBic": "NDEASESSXXX",
              "id": "HANDSESS",
              "name": "Svenska Handelsbanken",
              "status": "SUSPENDED",
              "suspendedTime": "2020-09-15T13:00:00Z",
              "participantType": "FUNDED",
              "suspensionLevel": "SCHEME"
            },
            "user": {
              "userId": "12a511",
              "name": "John Doe"
            }
            }
        """
    }

    @Test
    fun `should return 200 with valid headers and return user details and permissions`() {
        val userId = "12a511"
        val userName = "John Doe"
        val roles = listOf("MANAGEMENT")
        val participantId = "HANDSESS"
        val fundingBic = "NDEASESSXXX"
        val participantName = "Svenska Handelsbanken"
        val permission = "read.alerts-dashboard"
        val dateTime = ZonedDateTime.of(2020, 9, 15, 13, 0, 0, 0, ZoneId.of("UTC"))
        val participant = ParticipantDto.builder()
            .id(participantId)
            .bic(participantId)
            .name(participantName)
            .fundingBic(fundingBic)
            .status(SUSPENDED)
            .suspendedTime(dateTime)
            .participantType(FUNDED)
            .suspensionLevel(SCHEME)
            .build()

        `when`(userFacade.getCurrentUserInfo(anyString(), any(), anyString(), anyString(), anyList()))
                .thenReturn(CurrentUserInfoDto(listOf(permission), participant,
                        UserInfoDto(userId, userName)))

        mockMvc.perform(get("/me")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
                .header(X_USER_ID_HEADER, userId)
                .header(X_PARTICIPANT_ID_HEADER, participantId)
                .header(X_ROLES_HEADER, roles))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_USER_PERMISSIONS_RESPONSE))
    }

    @Test
    fun `should return 204 with valid headers and return no content`() {
        val userId = "12a511"
        val participantId = "HANDSESS"
        val roles = listOf("MANAGEMENT")

        mockMvc.perform(get("/logout")
            .contentType(UTF8_CONTENT_TYPE)
            .header(CONTEXT_HEADER, CONTEXT)
            .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
            .header(X_USER_ID_HEADER, userId)
            .header(X_PARTICIPANT_ID_HEADER, participantId)
            .header(X_ROLES_HEADER, roles))
            .andExpect(status().isNoContent)
    }
}
