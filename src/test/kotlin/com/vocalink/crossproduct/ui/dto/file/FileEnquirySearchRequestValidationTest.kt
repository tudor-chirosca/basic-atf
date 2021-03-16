package com.vocalink.crossproduct.ui.dto.file;

import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*
import javax.validation.Validation
import javax.validation.Validator

class FileEnquirySearchRequestValidationTest {

    private lateinit var validator: Validator
    private lateinit var request: FileEnquirySearchRequest

    companion object {
        const val MSG_DIRECTION_ERROR = "msg_direction in request parameters in empty or missing"
        const val WILDCARD_ERROR = "wildcard '*' can not be in the middle and id should not contain special symbols beside '.' and '_'"
        const val DIFFERENT_BIC_ERROR = "send_bic and recv_bic should not be the same"
        const val OLDER_THEN_DAYS_LIMIT_ERROR = "date_from can not be earlier than DAYS_LIMIT"
        const val CYCLE_OR_DATE_ERROR = "cycle_ids and date_to are both included in request params, exclude one of them"
        const val LIMIT_LESS_THAN_ONE = "Limit should be equal or higher than 1"
    }

    @BeforeEach
    fun `init`() {
        request = FileEnquirySearchRequest()
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `should fail on missing msg_direction`() {
        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(MSG_DIRECTION_ERROR)
    }

    @Test
    fun `should fail on bad id regex`() {
        request.setMsg_direction("Sending")
        request.id = "i*d"

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(WILDCARD_ERROR)
    }

    @Test
    fun `should pass on good id regex`() {
        request.setMsg_direction("Sending")
        request.id = "*blah"

        val result = ArrayList(validator.validate(request))

        assertThat(result).isEmpty()
    }

    @Test
    fun `should fail on same sending and receiving bic`() {
        request.setMsg_direction("Sending")
        request.setSend_bic("123")
        request.setRecv_bic("123")

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(DIFFERENT_BIC_ERROR)
    }

    @Test
    fun `should fail on longer then DAYS_LIMIT`() {
        request.setMsg_direction("Sending")
        val older = LocalDate.now().minusDays(
            (getDefault(DtoProperties.DAYS_LIMIT).toLong())+1).toString()
        request.setDate_from(older)

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(OLDER_THEN_DAYS_LIMIT_ERROR)
    }

    @Test
    fun `should fail if limit less than 1`() {
        request.setMsg_direction("Sending")
        request.limit = 0

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(LIMIT_LESS_THAN_ONE)
    }
}
