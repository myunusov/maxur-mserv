package org.maxur.mserv.core.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.maxur.mserv.core.LocatorImpl
import org.maxur.mserv.core.TestLocatorHolder
import org.maxur.mserv.core.builder.Java
import org.maxur.mserv.core.builder.Kotlin
import org.maxur.mserv.core.kotlin.Locator
import org.maxur.mserv.core.relativePathByResourceName
import org.maxur.mserv.core.service.properties.Properties
import org.maxur.mserv.core.service.properties.PropertiesSource
import java.util.function.Predicate
import kotlin.test.assertFailsWith

val source: PropertiesSource? get() = Locator.bean(Properties::class)?.sources?.firstOrNull()

@RunWith(JUnitPlatform::class)
class MicroServiceBuilderSpec : Spek({

    describe("Build empty micro-service") {

        beforeEachTest {
            LocatorImpl.holder = TestLocatorHolder
        }

        afterEachTest {
            Locator.stop()
        }

        context("without properties") {
            it("should return new micro-service") {
                val service = Kotlin.service {
                    withoutProperties()
                }
                assertThat(service).isNotNull()
                val source = source
                assertThat(source).isNotNull()
            }
            it("should return new micro-service for java client") {
                val service =
                    // tag::withoutproperties[]
                    Java.service()
                        .withoutProperties() // <1>
                        .build()
                // end::withoutproperties[]
                assertThat(service).isNotNull()
                val source = source
                assertThat(source).isNotNull()
            }
        }

        context("with properties without configuration") {

            listOf(
                Triple("Hocon", "DEFAULTS", "conf"),
                Triple("Yaml", null, "yaml"),
                Triple("Json", null, "json")
            )
                .forEach { (name, root, ext) ->
                    describe("With '$name' properties") {

                        it("should return new micro-service with default properties source") {
                            val service = Kotlin.service {
                                properties {
                                    format = name
                                }
                            }
                            assertThat(service).isNotNull()
                            val source = source
                            assertThat(source).isNotNull()
                            source!!.apply {
                                assertThat(format).isEqualTo(name)
                                assertThat(rootKey).isEqualTo(root)
                                assertThat(uri.toString()).endsWith("application.$ext")
                            }
                            Locator.stop()
                        }

                        it("should return new micro-service with default properties source for java client") {
                            val service = Java.service()
                                .properties(name)
                                .build()
                            assertThat(service).isNotNull()
                            val source = source
                            assertThat(source).isNotNull()
                            source!!.apply {
                                assertThat(format).isEqualTo(name)
                                assertThat(rootKey).isEqualTo(root)
                                assertThat(uri.toString()).endsWith("application.$ext")
                            }
                            Locator.stop()
                        }

                    }

                }
        }

        context("with properties file by url") {

            listOf(
                Triple("Hocon", "DEFAULTS", "conf"),
                Triple("Yaml", null, "yaml"),
                Triple("Json", null, "json")
            )
                .forEach { (name, root, ext) ->
                    describe("With '$name' properties") {

                        val propertyFile = relativePathByResourceName("/application.$ext") ?:
                            throw IllegalStateException("file application.$ext is not found")

                        it("should return new micro-service with properties") {
                            val service = Kotlin.service {
                                properties {
                                    format = name
                                    url = propertyFile
                                }
                            }
                            assertThat(service).isNotNull()
                            val source = source
                            assertThat(source).isNotNull()
                            source!!.apply {
                                assertThat(format).isEqualTo(name)
                                assertThat(rootKey).isEqualTo(root)
                                assertThat(uri.toString()).endsWith("application.$ext")
                            }
                            Locator.stop()
                        }

                        it("should return new micro-service with properties for java client") {
                            val service = Java.service()
                                .properties(name)
                                .url(propertyFile)
                                .build()
                            assertThat(service).isNotNull()
                            val source = source
                            assertThat(source).isNotNull()
                            source!!.apply {
                                assertThat(format).isEqualTo(name)
                                assertThat(rootKey).isEqualTo(root)
                                assertThat(uri.toString()).endsWith("application.$ext")
                            }
                            Locator.stop()
                        }
                    }
                }
        }

        context("with properties file with rootKey") {

            listOf(
                Triple("Hocon", "USER", "conf"),
                Triple("Yaml", "USER", "yaml"),
                Triple("Json", "USER", "json")
            )
                .forEach { (name, root, ext) ->
                    describe("With '$name' properties") {

                        it("should return new micro-service with properties") {
                            val service = Kotlin.service {
                                properties {
                                    format = name
                                    rootKey = "USER"
                                }
                            }
                            assertThat(service).isNotNull()
                            val source = source
                            assertThat(source).isNotNull()
                            source!!.apply {
                                assertThat(format).isEqualTo(name)
                                assertThat(rootKey).isEqualTo(root)
                                assertThat(uri.toString()).endsWith("application.$ext")
                            }
                            Locator.stop()
                        }

                        it("should return new micro-service with properties for java client") {
                            val service = Java.service()
                                .properties(name)
                                .rootKey("USER")
                                .build()
                            assertThat(service).isNotNull()
                            val source = source
                            assertThat(source).isNotNull()
                            source!!.apply {
                                assertThat(format).isEqualTo(name)
                                assertThat(rootKey).isEqualTo(root)
                                assertThat(uri.toString()).endsWith("application.$ext")
                            }
                            Locator.stop()
                        }

                    }

                }
        }

        context("with properties file with invalid configuration") {

            listOf("Hocon", "Yaml", "Json").forEach { name ->
                describe("With '$name' properties") {

                    it("should throw error on unknown format") {
                        assertFailsWith<IllegalStateException> {
                            Kotlin.service {
                                properties {
                                    format = "Error"
                                    url = "file:///file.cfg"
                                }
                            }
                            Locator.bean(Properties::class)
                        }
                    }
                    it("should throw error on unknown url scheme") {
                        assertFailsWith<IllegalStateException> {
                            Kotlin.service {
                                properties {
                                    format = name
                                    url = "error:///file.cfg"
                                }
                            }
                            Locator.bean(Properties::class)
                        }
                    }
                    it("should throw error on unknown url scheme for java client") {
                        assertFailsWith<IllegalStateException> {
                            Java.service()
                                .properties(name)
                                .url("error:///file.cfg")
                                .build()
                            Locator.bean(Properties::class)
                        }
                    }
                    it("should throw error on unknown file") {
                        assertFailsWith<IllegalStateException> {
                            Kotlin.service {
                                properties {
                                    format = name
                                    url = "file:///error.cfg"
                                }
                            }
                            Locator.bean(Properties::class)
                        }
                    }
                    it("should throw error on unknown file for java client") {
                        assertFailsWith<IllegalStateException> {
                            Java.service()
                                .properties(name)
                                .url("file:///error.cfg")
                                .build()
                            Locator.bean(Properties::class)
                        }
                    }
                    it("should throw error on unknown root key") {
                        assertFailsWith<IllegalStateException> {
                            Kotlin.service {
                                properties {
                                    format = name
                                    rootKey = "ERROR"
                                }
                            }
                            Locator.bean(Properties::class)
                        }
                    }
                    it("should throw error on unknown root key for java client") {
                        assertFailsWith<IllegalStateException> {
                            Java.service()
                                .properties(name)
                                .rootKey("ERROR")
                                .build()
                            Locator.bean(Properties::class)
                        }
                    }

                }

            }
        }

        val supportedFormat = condition(
            { it in arrayOf("Hocon", "Yaml", "Json") }, "supported format"
        )

        context("Build micro-service with default properties") {
            it("should return new micro-service") {
                val service = Kotlin.service { }
                assertThat(service).isNotNull()
                val source = source
                assertThat(source).isNotNull()
                assertThat(source!!.format).isNotNull()
                assertThat(source.format).`is`(supportedFormat)
            }
        }
    }

    describe("a rest micro-service") {
        it("should return new micro-service") {
            val service = Kotlin.service {
                withoutProperties()
                rest { }
            }
            assertThat(service).isNotNull()
        }
    }

})

private fun condition(function: (String) -> Boolean, description: String): Condition<String>
    = Condition(Predicate(function), description)
