package org.maxur.mserv.frame.domain

import org.maxur.mserv.frame.kotlin.Locator
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf

/** The Base Service */
abstract class BaseService(
    /** The Service Locator */
    val locator: Locator) {
    /** The hook on start */
    var afterStart: MutableList<KFunction<Any>> = ArrayList()
    /** The hook on stop */
    var beforeStop: MutableList<KFunction<Any>> = ArrayList()
    /** The hook on error */
    var onError: MutableList<KFunction<Any>> = ArrayList()

    protected var state: BaseService.State = BaseService.State.STOPPED
        private set

    /** The service name */
    abstract var name: String

    /** Start this Service */
    fun start() = state.start(this)

    /** Immediately shuts down this Service. */
    fun stop() = state.stop(this)

    /** Shutdown this service. */
    protected abstract fun shutdown()

    /** Launch this service. */
    protected abstract fun launch()

    /** Represent State of micro-service */
    enum class State {
        /** Running application */
        STARTED {
            /** {@inheritDoc} */
            override fun start(service: BaseService) = Unit

            /** {@inheritDoc} */
            override fun stop(service: BaseService) = shutdown(service)
        },
        /** Stop application */
        STOPPED {
            /** {@inheritDoc} */
            override fun start(service: BaseService) = launch(service)

            /** {@inheritDoc} */
            override fun stop(service: BaseService) = Unit
        };

        /** Start Service */
        abstract fun start(service: BaseService)

        /** Stop Service */
        abstract fun stop(service: BaseService)

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