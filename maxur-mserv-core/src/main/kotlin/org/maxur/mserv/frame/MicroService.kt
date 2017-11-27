package org.maxur.mserv.frame

import org.maxur.mserv.core.command.Event
import org.maxur.mserv.frame.domain.BaseService
import org.maxur.mserv.frame.embedded.EmbeddedService
import org.maxur.mserv.frame.event.MicroserviceStartedEvent
import org.maxur.mserv.frame.event.MicroserviceStoppedEvent
import org.maxur.mserv.frame.kotlin.Locator

/**
 * The micro-service
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
interface MicroService {

    /** The service name */
    val name: String

    /** The service version */
    val version: String

    /** Start this Service */
    fun start(): List<Event>

    /** Stop this Service */
    fun stop(): List<Event>
}

/**
 * @param embeddedService Embedded service (may be composite)
 * @param locator Service Locator
 */
class BaseMicroService constructor(
    locator: Locator,
    private val embeddedService: EmbeddedService = locator.service(EmbeddedService::class)!!
) : BaseService(locator), MicroService {

    override var name: String = "Anonymous microService"

    override val version: String = MicroService::class.java.`package`.implementationVersion ?: ""

    init {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            /** {@inheritDoc} */
            override fun run() {
                this@BaseMicroService.stop()
            }
        })
    }

    override fun shutdown(): List<Event> {
        val events = ArrayList(embeddedService.stop())
        locator.shutdown()
        events.add(MicroserviceStoppedEvent())
        return events
    }

    override fun launch(): List<Event> {
        val events = ArrayList(embeddedService.start())
        events.add(MicroserviceStartedEvent())
        return events
    }
}
