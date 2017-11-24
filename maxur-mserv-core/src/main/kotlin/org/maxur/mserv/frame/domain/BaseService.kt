package org.maxur.mserv.frame.domain

import org.maxur.mserv.core.command.Event
import org.maxur.mserv.frame.event.MicroserviceFailedEvent
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
    protected abstract fun shutdown(): List<Event>

    /** Launch this service. */
    protected abstract fun launch(): List<Event>

    /** Represent State of micro-service */
    enum class State {
        /** Running application */
        STARTED {
            /** {@inheritDoc} */
            override fun start(service: BaseService) = emptyList<Event>()

            /** {@inheritDoc} */
            override fun stop(service: BaseService) = shutdown(service)
        },
        /** Stop application */
        STOPPED {
            /** {@inheritDoc} */
            override fun start(service: BaseService) = launch(service)

            /** {@inheritDoc} */
            override fun stop(service: BaseService) = emptyList<Event>()
        };

        /** Start Service */
        abstract fun start(service: BaseService): List<Event>

        /** Stop Service */
        abstract fun stop(service: BaseService): List<Event>

        protected fun shutdown(service: BaseService): List<Event> = try {
            service.beforeStop.forEach { call(it, service) }
            val events = service.shutdown()
            service.state = STOPPED
            events
        } catch (e: Exception) {
            handleError(service, e)
        }

        protected fun launch(service: BaseService): List<Event> = try {
            val events = service.launch()
            service.state = STARTED
            service.afterStart.forEach { call(it, service) }
            events
        } catch (e: Exception) {
            handleError(service, e)
        }

        private fun handleError(service: BaseService, error: Exception): List<Event> {
            if (service.onError.isEmpty())
                throw error
            else
                service.onError.forEach { call(it, service, error) }
            return listOf(MicroserviceFailedEvent())
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