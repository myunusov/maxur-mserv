package org.maxur.mserv.core.service.properties

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.net.URI
import kotlin.test.assertFailsWith


class RawPropertiesSourceSpec : Spek({

    describe("a unknown Properties Source") {

        context("create new properties source") {
            val sut = PropertiesSource.make("Hocon")

            it("should throw exception when properties is read") {
                assertFailsWith<IllegalStateException> {
                    sut.asString("name")
                }
                assertFailsWith<IllegalStateException> {
                    sut.asLong("id")
                }
                assertFailsWith<IllegalStateException> {
                    sut.asInteger("id")
                }
                assertFailsWith<IllegalStateException> {
                    sut.asURI("url")
                }
                assertFailsWith<IllegalStateException> {
                    sut.read("url", URI::class)
                }
            }

        }
    }

})