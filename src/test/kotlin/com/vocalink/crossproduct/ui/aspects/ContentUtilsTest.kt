package com.vocalink.crossproduct.ui.aspects;

import com.fasterxml.jackson.databind.ObjectMapper
import com.vocalink.crossproduct.ui.aspects.EventType.FILE_ENQUIRY
import com.vocalink.crossproduct.ui.aspects.OperationType.REQUEST
import com.vocalink.crossproduct.ui.aspects.OperationType.RESPONSE
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
import com.vocalink.crossproduct.ui.exceptions.UILayerException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test

open class ContentUtilsTest {

    private val contentUtils = ContentUtils(ObjectMapper())

    companion object {
        private val ANY_REQUEST_OBJECT = FileEnquirySearchRequest()

        private val CONTENT = "{\"offset\": 0,\"limit\": 20}"
        private val INVALID_CONTENT = "{blah:\"2020-02-10\"}"

    }

    @Test
    fun `should convert an Object to jsonString`() {
        ANY_REQUEST_OBJECT.offset = 0
        ANY_REQUEST_OBJECT.limit = 20
        ANY_REQUEST_OBJECT.setMsg_direction("Any Direction")

        val toJsonString = contentUtils.toJsonString(ANY_REQUEST_OBJECT)

        assertThat(toJsonString).contains(ANY_REQUEST_OBJECT.messageDirection)
        assertThat(toJsonString).contains(ANY_REQUEST_OBJECT.offset.toString())
        assertThat(toJsonString).contains(ANY_REQUEST_OBJECT.limit.toString())
    }

    @Test
    fun `should throw exception if failed to convert Object to jsonString`() {
        assertThatExceptionOfType(UILayerException::class.java)
                .isThrownBy { contentUtils.toJsonString(Object()) }
    }

    @Test
    fun `should serialize string to Object based on event and operation types`() {

        val toObject = contentUtils.toObject(CONTENT, FILE_ENQUIRY, REQUEST) as FileEnquirySearchRequest

        assertThat(CONTENT).contains(toObject.offset.toString())
        assertThat(CONTENT).contains(toObject.limit.toString())
    }

    @Test
    fun `should return content if operation type RESPONSE`() {

        val toObject = contentUtils.toObject(CONTENT, FILE_ENQUIRY, RESPONSE)

        assertThat(CONTENT).isEqualTo(toObject)
    }

    @Test
    fun `should throw exception if failed serializing`() {
        assertThatExceptionOfType(UILayerException::class.java).isThrownBy {
            contentUtils.toObject(INVALID_CONTENT, FILE_ENQUIRY, REQUEST)
        }
    }
}
