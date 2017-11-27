package org.maxur.mserv.frame.service.bus

import com.google.common.eventbus.DeadEvent
import com.google.common.eventbus.Subscribe
import org.maxur.mserv.core.EventEnvelope
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * This adapter bind EventBus to google Guava EventBase implementation.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/27/2017</pre>
 */
class EventBusGuavaImpl : EventBus {

    private val executor = ThreadPoolExecutor(1, 1,
        0L, TimeUnit.MILLISECONDS,
        LinkedBlockingQueue())

    private val eventBus = com.google.common.eventbus.AsyncEventBus(executor)

    init {
        register(DeadEventsListener())
    }

    override fun register(listener: Any) {
        eventBus.register(listener)
    }

    override fun unregister(listener: Any) {
        eventBus.unregister(listener)
    }

    /** {@inheritDoc} */
    override fun post(list: List<EventEnvelope>) {
        list.forEach { eventBus.post(it) }
    }
}

class DeadEventsListener {

    @Subscribe
    @Suppress("unused")
    fun handleDeadEvent(deadEvent: DeadEvent) {
        // TODO process this event
        val enveloper = deadEvent.event as EventEnvelope
        println("Event: ${enveloper.event::class.simpleName}")
    }
}