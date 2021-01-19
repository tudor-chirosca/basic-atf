package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.transaction.Transaction
import com.vocalink.crossproduct.domain.transaction.TransactionRepository
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDetailsDto
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import kotlin.test.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class TransactionsFacadeImplTest {

    private val transactionRepository = Mockito.mock(TransactionRepository::class.java)!!
    private val presenterFactory = Mockito.mock(PresenterFactory::class.java)!!
    private val uiPresenter = Mockito.mock(UIPresenter::class.java)!!
    private val repositoryFactory = Mockito.mock(RepositoryFactory::class.java)

    private val transactionsServiceFacadeImpl = TransactionsFacadeImpl(
            presenterFactory,
            repositoryFactory
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getTransactionRepository(anyString()))
                .thenReturn(transactionRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository on get transactions`() {
        val page = Page<Transaction>(1, listOf(
                Transaction(null, null, null, null,
                        null, null, null, null,
                        null, null, null, null,
                        null, null, null, null,
                        null, null
                )
        ))
        val pageDto = PageDto<TransactionDto>(1, listOf(
                TransactionDto( null, null, null, null,
                        null, null
                )
        ))
        val request = TransactionEnquirySearchRequest(
                0, 20, null, null, null, null,
                "sending", null, null, null, null,
                null, null, null,  null, null,
                null, null
        )

        `when`(transactionRepository.findPaginated(any()))
                .thenReturn(page)

        `when`(uiPresenter.presentTransactions(any()))
                .thenReturn(pageDto)

        val result = transactionsServiceFacadeImpl.getPaginated(TestConstants.CONTEXT, ClientType.UI, request)

        verify(transactionRepository).findPaginated(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentTransactions(any())

        assertNotNull(result)
    }

    @Test
    fun `should invoke presenter and repository on get transaction details`() {
        val transaction = Transaction(
                null,null,null,null,null,
                null,null,null,null,
                null,null,null,null,null,
                null,null,null,null
        )
        val batchDetailsDto = TransactionDetailsDto(
                null,null,null,null,null,null,
                null,null,null,null,null,
                null,null
        )

        `when`(transactionRepository.findById(any()))
                .thenReturn(transaction)

        `when`(uiPresenter.presentTransactionDetails(any()))
                .thenReturn(batchDetailsDto)

        val result = transactionsServiceFacadeImpl.getDetailsById(TestConstants.CONTEXT, ClientType.UI, "")

        verify(transactionRepository).findById(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentTransactionDetails(any())

        assertNotNull(result)
    }
}