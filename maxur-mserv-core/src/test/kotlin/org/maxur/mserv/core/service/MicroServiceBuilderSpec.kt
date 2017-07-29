package org.maxur.mserv.core.service

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.service.msbuilder.Java
import org.maxur.mserv.core.service.msbuilder.Kotlin
import org.maxur.mserv.core.service.properties.Properties
import org.maxur.mserv.core.service.properties.PropertiesSource
import kotlin.test.assertFailsWith

class MicroServiceBuilderSpec : Spek({

    describe("Build empty micro-service") {

        afterEachTest {
            Locator.shutdown()
        }

        context("without properties") {
            it("should return new micro-service") {
                val service = Kotlin.service {
                    withoutProperties()
                }
                service.should.be.not.`null`
                val source = Locator.service(Properties::class)
                source.should.be.not.`null`
            }
            it("should return new micro-service for java client") {
                val service = Java.service()
                        .withoutProperties()
                        .build()
                service.should.be.not.`null`
                val source = Locator.service(Properties::class)
                source.should.be.not.`null`
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
                                service.should.be.not.`null`
                                val source = Locator.service(PropertiesSource::class)
                                source.should.be.not.`null`
                                source!!.apply {
                                    format.should.be.equal(name)
                                    rootKey.should.be.equal(root)
                                    uri.should.be.satisfy { it.toString().endsWith("application.$ext") }
                                }
                                Locator.shutdown()
                            }

                            it("should return new micro-service with default properties source for java client") {
                                val service = Java.service()
                                        .properties(name)
                                        .build()
                                service.should.be.not.`null`
                                val source = Locator.service(PropertiesSource::class)
                                source.should.be.not.`null`
                                source!!.apply {
                                    format.should.be.equal(name)
                                    rootKey.should.be.equal(root)
                                    uri.should.be.satisfy { it.toString().endsWith("application.$ext") }
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
                                service.should.be.not.`null`
                                val source = Locator.service(PropertiesSource::class)
                                source.should.be.not.`null`
                                source!!.apply {
                                    format.should.be.equal(name)
                                    rootKey.should.be.equal(root)
                                    uri.should.be.satisfy { it.toString().endsWith("application.$ext") }
                                }
                                Locator.shutdown()
                            }

                            it("should return new micro-service with properties for java client") {
                                val service = Java.service()
                                        .properties(name)
                                        .url("src/test/resources/application.$ext")
                                        .build()
                                service.should.be.not.`null`
                                val source = Locator.service(PropertiesSource::class)
                                source.should.be.not.`null`
                                source!!.apply {
                                    format.should.be.equal(name)
                                    rootKey.should.be.equal(root)
                                    uri.should.be.satisfy { it.toString().endsWith("application.$ext") }
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
                                service.should.be.not.`null`
                                val source = Locator.service(PropertiesSource::class)
                                source.should.be.not.`null`
                                source!!.apply {
                                    format.should.be.equal(name)
                                    rootKey.should.be.equal(root)
                                    uri.should.be.satisfy { it.toString().endsWith("application.$ext") }
                                }
                                Locator.shutdown()
                            }

                            it("should return new micro-service with properties for java client") {
                                val service = Java.service()
                                        .properties(name)
                                        .rootKey("USER")
                                        .build()
                                service.should.be.not.`null`
                                val source = Locator.service(PropertiesSource::class)
                                source.should.be.not.`null`
                                source!!.apply {
                                    format.should.be.equal(name)
                                    rootKey.should.be.equal(root)
                                    uri.should.be.satisfy { it.toString().endsWith("application.$ext") }
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

        context("Build micro-service with default properties") {
            it("should return new micro-service") {
                val service = Kotlin.service {
                }
                service.should.be.not.`null`
                val source = Locator.service(PropertiesSource::class)
                source.should.be.not.`null`
                source!!.format.should.be.not.`null`
                source.apply {
                    format.should.be.satisfy { arrayOf("Hocon", "Yaml", "Json").contains(it) }
                }
            }
        }
    }


    describe("a rest micro-service") {
        it("should return new micro-service") {
            val service = Kotlin.service {
                withoutProperties()
                rest { }
            }
            service.should.be.not.`null`
        }
    }

})
