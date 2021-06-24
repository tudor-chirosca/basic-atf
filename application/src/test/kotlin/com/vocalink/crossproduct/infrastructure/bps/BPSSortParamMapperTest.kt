package com.vocalink.crossproduct.infrastructure.bps

import java.util.stream.Stream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class BPSSortParamMapperTest {

    companion object {
        val bindings = mapOf("name" to "fileName", "created-at" to "createdDate", "senderBic" to "originator")

        @JvmStatic
        fun getData() = Stream.of(
            Arguments.of("name", BPSSortingQuery("fileName", BPSSortParamMapper.DEFAULT_SORTING_ORDER)),
            Arguments.of("  name     ", BPSSortingQuery("fileName", BPSSortParamMapper.DEFAULT_SORTING_ORDER)),
            Arguments.of("-name", BPSSortingQuery("fileName", BPSSortOrder.DESC)),
            Arguments.of("    -name", BPSSortingQuery("fileName", BPSSortOrder.DESC)),
            Arguments.of("+created-at", BPSSortingQuery("createdDate", BPSSortOrder.ASC)),
            Arguments.of("-created-at", BPSSortingQuery("createdDate", BPSSortOrder.DESC)),
            Arguments.of("unknown", BPSSortingQuery(null, BPSSortParamMapper.DEFAULT_SORTING_ORDER)),
            Arguments.of(null, BPSSortingQuery(null, BPSSortParamMapper.DEFAULT_SORTING_ORDER))
        )
    }

    @ParameterizedTest(name = "sort parameter: \"{0}\", result: \"{1}\"")
    @MethodSource("getData")
    fun `should map sorting param`(sortParam: String?, expected: BPSSortingQuery ) {
        val actual = BPSSortParamMapper.resolveParams(sortParam, bindings)

        assertThat(actual.sortOrder).isEqualTo(expected.sortOrder)
        assertThat(actual.sortOrderBy).isEqualTo(expected.sortOrderBy)
    }
}