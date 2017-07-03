package org.maxur.mserv.core.domain

import org.maxur.mserv.core.Locator
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaType


abstract class Service(val locator: Locator) {

    var beforeStart: MutableList<KFunction<Any>> = ArrayList()
    var afterStop: MutableList<KFunction<Any>> = ArrayList()
    var onError: MutableList<KFunction<Any>> = ArrayList()

    protected var state: Service.State = Service.State.STOPPED
    private set

    /**
     * The service name
     */
    abstract var name: String

    /**
     * Start this Service
     */
    fun start() = state.start(this, locator)

    /**
     * Immediately shuts down this Service.
     */
    fun stop() = state.stop(this, locator)

    /**
     * shutdown this service.
     */
    abstract protected fun shutdown()

    /**
     * launch this service.
     */
    abstract protected fun launch()

    /**
     * Represent State of micro-service
     */
    enum class State {
        /**
         *  Running application
         */
        STARTED {
            override fun start(service: Service, locator: Locator) = Unit
            override fun stop(service: Service, locator: Locator) = shutdown(service, locator)
            override fun restart(service: Service, locator: Locator) {
                shutdown(service, locator)
                launch(service, locator)

            }
        },
        /**
         * Stop application
         */
        STOPPED {
            override fun start(service: Service, locator: Locator) = launch(service, locator)
            override fun stop(service: Service, locator: Locator) = Unit
            override fun restart(service: Service, locator: Locator) = launch(service, locator)
        };
        abstract fun start(service: Service, locator: Locator)
        abstract fun stop(service: Service, locator: Locator)
        abstract fun restart(service: Service, locator: Locator)

        protected fun shutdown(service: Service, locator: Locator) {
            try {
                service.shutdown()
                service.state = Service.State.STOPPED
                service.afterStop.forEach { call(it, locator) }
            } catch(e: Exception) {
                service.onError.forEach { error(it, locator, e) }
            }
        }
        protected fun launch(service: Service, locator: Locator) {
            try {
                service.beforeStart.forEach { call(it, locator) }
                service.launch()
                service.state = Service.State.STARTED
            } catch(e: Exception) {
                service.onError.forEach { error(it, locator, e) }
            }
        }

        private fun call(func: KFunction<Any>, locator: Locator) {
            val params = func.parameters.map {
                param ->
                locator.service(param.type.javaType as Class<*>)
            }
            func.call(*params.toTypedArray())
        }

        private fun error(func: KFunction<Any>, locator: Locator, exception: Exception) {
            val params = func.parameters.map {
                param ->
                when {
                    param.type.javaType == Exception::class.java -> exception
                    else ->  locator.service(param.type.javaType as Class<*>)
                }
            }
            func.call(*params.toTypedArray())
        }

    }

}