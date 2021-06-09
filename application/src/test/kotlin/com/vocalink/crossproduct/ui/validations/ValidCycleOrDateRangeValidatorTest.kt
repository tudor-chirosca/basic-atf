package com.vocalink.crossproduct.ui.validations

import com.vocalink.crossproduct.TestConstants.DATE_TIME
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
import java.util.Collections
import java.util.stream.Stream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ValidCycleOrDateRangeValidatorTest {

    companion object {
        val constraints = mock(ValidCycleOrDateRange::class.java)!!
        val validator = ValidCycleOrDateRangeValidator()

        @JvmStatic
        fun getData() = Stream.of(
            Arguments.of(DATE_TIME, DATE_TIME, null, true),
            Arguments.of(null, null, "20210607_001", true),
            Arguments.of(DATE_TIME, null, "20210607_001", true),
            Arguments.of(null, DATE_TIME, "20210607_001", true),
            Arguments.of(null, DATE_TIME, null, false),
            Arguments.of(DATE_TIME, null, null, false),
            Arguments.of(null, null, null, false)
        )
    }

    @ParameterizedTest(name = "dateFrom: \"{0}\", dateTo: \"{1}\", cycleIds: {2}, check result: {3}")
    @MethodSource("getData")
    fun `should validate FileEnquirySearchRequest cycleId, dateFrom and dateTo`(
        dateFrom: String?, dateTo: String?,
        cycleId: String?, isValid: Boolean
    ) {
        `when`(constraints.cycleId).thenReturn("cycleId")
        `when`(constraints.dateFrom).thenReturn("dateFrom")
        `when`(constraints.dateTo).thenReturn("dateTo")
        validator.initialize(constraints)
        val request = FileEnquirySearchRequest()
        request.setCycle_id(cycleId)
        if (dateFrom != null) {
            request.setDate_from(dateFrom)
        }
        if (dateTo != null) {
            request.setDate_to(dateTo)
        }

        val validationResult = validator.isValid(request, null)

        assertThat(validationResult).isEqualTo(isValid)
    }

}