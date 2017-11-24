package org.maxur.mserv.frame

import org.maxur.mserv.frame.domain.BaseService
import org.maxur.mserv.frame.embedded.EmbeddedService
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
    fun start()

    /** Stop this Service */
    fun stop()
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

    override fun shutdown() {
        embeddedService.stop()
        locator.shutdown()
    }

    override fun launch() {
        embeddedService.start()
    }
}
