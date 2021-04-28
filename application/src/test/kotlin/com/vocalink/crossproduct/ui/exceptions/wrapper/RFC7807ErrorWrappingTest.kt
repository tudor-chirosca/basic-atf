package com.vocalink.crossproduct.ui.exceptions.wrapper

import com.vocalink.crossproduct.infrastructure.exception.ErrorConstants
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
import com.vocalink.crossproduct.ui.exceptions.RFCErrorDescription
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.mock.http.MockHttpInputMessage
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.BindException
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class RFC7807ErrorWrappingTest {

    private val rfcErrorWrappingStrategy = RFC7807ErrorWrappingStrategy()
    private val obj = FileEnquirySearchRequest()
    @Test
    fun `Bind exceptions should list all errors and be treated as Bad request`() {

        val bindingResult = BeanPropertyBindingResult(obj, "fileEnquirySearchRequest")
        bindingResult.rejectValue("messageDirection", "message direction error")
        val bindException = BindException(bindingResult)
        val wrappedError = rfcErrorWrappingStrategy.wrapException(bindException)

        assertEquals(HttpStatus.BAD_REQUEST.value(), wrappedError.statusCode.value())
        val body: RFCErrorDescription? = wrappedError.body as? RFCErrorDescription
        assertNotNull(body)
        assertEquals(1, body.errorDetails.size)
        assertEquals(ErrorConstants.ERROR_REASON_INVALID_INPUT, body.errorDetails[0].reason)
    }

    @Test
    fun `Message not readable exception should be treated as a Bad request`() {
        val exception = HttpMessageNotReadableException("not accessible",
                MockHttpInputMessage("Not readable".toByteArray()))
        val wrappedError = rfcErrorWrappingStrategy.wrapException(exception)
        assertEquals(HttpStatus.BAD_REQUEST.value(), wrappedError.statusCode.value())
        val body: RFCErrorDescription? = wrappedError.body as? RFCErrorDescription
        assertNotNull(body)
        assertEquals(HttpStatus.BAD_REQUEST.reasonPhrase, body.title)
    }

    @Test
    fun `Adapter exception should be treated as internal error`() {
        val exception = InfrastructureException(RuntimeException(), ErrorConstants.ERROR_SOURCE_BPS)
        val wrappedError = rfcErrorWrappingStrategy.wrapException(exception)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), wrappedError.statusCode.value())
        val body: RFCErrorDescription? = wrappedError.body as? RFCErrorDescription
        assertNotNull(body)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase, body.title)
    }

    @Test
    fun `Adapter exception should propagate the source of the error`() {
        val exception = InfrastructureException(RuntimeException(), ErrorConstants.ERROR_SOURCE_BPS)
        val wrappedError = rfcErrorWrappingStrategy.wrapException(exception)
        val body: RFCErrorDescription? = wrappedError.body as? RFCErrorDescription
        assertNotNull(body)
        assertEquals(ErrorConstants.ERROR_SOURCE_BPS, body.errorDetails[0].source)
    }

    @Test
    fun `Any other error should be treated as internal error`() {
        val exception = RuntimeException("something")
        val wrappedError = rfcErrorWrappingStrategy.wrapException(exception)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), wrappedError.statusCode.value())
        val body: RFCErrorDescription? = wrappedError.body as? RFCErrorDescription
        assertNotNull(body)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase, body.title)
    }
}
