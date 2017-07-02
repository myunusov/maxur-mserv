package org.maxur.mserv.core

import org.maxur.mserv.core.domain.Service
import org.maxur.mserv.core.embedded.EmbeddedService
import java.util.concurrent.Executors

/**
 * This class represents the Microservice.
 */
abstract class MicroService : Service() {

    /**
     * The service version
     */
    abstract val version: String

    /**
     * The bean from service IoC by it's name
     * @param clazz Class of bean
     * @param <T> type of bean
     */
    abstract fun <T> bean(clazz: Class<T>): T?

    /**
     * Stop this Service
     */
    fun deferredStop() = postpone({ state.stop(this) })

    /**
     * Restart this Service
     */
    fun deferredRestart() = postpone({ state.restart(this) })

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

/**
 * The micro-service
 *
 * @param embeddedService Embedded service (may be composite)
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
class BaseMicroService constructor(
    val embeddedService: EmbeddedService,
    val locator: Locator
) : MicroService() {

    init {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                this@BaseMicroService.stop()
            }
        })
    }

    override var name: String = "Anonymous microService"

    override val version: String = MicroService::class.java.`package`.implementationVersion ?: ""

    override fun <T> bean(clazz: Class<T>): T? = locator.service(clazz)



    override fun launch() {
        catchError {
            embeddedService.start()
      }
    }

    override fun shutdown() {
        catchError {
            embeddedService.stop()
            locator.shutdown()
        }
    }

    private fun catchError(function: () -> Unit) {
        try {
            function.invoke()
        } catch(e: Exception) {
            onError.forEach { it.invoke(this, e) }
        }
    }

}

