package org.maxur.mserv.core.service

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.maxur.mserv.core.service.hk2.DSL

class MicroServiceBuilderSpec : Spek({

    describe("a micro-service dsl Builder") {

        on("Build empty micro-service") {

            it("should return new micro-service") {
                val service = DSL.service {
                    properties {
                        none()
                    }
                }
                service.should.be.not.`null`
                service.start()
                service.stop()
            }
        }

        on("Build empty micro-service with default properties") {

            it("should return new micro-service") {
                val service = DSL.service {
                }
                service.should.be.not.`null`
                service.start()
                service.stop()
            }
        }
    }
})
