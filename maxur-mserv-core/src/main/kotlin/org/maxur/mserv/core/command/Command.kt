package org.maxur.mserv.core.command

/** The Command with some Application Logic */
abstract class Command {

    private val events = ArrayList<Event>()

    /** Execute command */
    fun execute(): List<Event> {
        run()
        return events
    }

    protected fun post(event: Event) {
        this.events.add(event)
    }

    protected fun post(events: List<Event>) {
        this.events.addAll(events)
    }

    protected abstract fun run()
}