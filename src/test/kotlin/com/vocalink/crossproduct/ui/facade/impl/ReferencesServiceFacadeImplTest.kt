package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.repository.ParticipantRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
class ReferencesServiceFacadeImplTest {

    @Mock
    private lateinit var participantRepository: ParticipantRepository

    @InjectMocks
    private lateinit var referenceServiceFacadeImpl: ReferencesServiceFacadeImpl

    @Test
    fun `should get participants name and bic sorted by name`() {
        assertEquals("Svenska Handelsbanken", MockParticipants().participants[0].name)
        assertEquals("Nordea", MockParticipants().participants[1].name)
        assertEquals("SEB Bank", MockParticipants().participants[2].name)

        Mockito.`when`(participantRepository.findAll(TestConstants.CONTEXT))
                .thenReturn(MockParticipants().participants)
        val result = referenceServiceFacadeImpl.getParticipants(TestConstants.CONTEXT)

        assertEquals("Nordea", result[0].name)
        assertEquals("SEB Bank", result[1].name)
        assertEquals("Svenska Handelsbanken", result[2].name)
    }
}