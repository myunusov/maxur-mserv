package org.maxur.mserv.frame.command

import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.maxur.mserv.frame.LocatorImpl
import org.maxur.mserv.frame.TestLocatorHolder
import org.maxur.mserv.frame.embedded.EmbeddedService
import org.maxur.mserv.frame.event.MicroserviceStartedEvent
import org.maxur.mserv.frame.event.MicroserviceStoppedEvent
import org.maxur.mserv.frame.event.WebServerStartedEvent
import org.maxur.mserv.frame.event.WebServerStoppedEvent
import org.maxur.mserv.frame.runner.KRunner
import org.maxur.mserv.frame.runner.Kotlin

class ServiceCommandSpec : Spek({

    describe("Execute Service Commands") {
        LocatorImpl.holder = TestLocatorHolder
        var runner: KRunner? = null

        beforeEachTest {
            runner = Kotlin.runner {
                withoutProperties()
                services += service {
                    ref = object : EmbeddedService {
                        override fun start() = listOf(WebServerStartedEvent())
                        override fun stop() = listOf(WebServerStoppedEvent())
                    }
                }
            }
        }

        context("restart command") {
            it("should create new id") {
                val service = runner!!.next()
                val command = ServiceCommand.Restart(service, runner!!)
                service.start()
                val stream = command.execute()
                Assertions.assertThat(stream.map { it::class })
                    .isEqualTo(listOf(
                        WebServerStoppedEvent::class,
                        MicroserviceStoppedEvent::class,
                        WebServerStartedEvent::class,
                        MicroserviceStartedEvent::class
                    ))
                service.stop()
            }
        }

        context("stop command ") {
            it("should create new id") {
                val service = runner!!.next()
                val command = ServiceCommand.Stop(service)
                service.start()
                val stream = command.execute()
                Assertions.assertThat(stream.map { it::class })
                    .isEqualTo(listOf(
                        WebServerStoppedEvent::class,
                        MicroserviceStoppedEvent::class
                    ))
            }
        }
    }
})