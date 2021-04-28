package com.vocalink.crossproduct.infrastructure.config

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ApplicationEvent
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.event.ContextClosedEvent

open class WireMockInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(context: ConfigurableApplicationContext) {
        val wireMockServer = WireMockServer(54001)
        wireMockServer.start()
        context.beanFactory.registerSingleton("wireMockServer", wireMockServer)
        context.addApplicationListener { applicationEvent: ApplicationEvent? ->
            if (applicationEvent is ContextClosedEvent) {
                wireMockServer.stop()
            }
        }
    }
}
