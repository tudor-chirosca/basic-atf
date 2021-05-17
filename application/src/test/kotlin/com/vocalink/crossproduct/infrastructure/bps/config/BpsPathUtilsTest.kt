package com.vocalink.crossproduct.infrastructure.bps.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import java.util.stream.Stream

@SpringBootTest(classes = [BPSProperties::class])
@EnableConfigurationProperties
class BpsPathUtilsTest @Autowired constructor(private var props: BPSProperties) {

    companion object {
        var FIRST_PATH_SEGMENT = "schemeName"
        var SECOND_PATH_SEGMENT = "100000000000001"
    }

    @TestFactory
    fun `should return URI with one dynamic path segment`() : Stream<DynamicTest> {
        return props.paths.keys.stream()
            .filter { pathAlias -> props.paths[pathAlias]!!.path.split(Regex("\\{[\\w]*\\}")).size == 2 }
            .map { pathAlias ->
                DynamicTest.dynamicTest(pathAlias) {
                    val uri = BPSPathUtils.resolve(pathAlias, props, FIRST_PATH_SEGMENT)
                    assertThat(uri.toString()).contains(FIRST_PATH_SEGMENT)
                }
            }
    }

    @TestFactory
    fun `should return URI with two dynamic path segments`() : Stream<DynamicTest> {
        return props.paths.keys.stream()
            .filter { pathAlias -> props.paths[pathAlias]!!.path.split(Regex("\\{[\\w]*\\}")).size == 3 }
            .map { pathAlias ->
                DynamicTest.dynamicTest(pathAlias) {
                    val uri = BPSPathUtils.resolve(pathAlias, props, FIRST_PATH_SEGMENT, SECOND_PATH_SEGMENT)
                    assertThat(uri.toString())
                        .contains(FIRST_PATH_SEGMENT)
                        .contains(SECOND_PATH_SEGMENT)
                }
            }
    }

}