package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchParams
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime


class DefaultDtoConfigurationTest {

    @Test
    fun `should load default properties for alert request`() {
        val alertSearchParams = AlertSearchParams()

        val default30Days = LocalDateTime.now().minusDays(getDefault(DtoProperties.DAYS_LIMIT).toLong())
        assertThat(alertSearchParams.limit).isEqualTo(20)
        assertThat(alertSearchParams.offset).isEqualTo(0)
        assertThat(alertSearchParams.dateFrom).isEqualTo(default30Days)
    }

    @Test
    fun `should load default properties for file request`() {
        val fileSearchRequest = FileEnquirySearchRequest()

        val default30Days = LocalDate.now().minusDays(getDefault(DtoProperties.DAYS_LIMIT).toLong())
        assertThat(fileSearchRequest.limit).isEqualTo(20)
        assertThat(fileSearchRequest.offset).isEqualTo(0)
        assertThat(fileSearchRequest.dateFrom).isEqualTo(default30Days)
    }

    @Test
    fun `should load default properties for batch request`() {
        val fileSearchRequest = BatchEnquirySearchRequest()

        val default30Days = LocalDate.now().minusDays(getDefault(DtoProperties.DAYS_LIMIT).toLong())
        assertThat(fileSearchRequest.limit).isEqualTo(20)
        assertThat(fileSearchRequest.offset).isEqualTo(0)
        assertThat(fileSearchRequest.dateFrom).isEqualTo(default30Days)
    }
}