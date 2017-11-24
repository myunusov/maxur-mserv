package org.maxur.mserv.frame.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.maxur.mserv.frame.LocatorImpl
import org.maxur.mserv.frame.TestLocatorHolder
import org.maxur.mserv.frame.kotlin.Locator
import org.maxur.mserv.frame.relativePathByResourceName
import org.maxur.mserv.frame.runner.Java
import org.maxur.mserv.frame.runner.Kotlin.runner
import org.maxur.mserv.frame.runner.PredefinedPropertiesBuilder
import org.maxur.mserv.frame.runner.PropertiesBuilder
import org.maxur.mserv.frame.runner.hocon
import org.maxur.mserv.frame.runner.json
import org.maxur.mserv.frame.runner.yaml
import org.maxur.mserv.frame.service.properties.Properties
import org.maxur.mserv.frame.service.properties.PropertiesSource
import java.net.URL
import java.util.function.Predicate
import kotlin.test.assertFailsWith

val properties: Properties? get() = Locator.bean(Properties::class)
val source: PropertiesSource? get() = Locator.bean(Properties::class)?.sources?.firstOrNull()

@RunWith(JUnitPlatform::class)
class MicroServiceRunnerSpec : Spek({

    val function: Map<String, (PredefinedPropertiesBuilder.() -> Unit) -> PropertiesBuilder> = mapOf(
        "Hocon" to ::hocon,
        "Json" to ::json,
        "Yaml" to ::yaml
    )

    describe("Build empty micro-service") {

        beforeEachTest {
            LocatorImpl.holder = TestLocatorHolder
        }

        afterEachTest {
            Locator.stop()
        }

        context("without properties") {
            it("should return new micro-service") {
                val service = runner {
                    withoutProperties()
                }.next()
                assertThat(service).isNotNull()
                val source = source
                assertThat(source).isNotNull()
            }
            it("should return new micro-service for java client") {
                val service =
                    // tag::withoutproperties[]
                    Java.runner()
                        .withoutProperties() // <1>
                        .next()
                // end::withoutproperties[]
                assertThat(service).isNotNull()
                val source = source
                assertThat(source).isNotNull()
            }
        }

        context("with properties object") {
            it("should return new micro-service") {
                val service = runner {
                    properties += "name" to "Test Service"
                    properties += "url" to URL("file:///")
                    properties += "count" to 0
                }.next()
                assertThat(service).isNotNull()
                val source = source
                assertThat(source).isNotNull()
                val properties = properties
                assertThat(properties).isNotNull()
                assertThat(properties!!.asString("name")).isEqualTo("Test Service")
                assertThat(properties.read("url", URL::class)).isEqualTo(URL("file:///"))
                assertThat(properties.asInteger("count")).isEqualTo(0)
                assertThat(properties.asString("none")).isNull()
            }
            it("should return new micro-service for java client") {
                val service =
                    Java.runner()
                        .properties("name", "Test Service")
                        .properties("url", URL("file:///"))
                        .properties("count", 0)
                        .next()
                assertThat(service).isNotNull()
                val source = source
                assertThat(source).isNotNull()
                val properties = properties
                assertThat(properties).isNotNull()
                assertThat(properties!!.asString("name")).isEqualTo("Test Service")
                assertThat(properties.read("url", URL::class)).isEqualTo(URL("file:///"))
                assertThat(properties.asInteger("count")).isEqualTo(0)
                assertThat(properties.asString("none")).isNull()
            }
        }

        context("with properties without configuration") {

            listOf(
                Triple("Hocon", "DEFAULTS", "conf"),
                Triple("Yaml", "/", "yaml"),
                Triple("Json", "/", "json")
            )
                .forEach { (name, root, ext) ->
                    describe("With '$name' properties") {

                        it("should return new micro-service with named properties source") {
                            val service = runner {
                                properties += function[name]!!.invoke({})
                            }.next()
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

                        it("should return new micro-service with default properties source") {
                            val service = runner {
                                properties += file { format = name }
                            }.next()
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
                            val service = Java.runner()
                                .properties(name)
                                .next()
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
                Triple("Yaml", "/", "yaml"),
                Triple("Json", "/", "json")
            )
                .forEach { (name, root, ext) ->
                    describe("With '$name' properties") {

                        val propertyFile = relativePathByResourceName("/application.$ext") ?:
                            throw IllegalStateException("file application.$ext is not found")

                        it("should return new micro-service with named properties source") {
                            val service = runner {
                                properties += function[name]!!.invoke({
                                    url = propertyFile
                                })
                            }.next()
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

                        it("should return new micro-service with properties") {
                            val service = runner {
                                properties += file {
                                    format = name
                                    url = propertyFile
                                }
                            }.next()
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
                            val service = Java.runner()
                                .properties(name)
                                .url(propertyFile)
                                .next()
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

                        it("should return new micro-service with named properties source") {
                            val service = runner {
                                properties += function[name]!!.invoke({
                                    rootKey = "USER"
                                })
                            }.next()
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

                        it("should return new micro-service with properties") {
                            val service = runner {
                                properties += file {
                                    format = name
                                    rootKey = "USER"
                                }
                            }.next()
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
                            val service = Java.runner()
                                .properties(name)
                                .rootKey("USER")
                                .next()
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
                            runner {
                                properties += file {
                                    format = "Error"
                                    url = "file:///file.cfg"
                                }
                            }
                            Locator.bean(Properties::class)
                        }
                    }
                    it("should throw error on unknown url scheme") {
                        assertFailsWith<IllegalStateException> {
                            runner {
                                properties += file {
                                    format = name
                                    url = "error:///file.cfg"
                                }
                            }
                            Locator.bean(Properties::class)
                        }
                    }
                    it("should throw error on unknown url scheme for java client") {
                        assertFailsWith<IllegalStateException> {
                            Java.runner()
                                .properties(name)
                                .url("error:///file.cfg")
                                .next()
                            Locator.bean(Properties::class)
                        }
                    }
                    it("should throw error on unknown file") {
                        assertFailsWith<IllegalStateException> {
                            runner {
                                properties += file {
                                    format = name
                                    url = "file:///error.cfg"
                                }
                            }
                            Locator.bean(Properties::class)
                        }
                    }
                    it("should throw error on unknown file for java client") {
                        assertFailsWith<IllegalStateException> {
                            Java.runner()
                                .properties(name)
                                .url("file:///error.cfg")
                                .next()
                            Locator.bean(Properties::class)
                        }
                    }
                    it("should throw error on unknown root key") {
                        assertFailsWith<IllegalStateException> {
                            runner {
                                properties += file {
                                    format = name
                                    rootKey = "ERROR"
                                }
                            }
                            Locator.bean(Properties::class)
                        }
                    }
                    it("should throw error on unknown root key for java client") {
                        assertFailsWith<IllegalStateException> {
                            Java.runner()
                                .properties(name)
                                .rootKey("ERROR")
                                .next()
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
                val service = runner { }.next()
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
            val service = runner {
                withoutProperties()
                rest { }
            }
            assertThat(service).isNotNull()
        }
    }
})

private fun condition(function: (String) -> Boolean, description: String): Condition<String>
    = Condition(Predicate(function), description)
