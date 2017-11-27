package org.maxur.mserv.frame.service.bus

import org.maxur.mserv.core.EventEnvelope

/**
 * Dispatches events to listeners, and provides ways for listeners to register themselves.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>24.11.2017</pre>
 */
interface EventBus {
    /** post Events from event's [list] */
    fun post(list: List<EventEnvelope>)
    /** register new [listener] */
    fun register(listener: Any)
    /** unregister the [listener] */
    fun unregister(listener: Any)
}