package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.audit.AuditDetails
import com.vocalink.crossproduct.domain.audit.AuditDetailsRepository
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.domain.participant.ParticipantType.FUNDED
import com.vocalink.crossproduct.domain.permission.UIPermission
import com.vocalink.crossproduct.domain.permission.UIPermissionRepository
import com.vocalink.crossproduct.domain.role.Role.Function
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto
import com.vocalink.crossproduct.ui.dto.permission.CurrentUserInfoDto
import com.vocalink.crossproduct.ui.dto.permission.UserInfoDto
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

class UserFacadeImplTest {

    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)
    private val participantRepository = mock(ParticipantRepository::class.java)!!
    private val uiPermissionRepository = mock(UIPermissionRepository::class.java)!!
    private val auditDetailsRepository = mock(AuditDetailsRepository::class.java)!!
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
        `when`(repositoryFactory.getAuditDetailsRepository(anyString()))
                .thenReturn(auditDetailsRepository)
        `when`(presenterFactory.getPresenter(UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository and get user permissions`() {
        val userId = "12a511"
        val userFirstName = "Peter"
        val userLastName = "Brooks"
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

        val userInfoDto = UserInfoDto(userId, "$userFirstName $userLastName")

        val uiPermission = UIPermission(permissionId, permissionValue)

        val auditDetails = AuditDetails.builder()
                .username(userId)
                .firstName(userFirstName)
                .lastName(userLastName)
                .build()

        val userPermissionDto = CurrentUserInfoDto(listOf(permissionValue), participantDto, userInfoDto)

        `when`(participantRepository.findById(participantId)).thenReturn(participant)

        `when`(uiPermissionRepository.findByRolesAndParticipantType(listOf(Function.valueOf(role)), FUNDED))
                .thenReturn(listOf(uiPermission))

        `when`(auditDetailsRepository.getAuditDetailsByUsername(userId))
                .thenReturn(auditDetails)

        `when`(uiPresenter.presentCurrentUserInfo(participant, listOf(uiPermission), auditDetails))
                .thenReturn(userPermissionDto)

        val result = userPermissionFacadeImpl.getCurrentUserInfo(CONTEXT, UI, userId, participantId, listOf(role))

        verify(participantRepository).findById(any())
        verify(uiPermissionRepository).findByRolesAndParticipantType(any(), any())
        verify(auditDetailsRepository).getAuditDetailsByUsername(any())
        verify(uiPresenter).presentCurrentUserInfo(any(), any(), any())

        assertThat(result).isNotNull
    }
}