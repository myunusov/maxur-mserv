package org.maxur.mserv.core

import org.maxur.mserv.core.command.Event

/** The events stream */
class EventStream : Iterable<Event> {

    private val events = HashSet<Event>()

    /** {@inheritDoc} */
    override fun iterator() = events.iterator()

    /** Post new event */
    fun post(event: Event) {
        events.add(event)
    }
}
