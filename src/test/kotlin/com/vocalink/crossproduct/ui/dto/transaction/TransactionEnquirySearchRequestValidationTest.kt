package com.vocalink.crossproduct.ui.dto.transaction

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.util.*
import javax.validation.Validation
import javax.validation.Validator

class TransactionEnquirySearchRequestValidationTest {

    private lateinit var validator: Validator
    private lateinit var request: TransactionEnquirySearchRequest

    companion object {
        const val MSG_DIRECTION_ERROR = "msg_direction in request parameters in empty or missing"
        const val WILDCARD_ERROR = "wildcard '*' can not be in the middle and id should not contain special symbols beside '.' and '_'"
        const val DIFFERENT_BIC_ERROR = "send_bic and recv_bic should not be the same"
        const val OLDER_THEN_30_ERROR = "date_from can not be earlier than 30 days"
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
                null,
                null,
                null
        )
        val result = ArrayList(validator.validate(request))
        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(MSG_DIRECTION_ERROR)
    }

    @Test
    fun `should fail on invalid id regex`() {
        request = TransactionEnquirySearchRequest(
                0, 20,
                null,
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
                null,
                null,
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
                null,
                null,
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
    fun `should fail on longer then 30`() {
        val older = ZonedDateTime.now().minusDays(31)
        request = TransactionEnquirySearchRequest(
                0, 20,
                null,
                older,
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
        assertThat(result[0].message).isEqualTo(OLDER_THEN_30_ERROR)
    }

    @Test
    fun `should pass on 30 days`() {
        val older = ZonedDateTime.now().minusDays(30)
        request = TransactionEnquirySearchRequest(
                0, 20,
                null,
                older,
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

        assertThat(result).isEmpty()
    }

    @Test
    fun `should fail if limit is less than 1`() {
        request = TransactionEnquirySearchRequest(
                0, 0,
                null,
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
        assertThat(result[0].message).isEqualTo(LIMIT_LESS_THAN_ONE)
    }
}