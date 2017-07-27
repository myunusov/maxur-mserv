package org.maxur.mserv.core.service.properties

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.net.URI
import kotlin.test.assertFailsWith


class PropertiesSourceNoneImplSpec : Spek({

    describe("a unknown Properties Source") {

        fun none(uri: URI?= null, root: String? = null): PropertiesSource = object : PropertiesSource {
            override val format: String? get() = "None"
            override val uri: URI? get() = uri
            override val rootKey: String? get() = root
        }

        context("create new properties source") {
            val sut = PropertiesFactoryNullImpl().make(none())
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