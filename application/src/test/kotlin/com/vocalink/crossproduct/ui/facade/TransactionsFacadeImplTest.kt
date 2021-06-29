package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.account.Account
import com.vocalink.crossproduct.domain.account.AccountRepository
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.domain.transaction.Transaction
import com.vocalink.crossproduct.domain.transaction.TransactionRepository
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDetailsDto
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest
import com.vocalink.crossproduct.ui.presenter.ClientType
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
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertNotNull

class TransactionsFacadeImplTest {

    private val transactionRepository = mock(TransactionRepository::class.java)!!
    private val accountRepository = mock(AccountRepository::class.java)!!
    private val participantRepository = mock(ParticipantRepository::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)

    private val transactionsServiceFacadeImpl = TransactionsFacadeImpl(
            presenterFactory,
            repositoryFactory,
            TestConstants.FIXED_CLOCK,
            "CET"
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getTransactionRepository(anyString()))
                .thenReturn(transactionRepository)
        `when`(repositoryFactory.getAccountRepository(anyString()))
                .thenReturn(accountRepository)
        `when`(repositoryFactory.getParticipantRepository(anyString()))
                .thenReturn(participantRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository on get transactions`() {
        val page = Page(
                1, listOf(
                Transaction(
                        null, null, null, null,
                        null, null, null, null,
                        null, null, null, null,
                        null, null, null, null,
                        null, null, null, null,
                        null, null
                )
        )
        )
        val pageDto = PageDto<TransactionDto>(
                1, listOf(
                TransactionDto(
                        null, null, null, null,
                        null, null, null
                )
        )
        )
        val pseudoUTC = ZonedDateTime.of(2021, 6, 20, 10, 10, 10, 0, ZoneId.of("CET"))
        val request = TransactionEnquirySearchRequest(
                0, 20, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, null, pseudoUTC, null, null
        )

        `when`(transactionRepository.findPaginated(any()))
                .thenReturn(page)

        `when`(uiPresenter.presentTransactions(any(), any()))
                .thenReturn(pageDto)

        val result = transactionsServiceFacadeImpl.getPaginated(
                TestConstants.CONTEXT,
                ClientType.UI,
                request
        )

        verify(transactionRepository).findPaginated(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentTransactions(any(), any())

        assertNotNull(result)
    }

    @Test
    fun `should invoke presenter and repository on get transaction details`() {
        val transaction = Transaction(
                null, null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, null, null, null, null,
                null, null, null, null
        )
        val batchDetailsDto = TransactionDetailsDto(
                null, null, null, null, null, null,
                null, null, null, null, null,
                null, null
        )
        val account = Account(null, null, null)

        val participant = Participant(
                null, null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, null
        )

        `when`(transactionRepository.findById(any()))
                .thenReturn(transaction)

        `when`(accountRepository.findByPartyCode(any()))
                .thenReturn(account)

        `when`(participantRepository.findById(any()))
                .thenReturn(participant)

        `when`(uiPresenter.presentTransactionDetails(any()))
                .thenReturn(batchDetailsDto)

        val result =
                transactionsServiceFacadeImpl.getDetailsById(TestConstants.CONTEXT, ClientType.UI, "")

        verify(transactionRepository).findById(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentTransactionDetails(any())

        assertNotNull(result)
    }

    @Test
    fun `should convert to utc from pseudo utc using value from properties`() {
        val pseudoUTC = ZonedDateTime.of(2021, 6, 20, 10, 10, 10, 0, ZoneId.of("UTC"))

        val UTC = transactionsServiceFacadeImpl.convertToUTC(pseudoUTC)

        assertThat(pseudoUTC).isAfter(UTC)
    }
}