package org.maxur.mserv.frame.service.bus

import com.google.common.eventbus.AsyncEventBus
import com.google.common.eventbus.DeadEvent
import com.google.common.eventbus.Subscribe
import org.maxur.mserv.core.command.Event
import org.maxur.mserv.frame.event.MicroserviceStoppedEvent
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

    private val eventBus = object: AsyncEventBus(executor) {
        override fun post(event: Any) {
            super.post(event)
            if (event is MicroserviceStoppedEvent)
                executor.shutdownNow()
        }
    }

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
    override fun publish(list: List<Event>) {
        list.forEach { eventBus.post(it) }
    }

    /** {@inheritDoc} */
    override fun store(list: List<Event>) {
        //TODO Is not implemented yet
    }
}

class DeadEventsListener {
    //TODO Process it
    @Subscribe
    @Suppress("unused")
    fun handleDeadEvent(deadEvent: DeadEvent) {
                println("Event: ${deadEvent.event::class.simpleName}")
    }
}