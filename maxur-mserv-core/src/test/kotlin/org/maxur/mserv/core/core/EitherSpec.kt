package org.maxur.mserv.core.core

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class EitherSpec : Spek({

    describe("Build either") {

        context("as right") {
            val right: Either<String, String> = right("R")

            it("should create new instance") {
                assertThat(right).isNotNull()
            }

            it("should fold right value") {
                val result = right.fold(
                    { throw AssertionError("it's left but must be right") },
                    { r -> r + "1" }
                )
                assertThat(result).isEqualTo("R1")
            }
            it("should map right value by mapRight") {
                val result = right.mapRight { r -> right(r + "1") }
                assertThat(result).isEqualTo(right("R1"))
            }
            it("should do nothing by mapLeft") {
                val result = right.mapLeft { r -> left(r + "1") }
                assertThat(result).isEqualTo(right)
            }
        }
        context("as left") {
            val left: Either<String, String> = left("L")

            it("should create new instance") {
                assertThat(left).isNotNull()
            }
            it("should fold right value") {
                val result = left.fold(
                    { l -> l + "1" },
                    { throw AssertionError("it's right but must be left") }
                )
                assertThat(result).isEqualTo("L1")
            }
            it("should do nothing by mapRight") {
                val result = left.mapRight { r -> right(r + "1") }
                assertThat(result).isEqualTo(left)
            }
            it("should map left value by mapLeft") {
                val result = left.mapLeft { r -> r + "1" }
                assertThat(result).isEqualTo(left("L1"))
            }
        }
    }
})

