package org.maxur.mserv.frame.service.bus

import org.maxur.mserv.core.Id
import org.maxur.mserv.core.command.Event

/**
 * Dispatches events to listeners, and provides ways for listeners to register themselves.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>24.11.2017</pre>
 */
interface EventBus {
    /** post Events from event's [list] */
    fun post(list: List<Event>) {
        list.filter { it.id == Id.Unknown }
            .map { throw IllegalStateException("Event '$it' has not identifier") }
        store(list)
        publish(list)
    }

    /** store events */
    fun store(list: List<Event>)

    /** publish events to subscribers */
    fun publish(list: List<Event>)

    /** register new [listener] */
    fun register(listener: Any)

    /** unregister the [listener] */
    fun unregister(listener: Any)
}