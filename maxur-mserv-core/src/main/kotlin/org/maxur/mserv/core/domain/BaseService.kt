package org.maxur.mserv.core.domain

import org.maxur.mserv.core.Locator
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType


abstract class BaseService(val locator: Locator) {

    var beforeStart: MutableList<KFunction<Any>> = ArrayList()
    var afterStart: MutableList<KFunction<Any>> = ArrayList()
    var afterStop: MutableList<KFunction<Any>> = ArrayList()
    var beforeStop: MutableList<KFunction<Any>> = ArrayList()
    var onError: MutableList<KFunction<Any>> = ArrayList()

    protected var state: BaseService.State = BaseService.State.STOPPED
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
            override fun start(service: BaseService, locator: Locator) = Unit
            override fun stop(service: BaseService, locator: Locator) = shutdown(service, locator)
            override fun restart(service: BaseService, locator: Locator) {
                shutdown(service, locator)
                launch(service, locator)

            }
        },
        /**
         * Stop application
         */
        STOPPED {
            override fun start(service: BaseService, locator: Locator) = launch(service, locator)
            override fun stop(service: BaseService, locator: Locator) = Unit
            override fun restart(service: BaseService, locator: Locator) = launch(service, locator)
        };
        abstract fun start(service: BaseService, locator: Locator)
        abstract fun stop(service: BaseService, locator: Locator)
        abstract fun restart(service: BaseService, locator: Locator)

        protected fun shutdown(service: BaseService, locator: Locator) {
            try {
                service.shutdown()
                service.beforeStop.forEach { call(it, service, locator) }
                service.state = BaseService.State.STOPPED
                service.afterStop.forEach { call(it, service, locator) }
            } catch(e: Exception) {
                service.onError.forEach { error(it, service, locator, e) }
            }
        }
        protected fun launch(service: BaseService, locator: Locator) {
            try {
                service.beforeStart.forEach { call(it, service, locator) }
                service.launch()
                service.afterStart.forEach { call(it, service, locator) }
                service.state = BaseService.State.STARTED
            } catch(e: Exception) {
                service.onError.forEach { error(it, service, locator, e) }
            }
        }

        private fun call(func: KFunction<Any>, service: BaseService, locator: Locator) {
            val params = func.parameters.map {
                param ->
                when {
                    isInheriting(param.type, service.javaClass) -> service
                    else ->  locator.service(param.type.javaType as Class<*>)
                }
            }
            func.call(*params.toTypedArray())
        }

        private fun error(func: KFunction<Any>, service: BaseService, locator: Locator, exception: Exception) {
            val params = func.parameters.map {
                param ->
                when {
                    isInheriting(param.type, Exception::class.java) -> exception
                    isInheriting(param.type, service.javaClass) -> service
                    else ->  locator.service(param.type.javaType as Class<*>)
                }
            }
            func.call(*params.toTypedArray())
        }

        private fun isInheriting(type: KType, clazz: Class<*>) = (type.javaType as Class<*>).isAssignableFrom(clazz)

    }

}