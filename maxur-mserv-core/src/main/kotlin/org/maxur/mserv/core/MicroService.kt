package org.maxur.mserv.core

import org.maxur.mserv.core.domain.BaseService
import org.maxur.mserv.core.embedded.EmbeddedService
import java.util.concurrent.Executors

/**
 * The micro-service
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
interface MicroService  {

    /**
     * The service name
     */
    val name: String

    /**
     * The service version
     */
    val version: String

    /**
     * Start this Service
     */
    fun start()

    /**
     * Stop this Service
     */
    fun stop()

    /**
     * Stop this Service
     */
    fun deferredStop()

    /**
     * Restart this Service
     */
    fun deferredRestart()

}


/**
 * @param embeddedService Embedded service (may be composite)
 * @param locator Service Locator
 */
class BaseMicroService constructor(
        val embeddedService: EmbeddedService,
        locator: Locator
) : BaseService(locator), MicroService {

    init {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                this@BaseMicroService.stop()
            }
        })
    }

    override var name: String = "Anonymous microService"

    override val version: String = MicroService::class.java.`package`.implementationVersion ?: ""

    override fun deferredStop() = postpone({ state.stop(this, locator) })

    override fun deferredRestart() = postpone({ state.restart(this, locator) })

    private fun postpone(func: () -> Unit) {
        val pool = Executors.newSingleThreadExecutor { runnable ->
            val thread = Executors.defaultThreadFactory().newThread(runnable)
            thread.isDaemon = false
            thread
        }
        pool.submit {
            Thread.sleep(1000)
            func.invoke()
        }
        pool.shutdown()
    }


    override fun launch() {
        embeddedService.start()
    }

    override fun shutdown() {
        embeddedService.stop()
        locator.shutdown()
    }

    override fun relaunch() {
        embeddedService.restart()
    }
}
