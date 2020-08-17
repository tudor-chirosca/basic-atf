package com.vocalink.crossproduct


import com.vocalink.crossproduct.domain.CycleRepository
import com.vocalink.crossproduct.domain.ParticipantRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import javax.annotation.PostConstruct

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(AcceptanceTest.AcceptanceTestConfiguration::class)
open class AcceptanceTest {

    @LocalServerPort
    private var port: Int = 0
    private val rootUrl by lazy { "http://localhost:$port" }
    lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun setUp(context: ApplicationContext) {
        webTestClient = WebTestClient.bindToServer().baseUrl(rootUrl).build()
    }

    @PostConstruct
    fun initTestApp() {
        TestApp.rootUrl = rootUrl
    }

    object TestApp {
        lateinit var rootUrl: String
        val cycleRepository = Mockito.mock(CycleRepository::class.java)!!
        val participantRepository = Mockito.mock(ParticipantRepository::class.java)!!
    }

    @TestConfiguration
    open class AcceptanceTestConfiguration {
        @Bean
        @Primary
        open fun mockCycleRepo() = TestApp.cycleRepository

        @Bean
        @Primary
        open fun mockParticipantsRepo() = TestApp.participantRepository
    }

}
