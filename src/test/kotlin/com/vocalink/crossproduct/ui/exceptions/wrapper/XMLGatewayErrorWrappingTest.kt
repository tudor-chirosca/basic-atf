package com.vocalink.crossproduct.ui.exceptions.wrapper

import com.vocalink.crossproduct.infrastructure.exception.ErrorConstants
import com.vocalink.crossproduct.shared.exception.AdapterException
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
import com.vocalink.crossproduct.ui.exceptions.GatewayErrorDescription
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.mock.http.MockHttpInputMessage
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.BindException
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class XMLGatewayErrorWrappingTest {

    private val xmlErrorWrappingStrategy = XMLGatewayErrorWrappingStrategy()
    private val obj = FileEnquirySearchRequest()
    @Test
    fun `Bind exceptions should list all errors and be treated as Bad request`() {

        val bindingResult = BeanPropertyBindingResult(obj, "fileEnquirySearchRequest")
        bindingResult.rejectValue("messageDirection", "message direction error")
        val bindException = BindException(bindingResult)
        val wrappedError = xmlErrorWrappingStrategy.wrapException(bindException)

        assertEquals(HttpStatus.BAD_REQUEST.value(), wrappedError.statusCode.value())
        val body: GatewayErrorDescription? = wrappedError.body as? GatewayErrorDescription
        assertNotNull(body)
        assertEquals(1, body.errors.error.size)
    }

    @Test
    fun `Message not readable exception should be treated as a Bad request`() {
        val exception = HttpMessageNotReadableException("not accessible",
                MockHttpInputMessage("Not readable".toByteArray()))
        val wrappedError = xmlErrorWrappingStrategy.wrapException(exception)
        assertEquals(HttpStatus.BAD_REQUEST.value(), wrappedError.statusCode.value())
        val body: GatewayErrorDescription? = wrappedError.body as? GatewayErrorDescription
        assertNotNull(body)
        assertEquals(1, body.errors.error.size)
        assertEquals(ErrorConstants.ERROR_SOURCE_ISS, body.errors.error[0].source)
    }

    @Test
    fun `Adapter exception should be treated as internal error`() {
        val exception = AdapterException(RuntimeException(), ErrorConstants.ERROR_SOURCE_BPS)
        val wrappedError = xmlErrorWrappingStrategy.wrapException(exception)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), wrappedError.statusCode.value())
        val body: GatewayErrorDescription? = wrappedError.body as? GatewayErrorDescription
        assertNotNull(body)
        assertEquals(1, body.errors.error.size)
    }

    @Test
    fun `Adapter exception should propagate the source of the error`() {
        val exception = AdapterException(RuntimeException(), ErrorConstants.ERROR_SOURCE_BPS)
        val wrappedError = xmlErrorWrappingStrategy.wrapException(exception)
        val body: GatewayErrorDescription? = wrappedError.body as? GatewayErrorDescription
        assertNotNull(body)
        assertEquals(ErrorConstants.ERROR_SOURCE_BPS, body.errors.error[0].source)
    }

    @Test
    fun `Any other error should be treated as internal error`() {
        val exception = RuntimeException("something")
        val wrappedError = xmlErrorWrappingStrategy.wrapException(exception)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), wrappedError.statusCode.value())
        val body: GatewayErrorDescription? = wrappedError.body as? GatewayErrorDescription
        assertNotNull(body)
        assertEquals(1, body.errors.error.size)
    }
}
