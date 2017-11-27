package org.maxur.mserv.core

/** The events stream */
class EventStream : Iterable<EventEnvelope> {

    private val events = HashSet<EventEnvelope>()

    /** {@inheritDoc} */
    override fun iterator() = events.iterator()

    /** Post new event */
    fun post(event: EventEnvelope) {
        events.add(event)
    }
}

