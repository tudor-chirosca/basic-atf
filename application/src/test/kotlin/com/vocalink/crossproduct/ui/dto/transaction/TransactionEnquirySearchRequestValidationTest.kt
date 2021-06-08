package com.vocalink.crossproduct.ui.dto.transaction

import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.validation.Validation
import javax.validation.Validator

class TransactionEnquirySearchRequestValidationTest {

    private lateinit var validator: Validator
    private lateinit var request: TransactionEnquirySearchRequest

    companion object {
        const val MSG_DIRECTION_ERROR = "Message direction in request is empty or missing"
        const val CYCLE_ID_ERROR = "CycleId either both dateFrom and dateTo must not be null"
        const val WILDCARD_ERROR = "wildcard '*' can not be in the middle and id should not contain special symbols beside '.' and '_'"
        const val DIFFERENT_BIC_ERROR = "send_bic and recv_bic should not be the same"
        const val OLDER_THEN_DAYS_LIMIT_ERROR = "date_from can not be earlier than DAYS_LIMIT"
        const val CYCLE_OR_DATE_ERROR = "cycle_ids and date_to are both included in request params, exclude one of them"
        const val LIMIT_LESS_THAN_ONE = "Limit should be equal or higher than 1"
    }

    @BeforeEach
    fun `init`() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `should fail on missing msg_direction`() {
        request = TransactionEnquirySearchRequest(
                0, 20,
                null,
                null,
                null,
                "20190212004",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        )
        val result = ArrayList(validator.validate(request))
        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(MSG_DIRECTION_ERROR)
    }

    @Test
    fun `should fail on missing cycle_id`() {
        request = TransactionEnquirySearchRequest(
            0, 20,
            null,
            null,
            null,
            null,
            "sending",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val result = ArrayList(validator.validate(request))
        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(CYCLE_ID_ERROR)
    }

    @Test
    fun `should fail on invalid id regex`() {
        request = TransactionEnquirySearchRequest(
                0, 20,
                null,
                null,
                null,
            "20190212004",
                "sending",
                null,
                null,
                null,
                null,
                null,
                "i*d",
                null,
                null,
                null,
                null,
                null
        )
        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(WILDCARD_ERROR)
    }

    @Test
    fun `should pass on valid id regex`() {
        request = TransactionEnquirySearchRequest(
                0, 20,
                null,
                null,
                null,
                "20190212004",
                "sending",
                null,
                null,
                null,
                null,
                null,
                null,
                "*blah",
                null,
                null,
                null,
                null
        )

        val result = ArrayList(validator.validate(request))
        assertThat(result).isEmpty()
    }

    @Test
    fun `should fail on same sending and receiving bic`() {
        request = TransactionEnquirySearchRequest(
                0, 20,
                null,
                null,
                null,
                "20190212004",
                "sending",
                null,
                "123",
                "123",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        )
        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(DIFFERENT_BIC_ERROR)
    }

    @Test
    fun `should fail on longer then DAYS_LIMIT`() {
        val dateFrom = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(getDefault(DtoProperties.DAYS_LIMIT).toLong()+1)
        val dateTo = ZonedDateTime.now(ZoneId.of("UTC"))
        request = TransactionEnquirySearchRequest(
                0, 20,
                null,
                dateFrom,
                dateTo,
                null,
                "sending",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        )
        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(OLDER_THEN_DAYS_LIMIT_ERROR)
    }

    @Test
    fun `should pass if days are equal to DAYS_LIMIT`() {
        val dateFrom = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(getDefault(DtoProperties.DAYS_LIMIT).toLong())
        val dateTo = ZonedDateTime.now(ZoneId.of("UTC"))
        request = TransactionEnquirySearchRequest(
                0, 20,
                null,
                dateFrom,
                dateTo,
                null,
                "sending",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        )
        val result = ArrayList(validator.validate(request))

        assertThat(result).isEmpty()
    }

    @Test
    fun `should fail if limit is less than 1`() {
        request = TransactionEnquirySearchRequest(
                0, 0,
                null,
                null,
                null,
                "20190212004",
                "sending",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        )

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(LIMIT_LESS_THAN_ONE)
    }
}