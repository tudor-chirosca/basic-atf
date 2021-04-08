package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.audit.UserActivity
import com.vocalink.crossproduct.domain.audit.UserActivityRepository
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.domain.participant.ParticipantType.FUNDED
import com.vocalink.crossproduct.domain.permission.UIPermission
import com.vocalink.crossproduct.domain.permission.UIPermissionRepository
import com.vocalink.crossproduct.domain.role.Role.Function
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto
import com.vocalink.crossproduct.ui.dto.permission.UserInfoDto
import com.vocalink.crossproduct.ui.dto.permission.CurrentUserInfoDto
import com.vocalink.crossproduct.ui.presenter.ClientType.UI
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.*

class UserFacadeImplTest {

    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)
    private val participantRepository = mock(ParticipantRepository::class.java)!!
    private val uiPermissionRepository = mock(UIPermissionRepository::class.java)!!
    private val userActivityRepository = mock(UserActivityRepository::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!

    private val userPermissionFacadeImpl = UserFacadeImpl(
            presenterFactory,
            repositoryFactory
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getParticipantRepository(anyString()))
                .thenReturn(participantRepository)
        `when`(repositoryFactory.uiPermissionRepository)
                .thenReturn(uiPermissionRepository)
        `when`(repositoryFactory.getUserActivityRepository(anyString()))
                .thenReturn(userActivityRepository)
        `when`(presenterFactory.getPresenter(UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository and get user permissions`() {
        val userId = "cd4d219d-3daf-40f2-becc-6f08e6edc477"
        val userName = "John Doe"
        val userDescription = "Simple User"
        val participantId = "HANDSESS"
        val role = "MANAGEMENT"
        val permissionId = "6c93e343-9644-4b49-bb99-f6c768f043aa"
        val permissionValue = "read.alerts-dashboard"

        val participant = Participant.builder()
                .id(participantId)
                .participantType(FUNDED)
                .build()

        val participantDto = ParticipantDto.builder()
                .id(participantId)
                .participantType(FUNDED)
                .build()

        val userInfoDto = UserInfoDto(UUID.fromString(userId), userName)

        val uiPermission = UIPermission(permissionId, permissionValue)

        val userActivity = UserActivity(UUID.fromString(userId), userName)

        val userPermissionDto = CurrentUserInfoDto(listOf(permissionValue), participantDto, userInfoDto)

        `when`(participantRepository.findById(participantId)).thenReturn(participant)

        `when`(uiPermissionRepository.findByRolesAndParticipantType(listOf(Function.valueOf(role)), FUNDED))
                .thenReturn(listOf(uiPermission))

        `when`(userActivityRepository.getActivitiesById(UUID.fromString(userId)))
                .thenReturn(userActivity)

        `when`(uiPresenter.presentCurrentUserInfo(participant, listOf(uiPermission), userActivity))
                .thenReturn(userPermissionDto)

        val result = userPermissionFacadeImpl.getCurrentUserInfo(CONTEXT, UI, userId, participantId, role)

        verify(participantRepository).findById(any())
        verify(uiPermissionRepository).findByRolesAndParticipantType(any(), any())
        verify(userActivityRepository).getActivitiesById(any())
        verify(uiPresenter).presentCurrentUserInfo(any(), any(), any())

        assertThat(result).isNotNull
    }
}