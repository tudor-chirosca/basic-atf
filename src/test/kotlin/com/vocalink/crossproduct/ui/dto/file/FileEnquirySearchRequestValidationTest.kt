package com.vocalink.crossproduct.ui.dto.file;

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
        const val OLDER_THEN_30_ERROR = "date_from can not be earlier than 30 days"
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
    fun `should fail on longer then 30`() {
        request.setMsg_direction("Sending")
        val older = LocalDate.now().minusDays(31).toString()
        request.setDate_from(older)

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(OLDER_THEN_30_ERROR)
    }

    @Test
    fun `should pass on 30`() {
        request.setMsg_direction("Sending")
        val older = LocalDate.now().minusDays(30).toString()
        request.setDate_from(older)

        val result = ArrayList(validator.validate(request))

        assertThat(result).isEmpty()
    }

//    @Test //TODO: fix validation
//    fun `should fail if both cycle and date to are present`() {
//        request.setMsg_direction("Sending")
//        request.setCycle_id("1")
//        request.setDate_to(LocalDate.now().toString())
//
//        val result = ArrayList(validator.validate(request))
//
//        assertThat(result).isNotEmpty
//        assertThat(result[0].message).isEqualTo(CYCLE_OR_DATE_ERROR)
//    }

    @Test
    fun `should fail if limit less than 1`() {
        request.setMsg_direction("Sending")
        request.limit = 0

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(LIMIT_LESS_THAN_ONE)
    }
}
