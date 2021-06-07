package com.vocalink.crossproduct.ui.dto.batch

import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.validation.Validation
import javax.validation.Validator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BatchEnquirySearchRequestValidationTest {

    private lateinit var validator: Validator
    private lateinit var request: BatchEnquirySearchRequest

    companion object {
        const val MSG_DIRECTION_ERROR = "Message direction in request is empty or missing"
        const val WILDCARD_ERROR = "wildcard '*' can not be in the middle and id should not contain special symbols beside '.' and '_'"
        const val PARTICIPANT_BIC_ERROR = "Participant bic in request is empty or missing"
        const val OLDER_THEN_DAYS_LIMIT_ERROR = "date_from can not be earlier than DAYS_LIMIT"
        const val LIMIT_LESS_THAN_ONE = "Limit should be equal or higher than 1"
    }

    @BeforeEach
    fun `init`() {
        request = BatchEnquirySearchRequest()
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `should fail on missing msg_direction`() {
        request.setParticipant_bic("NDEASESSSX")
        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(MSG_DIRECTION_ERROR)
    }

    @Test
    fun `should fail on missing participant_id`() {
        request.setMsg_direction("Sending")
        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(PARTICIPANT_BIC_ERROR)
    }

    @Test
    fun `should fail on bad id regex`() {
        request.setMsg_direction("Sending")
        request.setParticipant_bic("NDEASESSSX")
        request.id = "i*d"

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(WILDCARD_ERROR)
    }

    @Test
    fun `should pass on good id regex`() {
        request.setMsg_direction("Sending")
        request.setParticipant_bic("NDEASESSSX")
        request.id = "*blah"

        val result = ArrayList(validator.validate(request))

        assertThat(result).isEmpty()
    }

    @Test
    fun `should fail on earlier then DAYS_LIMIT`() {
        request.setMsg_direction("Sending")
        request.setParticipant_bic("NDEASESSSX")
        val older = ZonedDateTime.now(ZoneId.of("UTC")).minusDays((getDefault(DtoProperties.DAYS_LIMIT)
            .toLong())+1).toString()
        request.setDate_from(older)

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(OLDER_THEN_DAYS_LIMIT_ERROR)
    }

    @Test
    fun `should fail if limit less than 1`() {
        request.setMsg_direction("Sending")
        request.setParticipant_bic("NDEASESSSX")
        request.limit = 0

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(LIMIT_LESS_THAN_ONE)
    }
}
