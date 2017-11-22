package org.maxur.mserv.core.core.command

interface Command {
    fun execute()
    val type: String
}