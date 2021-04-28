package com.vocalink.crossproduct

import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("com.vocalink.crossproduct")
@AutoConfigureDataJpa
open class TestConfig {
}
