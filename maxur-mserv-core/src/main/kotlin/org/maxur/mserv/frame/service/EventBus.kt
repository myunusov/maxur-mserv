package org.maxur.mserv.frame.service

import org.maxur.mserv.core.EventEnvelope

/**
 * The event bus.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>24.11.2017</pre>
 */
class EventBus {

    /** post Event Stream */
    fun post(list: List<EventEnvelope>) {
        // TODO
        list.forEach { System.out.println(it.event::class.simpleName) }
    }
}