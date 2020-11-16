package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class DefaultDtoConfigurationTest {

    @Test
    fun `should load properties`() {
        val alertSearchRequest = AlertSearchRequest(1, null, null, null, null, null, null, null, null, null)

        assertThat(alertSearchRequest.order).isEqualTo("DESC")
    }
}