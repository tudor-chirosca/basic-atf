package com.vocalink.crossproduct.infrastructure.adapter

import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory
import com.vocalink.crossproduct.shared.positions.CPIntraDayPositionGross
import com.vocalink.crossproduct.shared.positions.PositionClient
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IntraDayPositionGrossAdapterTest {

    private val clientFactory: ClientFactory = mock(ClientFactory::class.java)
    private val positionClient: PositionClient = mock(PositionClient::class.java)
    private val intraDayPositionGrossAdapter = IntraDayPositionGrossAdapter(clientFactory)
    private val cpIntraDayPositionGross = listOf(
            CPIntraDayPositionGross
                    .builder()
                    .participantId("HANDSESS")
                    .debitCap(1100.0.toBigDecimal())
                    .debitPosition(1010.0.toBigDecimal())
                    .build(),
            CPIntraDayPositionGross
                    .builder()
                    .participantId("ESSESESS")
                    .debitCap(1001.0.toBigDecimal())
                    .debitPosition(1000.0.toBigDecimal())
                    .build()
    )

    @Test
    fun `should find intra day gross by participant ids`() {
        val participantIds = listOf("HANDSESS", "ESSESESS")

        `when`(clientFactory.getPositionClient(CONTEXT)).thenReturn(positionClient)
        `when`(positionClient.findIntraDayPositionsGrossByParticipantId(participantIds))
                .thenReturn(cpIntraDayPositionGross)
        val result = intraDayPositionGrossAdapter
                .findIntraDayPositionGrossByParticipantId(CONTEXT, participantIds)

        verify(clientFactory, atLeastOnce()).getPositionClient(CONTEXT)
        verify(positionClient, atLeastOnce()).findIntraDayPositionsGrossByParticipantId(participantIds)

        assertEquals(2, result.size)
        assertTrue(result[0] is IntraDayPositionGross)

        assertEquals("HANDSESS", result[0].participantId)
        assertEquals("ESSESESS", result[1].participantId)
    }
}