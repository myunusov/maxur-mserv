package org.maxur.mserv.core.domain

import org.maxur.mserv.core.kotlin.Locator
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf

abstract class BaseService(val locator: Locator) {

    var afterStart: MutableList<KFunction<Any>> = ArrayList()
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
    fun start() = state.start(this)

    /**
     * Immediately shuts down this Service.
     */
    fun stop() = state.stop(this)

    /**
     * Immediately restart this Service.
     */
    fun restart() = state.restart(this)

    /**
     * shutdown this service.
     */
    protected abstract fun shutdown()

    /**
     * launch this service.
     */
    protected abstract fun launch()

    /**
     * relaunch this service.
     */
    protected abstract fun relaunch()

    /**
     * Represent State of micro-service
     */
    enum class State {
        /**
         *  Running application
         */
        STARTED {
            override fun start(service: BaseService) = Unit
            override fun stop(service: BaseService) = shutdown(service)
            override fun restart(service: BaseService) = relaunch(service)
        },
        /**
         * Stop application
         */
        STOPPED {
            override fun start(service: BaseService) = launch(service)
            override fun stop(service: BaseService) = Unit
            override fun restart(service: BaseService) = launch(service)
        };

        abstract fun start(service: BaseService)
        abstract fun stop(service: BaseService)
        abstract fun restart(service: BaseService)

        protected fun shutdown(service: BaseService) = check(service, {
            beforeStop.forEach { call(it, service) }
            shutdown()
            state = STOPPED
        })

        protected fun launch(service: BaseService) = check(service, {
            launch()
            state = STARTED
            afterStart.forEach { call(it, service) }
        })

        protected fun relaunch(service: BaseService) = check(service, {
            beforeStop.forEach { call(it, service) }
            relaunch()
            afterStart.forEach { call(it, service) }
            state = STARTED
        })

        private inline fun check(service: BaseService, function: BaseService.() -> Unit) {
            try {
                service.apply(function)
            } catch (e: Exception) {
                if (service.onError.isEmpty())
                    throw e
                else
                    service.onError.forEach { call(it, service, e) }
            }
        }

        private fun call(func: KFunction<Any>, vararg values: Any) {
            val args = func.parameters.map { match(it, *values) }.toTypedArray()
            func.call(*args)
        }

        private fun match(param: KParameter, vararg values: Any): Any? =
            values.firstOrNull { isApplicable(param, it) } ?: Locator.bean(param)

        @Suppress("UNCHECKED_CAST")
        private fun isApplicable(type: KType, clazz: KClass<out Any>) =
            clazz.isSubclassOf(type.classifier as KClass<Any>)

        private fun isApplicable(param: KParameter, value: Any) = isApplicable(param.type, value::class)
    }
}