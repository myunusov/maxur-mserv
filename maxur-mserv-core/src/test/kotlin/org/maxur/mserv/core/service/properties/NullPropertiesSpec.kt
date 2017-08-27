package org.maxur.mserv.core.service.properties

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.maxur.mserv.core.LocatorImpl
import org.maxur.mserv.core.TestLocatorHolder
import java.net.URI
import kotlin.test.assertFailsWith

@RunWith(JUnitPlatform::class)
class NullPropertiesSpec : Spek({

    describe("a unknown Properties Source") {

        beforeEachTest {
            LocatorImpl.holder = TestLocatorHolder
        }

        context("create new properties source") {
            val sut = NullProperties

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