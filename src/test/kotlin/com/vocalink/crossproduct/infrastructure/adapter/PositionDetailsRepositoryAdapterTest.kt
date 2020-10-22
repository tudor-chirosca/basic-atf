package com.vocalink.crossproduct.infrastructure.adapter

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.positions.BPSPositionClient
import com.vocalink.crossproduct.domain.position.PositionDetails
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory
import com.vocalink.crossproduct.mocks.MockPositions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigInteger
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PositionDetailsRepositoryAdapterTest {

    private val clientFactory = Mockito.mock(ClientFactory::class.java)!!
    private var positionClient = Mockito.mock(BPSPositionClient::class.java)!!
    private var testingModule = PositionDetailsRepositoryAdapter(clientFactory)

    @Test
    fun `should find all positions`() {
        val participantId = "HANDSESS"
        val mocks = MockPositions()
        Mockito.`when`(clientFactory.getPositionClient(TestConstants.CONTEXT))
                .thenReturn(positionClient)
        Mockito.`when`(positionClient.findByParticipantId(participantId)).thenReturn(mocks.cpPositionDetails)

        val result = testingModule.findByParticipantId(TestConstants.CONTEXT, participantId)

        assertEquals(2, result.size)
        assertTrue(result[0] is PositionDetails)

        assertEquals(BigInteger.ONE, result[0].customerCreditTransfer.credit)
        assertEquals(BigInteger.TEN, result[0].customerCreditTransfer.debit)
        assertEquals(BigInteger.valueOf(9), result[0].customerCreditTransfer.netPosition)

        assertEquals(BigInteger.TEN, result[0].paymentReturn.credit)
        assertEquals(BigInteger.TEN, result[0].paymentReturn.debit)
        assertEquals(BigInteger.ZERO, result[0].paymentReturn.netPosition)

        assertEquals(BigInteger.TEN, result[1].customerCreditTransfer.credit)
        assertEquals(BigInteger.TEN, result[1].customerCreditTransfer.debit)
        assertEquals(BigInteger.ZERO, result[1].customerCreditTransfer.netPosition)

        assertEquals(BigInteger.ONE, result[1].paymentReturn.credit)
        assertEquals(BigInteger.TEN, result[1].paymentReturn.debit)
        assertEquals(BigInteger.valueOf(9), result[1].paymentReturn.netPosition)
    }
}