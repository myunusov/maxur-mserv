package org.maxur.mserv.core.service

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.NullService
import org.maxur.mserv.core.service.msbuilder.Kotlin
import org.maxur.mserv.core.service.properties.PropertiesService
import java.net.URI

class MicroServiceBuilderSpec : Spek({

    describe("a micro-service dsl Builder") {

        on("Build empty micro-service") {

            it("should return new micro-service") {
                val service = Kotlin.service { withoutProperties() }
                service.should.be.not.`null`
                service.start()
                val propertiesService = Locator.current.service(PropertiesService::class.java)
                propertiesService.should.be.not.`null`
                propertiesService!!.source.format.should.be.equal("None")
                service.stop()
            }
        }

        on("Build empty micro-service with Hocon properties") {

            it("should return new micro-service with properties by default") {
                val service = Kotlin.service {
                    properties {
                        format = "Hocon"
                    }
                }
                service.should.be.not.`null`
                service.start()
                val propertiesService = Locator.current.service(PropertiesService::class.java)
                propertiesService.should.be.not.`null`
                propertiesService!!.source.apply {
                    format.should.be.equal("Hocon")
                    rootKey.should.be.equal("DEFAULTS")
                    uri.should.be.equal(URI("classpath:///application.conf"))
                }
                service.stop()
            }

            it("should return new micro-service with described properties file") {
                val service = Kotlin.service {
                    properties {
                        format = "Hocon"
                        url = "src/test/resources/application.conf"
                    }
                }
                service.should.be.not.`null`
                service.start()
                val propertiesService = Locator.current.service(PropertiesService::class.java)
                propertiesService.should.be.not.`null`
                propertiesService!!.source.apply {
                    format.should.be.equal("Hocon")
                    rootKey.should.be.equal("DEFAULTS")
                    uri.should.be.equal(URI("src/test/resources/application.conf"))
                }
                service.stop()
            }

            it("should return new micro-service with described root key") {
                val service = Kotlin.service {
                    properties {
                        format = "Hocon"
                        rootKey = "USER"
                    }
                }
                service.should.be.not.`null`
                service.start()
                val propertiesService = Locator.current.service(PropertiesService::class.java)
                propertiesService.should.be.not.`null`
                propertiesService!!.source.apply {
                    format.should.be.equal("Hocon")
                    rootKey.should.be.equal("USER")
                    uri.should.be.equal(URI("classpath:///application.conf"))
                }
                service.stop()
            }

            it("should return null service on unknown url scheme") {
                val service = Kotlin.service {
                    properties {
                        format = "Hocon"
                        url = "error:///file.cfg"
                    }
                }
                service.should.be.`is`.instanceof(NullService::class.java)
            }

            it("should return null service on unknown file") {
                val service = Kotlin.service {
                    properties {
                        format = "Hocon"
                        url = "file:///error.cfg"
                    }
                }
                service.should.be.`is`.instanceof(NullService::class.java)
            }

            it("should return null service on unknown root key") {
                val service = Kotlin.service {
                    properties {
                        format = "Hocon"
                        rootKey = "ERROR"
                    }
                }
                service.should.be.`is`.instanceof(NullService::class.java)
            }

        }



        on("Build empty micro-service with default properties") {

            it("should return new micro-service") {
                val service = Kotlin.service {
                }
                service.should.be.not.`null`
                service.start()
                service.stop()
            }
        }
    }
})
