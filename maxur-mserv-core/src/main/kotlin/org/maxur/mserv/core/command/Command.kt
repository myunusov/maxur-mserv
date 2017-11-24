package org.maxur.mserv.core.command

import org.maxur.mserv.core.EventStream

interface Command {

    val type: String

    fun execute(): EventStream<out Any> {
        run()
        return EventStream()
    }

    fun run()
}