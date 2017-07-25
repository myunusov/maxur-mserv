package org.maxur.mserv.core.service

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.service.msbuilder.Java
import org.maxur.mserv.core.service.msbuilder.Kotlin
import org.maxur.mserv.core.service.properties.PropertiesSource
import java.net.URI
import kotlin.test.assertFailsWith

class MicroServiceBuilderSpec : Spek({

    describe("a empty micro-service") {

        afterEachTest {
            Locator.shutdown()
        }

        context("Build micro-service without properties") {
            it("should return new micro-service") {
                val service = Kotlin.service {
                    withoutProperties()
                }
                service.should.be.not.`null`
                val source = Locator.service(PropertiesSource::class)
                source.should.be.not.`null`
                source!!.format.should.be.equal("None")
                source.isOpened.should.be.`false`
            }
            it("should return new micro-service for java client") {
                val service = Java.service()
                        .withoutProperties()
                        .build()
                service.should.be.not.`null`
                val source = Locator.service(PropertiesSource::class)
                source.should.be.not.`null`
                source!!.format.should.be.equal("None")
                source.isOpened.should.be.`false`
            }
            it("should throw exception on configure none properties source") {
                assertFailsWith<IllegalStateException> {
                    val service = Kotlin.service {
                        properties {
                            format = "None"
                            url = "file:///file.cfg"
                        }
                    }
                }
            }
        }

        context("Build micro-service with Hocon properties without configuration") {
            it("should return new micro-service with default properties source") {
                val service = Kotlin.service {
                    properties {
                        format = "Hocon"
                    }
                }
                service.should.be.not.`null`
                val source = Locator.service(PropertiesSource::class)
                source.should.be.not.`null`
                source!!.apply {
                    isOpened.should.be.`true`
                    format.should.be.equal("Hocon")
                    rootKey.should.be.equal("DEFAULTS")
                    uri.should.be.equal(URI("classpath:///application.conf"))
                }
            }
            it("should return new micro-service with default properties source for java client") {
                val service = Java.service()
                        .properties("Hocon")
                        .build()
                service.should.be.not.`null`
                val source = Locator.service(PropertiesSource::class)
                source.should.be.not.`null`
                source!!.apply {
                    isOpened.should.be.`true`
                    format.should.be.equal("Hocon")
                    rootKey.should.be.equal("DEFAULTS")
                    uri.should.be.equal(URI("classpath:///application.conf"))
                }
            }
        }

        context("Build micro-service with Yaml properties without configuration") {
            it("should return new micro-service with default properties source") {
                val service = Kotlin.service {
                    properties {
                        format = "Yaml"
                    }
                }
                service.should.be.not.`null`
                val source = Locator.service(PropertiesSource::class)
                source.should.be.not.`null`
                source!!.apply {
                    isOpened.should.be.`true`
                    format.should.be.equal("Yaml")
                    rootKey.should.be.`null`
                    uri.should.be.equal(URI("classpath:///application.yaml"))
                }
            }
            it("should return new micro-service with default properties source for java client") {
                val service = Java.service()
                        .properties("Yaml")
                        .build()
                service.should.be.not.`null`
                val source = Locator.service(PropertiesSource::class)
                source.should.be.not.`null`
                source!!.apply {
                    isOpened.should.be.`true`
                    format.should.be.equal("Yaml")
                    rootKey.should.be.`null`
                    uri.should.be.equal(URI("classpath:///application.yaml"))
                }
            }
        }

        context("Build micro-service with Json properties without configuration") {
            it("should return new micro-service with default properties source") {
                val service = Kotlin.service {
                    properties {
                        format = "Json"
                    }
                }
                service.should.be.not.`null`
                val source = Locator.service(PropertiesSource::class)
                source.should.be.not.`null`
                source!!.apply {
                    isOpened.should.be.`true`
                    format.should.be.equal("Json")
                    rootKey.should.be.`null`
                    uri.should.be.equal(URI("classpath:///application.json"))
                }
            }
            it("should return new micro-service with default properties source for java client") {
                val service = Java.service()
                        .properties("Json")
                        .build()
                service.should.be.not.`null`
                val source = Locator.service(PropertiesSource::class)
                source.should.be.not.`null`
                source!!.apply {
                    isOpened.should.be.`true`
                    format.should.be.equal("Json")
                    rootKey.should.be.`null`
                    uri.should.be.equal(URI("classpath:///application.json"))
                }
            }
        }

        context("Build micro-service with Hocon properties file by url") {
            it("should return new micro-service") {
                val service = Kotlin.service {
                    properties {
                        format = "Hocon"
                        url = "src/test/resources/application.conf"
                    }
                }
                service.should.be.not.`null`
                val source = Locator.service(PropertiesSource::class)
                source.should.be.not.`null`
                source!!.apply {
                    isOpened.should.be.`true`
                    format.should.be.equal("Hocon")
                    rootKey.should.be.equal("DEFAULTS")
                    uri.should.be.equal(URI("src/test/resources/application.conf"))
                }
            }
            it("should return new micro-service for java client") {
                val service = Java.service()
                        .properties("Hocon")
                        .url("src/test/resources/application.conf")
                        .build()
                service.should.be.not.`null`
                val source = Locator.service(PropertiesSource::class)
                source.should.be.not.`null`
                source!!.apply {
                    isOpened.should.be.`true`
                    format.should.be.equal("Hocon")
                    rootKey.should.be.equal("DEFAULTS")
                    uri.should.be.equal(URI("src/test/resources/application.conf"))
                }
            }
        }

        context("Build micro-service with Hocon properties and rootKey") {
            it("should return new micro-service ") {
                val service = Kotlin.service {
                    properties {
                        format = "Hocon"
                        rootKey = "USER"
                    }
                }
                service.should.be.not.`null`
                val source = Locator.service(PropertiesSource::class)
                source.should.be.not.`null`
                source!!.apply {
                    isOpened.should.be.`true`
                    format.should.be.equal("Hocon")
                    rootKey.should.be.equal("USER")
                    uri.should.be.equal(URI("classpath:///application.conf"))
                }
            }
            it("should return new micro-service for java client") {
                val service = Java.service()
                        .properties("Hocon")
                        .rootKey("USER")
                        .build()
                service.should.be.not.`null`
                val source = Locator.service(PropertiesSource::class)
                source.should.be.not.`null`
                source!!.apply {
                    isOpened.should.be.`true`
                    format.should.be.equal("Hocon")
                    rootKey.should.be.equal("USER")
                    uri.should.be.equal(URI("classpath:///application.conf"))
                }
            }
        }

        context("Build micro-service with Hocon properties with invalid configuration") {

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
                            format = "Hocon"
                            url = "error:///file.cfg"
                        }
                    }
                }
            }
            it("should throw error on unknown url scheme for java client") {
                assertFailsWith<IllegalStateException> {
                    Java.service()
                            .properties("Hocon")
                            .url("error:///file.cfg")
                            .build()
                }
            }
            it("should throw error on unknown file") {
                assertFailsWith<IllegalStateException> {
                    Kotlin.service {
                        properties {
                            format = "Hocon"
                            url = "file:///error.cfg"
                        }
                    }
                }
            }
            it("should throw error on unknown file for java client") {
                assertFailsWith<IllegalStateException> {
                    Java.service()
                            .properties("Hocon")
                            .url("file:///error.cfg")
                            .build()
                }
            }
            it("should throw error on unknown root key") {
                assertFailsWith<IllegalStateException> {
                    Kotlin.service {
                        properties {
                            format = "Hocon"
                            rootKey = "ERROR"
                        }
                    }
                }
            }
            it("should throw error on unknown root key for java client") {
                assertFailsWith<IllegalStateException> {
                    Java.service()
                            .properties("Hocon")
                            .rootKey("ERROR")
                            .build()
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
                source!!.apply {
                    format.should.be.satisfy { arrayOf("Hocon", "Yaml", "Json").contains(it) }
                    isOpened.should.be.`true`
                }
            }
        }
    }


    describe("a rest micro-service") {
        it("should return new micro-service") {
            val service = Kotlin.service {
                withoutProperties()
                rest {  }
            }
            service.should.be.not.`null`
        }
    }

})
