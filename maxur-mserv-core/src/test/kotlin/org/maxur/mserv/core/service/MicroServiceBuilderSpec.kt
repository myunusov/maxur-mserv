package org.maxur.mserv.core.service

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.service.msbuilder.Kotlin
import org.maxur.mserv.core.service.properties.PropertiesService

class MicroServiceBuilderSpec : Spek({

    describe("a micro-service dsl Builder") {

        on("Build empty micro-service") {

            it("should return new micro-service") {
                val service = Kotlin.service {
                    properties {
                        none()
                    }
                }
                service.should.be.not.`null`
                service.start()
                val propertiesService = Locator.current.service(PropertiesService::class.java)
                propertiesService.should.be.not.`null`
                propertiesService!!.name.should.be.equal("None")
                service.stop()
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
