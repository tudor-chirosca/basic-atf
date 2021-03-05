package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
import com.vocalink.crossproduct.ui.dto.report.ReportsSearchRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime


class DefaultDtoConfigurationTest {

    @Test
    fun `should load default properties for alert request`() {
        val alertSearchParams = AlertSearchRequest()

        val default30Days =
            ZonedDateTime.now().minusDays(getDefault(DtoProperties.DAYS_LIMIT).toLong())
        assertThat(alertSearchParams.limit).isEqualTo(20)
        assertThat(alertSearchParams.offset).isEqualTo(0)
        assertThat(alertSearchParams.dateFrom.toLocalDate()).isEqualTo(default30Days.toLocalDate())
    }

    @Test
    fun `should load default properties for file request`() {
        val fileSearchRequest = FileEnquirySearchRequest()

        assertThat(fileSearchRequest.limit).isEqualTo(20)
        assertThat(fileSearchRequest.offset).isEqualTo(0)
    }

    @Test
    fun `should load default properties for batch request`() {
        val fileSearchRequest = BatchEnquirySearchRequest()

        assertThat(fileSearchRequest.limit).isEqualTo(20)
        assertThat(fileSearchRequest.offset).isEqualTo(0)
    }

    @Test
    fun `should load default properties for reports request`() {
        val reportsSearchRequest = ReportsSearchRequest()

        val default30Days = ZonedDateTime.now().minusDays(getDefault(DtoProperties.DAYS_LIMIT).toLong())
        assertThat(reportsSearchRequest.limit).isEqualTo(20)
        assertThat(reportsSearchRequest.offset).isEqualTo(0)
        assertThat(reportsSearchRequest.dateFrom.toLocalDate()).isEqualTo(default30Days.toLocalDate())
    }
}
