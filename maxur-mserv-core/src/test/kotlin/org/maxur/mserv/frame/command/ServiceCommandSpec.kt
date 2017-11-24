package org.maxur.mserv.frame.command

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.maxur.mserv.frame.LocatorImpl
import org.maxur.mserv.frame.TestLocatorHolder
import org.maxur.mserv.frame.kotlin.Locator
import org.maxur.mserv.frame.runner.Kotlin

class ServiceCommandSpec : Spek({

    describe("Execute Service Commands") {
        
        beforeEachTest {
            LocatorImpl.holder = TestLocatorHolder
        }

        afterEachTest {
            Locator.stop()
        }
        
        context("restart command") {
            val runner = Kotlin.runner { withoutProperties() }
            val service = runner.next()
            val command = ServiceCommand.Restart(service, runner)

            it("should create new id") {
                service.start()
                command.execute()
                service.stop()
                Locator.stop()
            }
        }
        context("stop command ") {
            val runner = Kotlin.runner { withoutProperties() }
            val service = runner.next()

            val command = ServiceCommand.Stop(service)
            
            it("should create new id") {
                service.start()
                command.execute()
                Locator.stop()
            }
        }
    }
})