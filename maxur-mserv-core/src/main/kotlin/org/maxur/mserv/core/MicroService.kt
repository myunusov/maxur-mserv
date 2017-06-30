@file:Suppress("unused")

package org.maxur.mserv.core

import org.maxur.mserv.core.embedded.EmbeddedService
import java.util.concurrent.Executors


interface MicroService {

    var name: String

    fun <T> bean(clazz: Class<T>): T?
    /**
     * Start Service
     */
    fun start()

    /**
     * Stop Service
     */
    fun deferredStop()

    fun stop()
    /**
     * Restart Service
     */
    fun deferredRestart()

    /**
     * Represent State of micro-service
     */
    enum class State {

        /**
         *  Running application
         */
        START,

        /**
         * Stop application
         */
        STOP,

        /**
         * Restart application
         */
        RESTART;

        companion object {

            fun from(value: String): MicroService.State {
                val case = value.toUpperCase()
                if (case in State::class.java.enumConstants.map { e -> e.name }) {
                    return State.valueOf(case)
                } else {
                    throw IllegalArgumentException("The '$value' is not acceptable Application State")
                }
            }
        }

    }
}

/**
 * The micro-service
 *
 * @param service Embedded service (may be composite)
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
class BaseMicroService constructor(
    val service: EmbeddedService,
    val locator: Locator
) : MicroService {

    private var state: MicroService.State = MicroService.State.STOP

    init {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                this@BaseMicroService.stop()
            }
        })
    }

    override var name: String = "Anonymous microService"
    var beforeStart: ((BaseMicroService) -> Unit)? = null
    var afterStop: ((BaseMicroService) -> Unit)? = null
    var onError: ((BaseMicroService, Exception) -> Unit)? = null

    override fun <T> bean(clazz: Class<T>): T? = locator.service(clazz)

    /**
     * Start Service
     */
    override fun start() {
        if (state == MicroService.State.START) return
        try {
            beforeStart?.invoke(this)
            service.start()
            state = MicroService.State.START
        } catch(e: Exception) {
            error(e)
        }
    }

    /**
     * Stop Service
     */
    override fun deferredStop() {
        if (state == MicroService.State.STOP) return
        try {
            postpone({
                service.stop()
                afterStop?.invoke(this)
                state = MicroService.State.STOP
            })
        } catch(e: Exception) {
            error(e)
        }
    }

    override fun stop() {
        if (state == MicroService.State.STOP) return
        try {
            service.stop()
            afterStop?.invoke(this)
            state = MicroService.State.STOP
        } catch(e: Exception) {
            error(e)
        }
    }

    /**
     * Restart Service
     */
    override fun deferredRestart() {
        if (state == MicroService.State.STOP) return
        try {
            postpone({
                service.stop()
                afterStop?.invoke(this)
                state = MicroService.State.STOP
                beforeStart?.invoke(this)
                service.start()
                state = MicroService.State.START
            })
        } catch(e: Exception) {
            error(e)
        }
    }

    private fun error(exception: Exception) {
        onError?.invoke(this, exception)
    }


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


}

