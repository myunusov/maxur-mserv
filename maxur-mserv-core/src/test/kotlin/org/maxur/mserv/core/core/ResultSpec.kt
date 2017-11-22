package org.maxur.mserv.core.core

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertFailsWith

@RunWith(JUnitPlatform::class)
class ResultSpec : Spek({

    describe("Build error either") {

        context("as value") {
            val value: Result<AssertionError, String> = value("R")

            it("should create new instance") {
                assertThat(value).isNotNull()
            }

            it("should fold value") {
                val result = value.fold(
                    { throw AssertionError("it's left but must be right") },
                    { r -> r + "1" }
                )
                assertThat(result).isEqualTo("R1")
            }
            it("should map value by map") {
                val result = value.map { r -> r + "1" }
                assertThat(result).isEqualTo(value("R1"))
            }
            it("should do nothing by mapError") {
                val result = value.mapError { e -> AssertionError(e) }
                assertThat(result).isEqualTo(value)
            }
            it("should do value map by flatMap") {
                val result = value.flatMap { r -> value(r + "1") }
                assertThat(result).isEqualTo(value("R1"))
            }
        }
        context("as error") {
            val error: Result<AssertionError, String> = error(AssertionError())

            it("should create new instance") {
                assertThat(error).isNotNull()
            }
            it("should fold error") {
                assertFailsWith<AssertionError> {
                    error.fold(
                        { e -> throw AssertionError(e) },
                        { throw AssertionError("it's right but must be left") }
                    )
                }
            }
            it("should do nothing by map") {
                val result = error.map { r -> r + "1" }
                assertThat(result).isEqualTo(error)
            }
            it("should do nothing by flatMap") {
                val result = error.flatMap { r -> value(r + "1") }
                assertThat(result).isEqualTo(error)
            }
            it("should map error value by mapError") {
                assertFailsWith<AssertionError> {
                    error.mapError { e -> throw AssertionError(e) }
                }
            }
        }
    }
})

