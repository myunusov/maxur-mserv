package org.maxur.mserv.core.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.TestLocatorHolder
import org.maxur.mserv.core.service.msbuilder.Java
import org.maxur.mserv.core.service.msbuilder.Kotlin
import org.maxur.mserv.core.service.properties.Properties
import org.maxur.mserv.core.service.properties.PropertiesSource
import java.util.function.Predicate
import kotlin.test.assertFailsWith

@RunWith(JUnitPlatform::class)
class MicroServiceBuilderSpec : Spek({

    describe("Build empty micro-service") {

        beforeEachTest {
            Locator.holder = TestLocatorHolder
        }

        afterEachTest {
            Locator.shutdown()
        }

        context("without properties") {
            it("should return new micro-service") {
                val service = Kotlin.service {
                    withoutProperties()
                }
                assertThat(service).isNotNull()
                val source = Locator.service(Properties::class)
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
                val source = Locator.service(Properties::class)
                assertThat(source).isNotNull()
            }
        }

        context("with properties without configuration") {

            listOf(
                    Triple("Hocon", "DEFAULTS", "conf"),
                    Triple("Yaml", null, "yaml"),
                    Triple("Json", null, "json")
            )
                    .forEach {
                        (name, root, ext) ->
                        describe("With '$name' properties") {

                            it("should return new micro-service with default properties source") {
                                val service = Kotlin.service {
                                    properties {
                                        format = name
                                    }
                                }
                                assertThat(service).isNotNull()
                                val source = Locator.service(PropertiesSource::class)
                                assertThat(source).isNotNull()
                                source!!.apply {
                                    assertThat(format).isEqualTo(name)
                                    assertThat(rootKey).isEqualTo(root)
                                    assertThat(uri.toString()).endsWith("application.$ext")
                                }
                                Locator.shutdown()
                            }

                            it("should return new micro-service with default properties source for java client") {
                                val service = Java.service()
                                        .properties(name)
                                        .build()
                                assertThat(service).isNotNull()
                                val source = Locator.service(PropertiesSource::class)
                                assertThat(source).isNotNull()
                                source!!.apply {
                                    assertThat(format).isEqualTo(name)
                                    assertThat(rootKey).isEqualTo(root)
                                    assertThat(uri.toString()).endsWith("application.$ext")
                                }
                                Locator.shutdown()
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
                    .forEach {
                        (name, root, ext) ->
                        describe("With '$name' properties") {

                            it("should return new micro-service with properties") {
                                val service = Kotlin.service {
                                    properties {
                                        format = name
                                        url = "src/test/resources/application.$ext"
                                    }
                                }
                                assertThat(service).isNotNull()
                                val source = Locator.service(PropertiesSource::class)
                                assertThat(source).isNotNull()
                                source!!.apply {
                                    assertThat(format).isEqualTo(name)
                                    assertThat(rootKey).isEqualTo(root)
                                    assertThat(uri.toString()).endsWith("application.$ext")
                                }
                                Locator.shutdown()
                            }

                            it("should return new micro-service with properties for java client") {
                                val service = Java.service()
                                        .properties(name)
                                        .url("src/test/resources/application.$ext")
                                        .build()
                                assertThat(service).isNotNull()
                                val source = Locator.service(PropertiesSource::class)
                                assertThat(source).isNotNull()
                                source!!.apply {
                                    assertThat(format).isEqualTo(name)
                                    assertThat(rootKey).isEqualTo(root)
                                    assertThat(uri.toString()).endsWith("application.$ext")
                                }
                                Locator.shutdown()
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
                    .forEach {
                        (name, root, ext) ->
                        describe("With '$name' properties") {

                            it("should return new micro-service with properties") {
                                val service = Kotlin.service {
                                    properties {
                                        format = name
                                        rootKey = "USER"
                                    }
                                }
                                assertThat(service).isNotNull()
                                val source = Locator.service(PropertiesSource::class)
                                assertThat(source).isNotNull()
                                source!!.apply {
                                    assertThat(format).isEqualTo(name)
                                    assertThat(rootKey).isEqualTo(root)
                                    assertThat(uri.toString()).endsWith("application.$ext")
                                }
                                Locator.shutdown()
                            }

                            it("should return new micro-service with properties for java client") {
                                val service = Java.service()
                                        .properties(name)
                                        .rootKey("USER")
                                        .build()
                                assertThat(service).isNotNull()
                                val source = Locator.service(PropertiesSource::class)
                                assertThat(source).isNotNull()
                                source!!.apply {
                                    assertThat(format).isEqualTo(name)
                                    assertThat(rootKey).isEqualTo(root)
                                    assertThat(uri.toString()).endsWith("application.$ext")
                                }
                                Locator.shutdown()
                            }

                        }

                    }
        }

        context("with properties file with invalid configuration") {

            listOf("Hocon", "Yaml", "Json").forEach {
                        name -> describe("With '$name' properties") {

                            it("should throw error on unknown format") {
                                assertFailsWith<IllegalStateException> {
                                    Kotlin.service {
                                        properties {
                                            format = "Error"
                                            url = "file:///file.cfg"
                                        }
                                    }
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
                                }
                            }
                            it("should throw error on unknown url scheme for java client") {
                                assertFailsWith<IllegalStateException> {
                                    Java.service()
                                            .properties(name)
                                            .url("error:///file.cfg")
                                            .build()
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
                                }
                            }
                            it("should throw error on unknown file for java client") {
                                assertFailsWith<IllegalStateException> {
                                    Java.service()
                                            .properties(name)
                                            .url("file:///error.cfg")
                                            .build()
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
                                }
                            }
                            it("should throw error on unknown root key for java client") {
                                assertFailsWith<IllegalStateException> {
                                    Java.service()
                                            .properties(name)
                                            .rootKey("ERROR")
                                            .build()
                                }
                            }

                        }

                    }
        }

        val supportedFormat = condition (
                { it in arrayOf("Hocon", "Yaml", "Json") }, "supported format"
        )

        context("Build micro-service with default properties") {
            it("should return new micro-service") {
                val service = Kotlin.service {
                }
                assertThat(service).isNotNull()
                val source = Locator.service(PropertiesSource::class)
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
