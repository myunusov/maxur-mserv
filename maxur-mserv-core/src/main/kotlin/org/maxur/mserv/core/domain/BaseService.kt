package org.maxur.mserv.core.domain

import org.maxur.mserv.core.Locator
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf


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
     * Immediately restart this Service.
     */
    fun restart() = state.restart(this, locator)


    /**
     * shutdown this service.
     */
    abstract protected fun shutdown()

    /**
     * launch this service.
     */
    abstract protected fun launch()

    /**
     * relaunch this service.
     */
    abstract protected fun relaunch()


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
            override fun restart(service: BaseService, locator: Locator) = relaunch(service, locator)
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
                service.beforeStop.forEach {
                    call(it, locator, service)
                }
                service.state = BaseService.State.STOPPED
                service.afterStop.forEach {
                    call(it, locator, service)
                }
            } catch(e: Exception) {
                service.onError.forEach {
                    call(it, locator, service, e)
                }
            }
        }

        protected fun launch(service: BaseService, locator: Locator) = try {
            service.beforeStart.forEach { call(it, locator, service) }
            service.launch()
            service.afterStart.forEach { call(it, locator, service) }
            service.state = BaseService.State.STARTED
        } catch(e: Exception) {
            service.onError.forEach { call(it, locator, service, e) }
        }

        protected fun relaunch(service: BaseService, locator: Locator) {
            try {
                service.beforeStop.forEach { call(it, locator, service) }
                service.relaunch()
                service.afterStart.forEach { call(it, locator, service) }
                service.state = BaseService.State.STARTED
            } catch(e: Exception) {
                service.onError.forEach { call(it, locator, service, e) }
            }
        }

        private fun call(func: KFunction<Any>, locator: Locator, vararg values: Any) {
            val args = func.parameters.map { match(locator, it, *values) }.toTypedArray()
            func.call(*args)
        }

        private fun match(locator: Locator, param: KParameter, vararg values: Any): Any? =
                values.filter { isApplicable(param, it) }.firstOrNull() ?: locator.service(param)

        @Suppress("UNCHECKED_CAST")
        private fun isApplicable(type: KType, clazz: KClass<out Any>) =
                clazz.isSubclassOf(type.classifier as KClass<Any>)

        private fun isApplicable(param: KParameter, value: Any) = isApplicable(param.type, value::class)

    }

}