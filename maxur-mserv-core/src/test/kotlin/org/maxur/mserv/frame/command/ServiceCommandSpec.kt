package org.maxur.mserv.frame.command

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class ServiceCommandSpec : Spek({

    describe("Execute Service Commands") {

        context("restart command") {
            val command = ServiceCommand.Restart()

            it("should create new id") {
            }
        }
        context("stop command ") {
            val command = ServiceCommand.Stop()
            
            it("should create new id") {
            }
        }
    }
})