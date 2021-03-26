package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.domain.audit.AuditDetails
import com.vocalink.crossproduct.domain.audit.AuditDetailsRepository
import com.vocalink.crossproduct.infrastructure.jpa.activities.UserActivity
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*

class AuditFacadeImplTest {

    private val presenterFactory = mock(PresenterFactory::class.java)
    private val repositoryFactory = mock(RepositoryFactory::class.java)
    private val auditRepositoryFactory = mock(AuditDetailsRepository::class.java)

    private val uiPresenter = UIPresenter()

    private var auditFacadeImpl = AuditFacadeImpl(repositoryFactory, presenterFactory)

    companion object {
        private const val PRODUCT = "BPS"
        private const val PARTICIPANT_ID = "PART_ID"
        private const val USERNAME = "UserName"
        private var CLIENT_TYPE = ClientType.UI

        private var details = listOf(AuditDetails.builder()
                .id(UUID.randomUUID())
                .activityId(UserActivity.builder().id(UUID.randomUUID()).build())
                .approvalRequestId("1")
                .correlationId("corId")
                .channel("BPS")
                .username(USERNAME)
                .userActivityString("activityString")
                .approvalRequestId("1")
                .requestOrResponseEnum("blah")
                .requestUrl("reqUrl")
                .contents("contentJson")
                .userId(UUID.randomUUID().toString())
                .firstName("John")
                .lastName("Snow")
                .participantId(PARTICIPANT_ID)
                .build()
        )
    }

    @Test
    fun `should get user details`() {
        `when`(repositoryFactory.getAuditDetailsRepository(PRODUCT))
                .thenReturn(auditRepositoryFactory)
        `when`(auditRepositoryFactory.getAuditDetailsById(PARTICIPANT_ID))
                .thenReturn(details)

        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)

        val userDetails = auditFacadeImpl.getUserDetails(PRODUCT, CLIENT_TYPE, PARTICIPANT_ID)

        assertThat(USERNAME).isEqualTo(userDetails[0].userName)
    }
}
