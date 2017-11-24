package org.maxur.mserv.core

class EventStream<T : Any> {
    val events = HashSet<Event<T>>()

    fun add(event: Event<T>) {
        events.add(event)
    }
}

