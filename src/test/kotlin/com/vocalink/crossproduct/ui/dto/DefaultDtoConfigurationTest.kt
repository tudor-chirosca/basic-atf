package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class DefaultDtoConfigurationTest {

    @Test
    fun `should load default properties for alert request`() {
        val alertSearchRequest = AlertSearchRequest(0, null, null, null, null, null, null, null, null, null)

        assertThat(alertSearchRequest.order).isEqualTo("DESC")
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