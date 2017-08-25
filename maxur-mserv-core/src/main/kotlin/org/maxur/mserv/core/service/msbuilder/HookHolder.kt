package org.maxur.mserv.core.service.msbuilder

import org.maxur.mserv.core.domain.BaseService
import kotlin.reflect.KFunction

/**
 * This class is holder of hooks functions.
 *
 * @param T the type of hook function argument
 * @property list the list of hooks.
 * @constructor Creates an empty holder.
 */
abstract class HookHolder<out T : Any>(val list: MutableList<KFunction<Any>> = ArrayList()) {

    /**
     * companion object.
     */
    companion object {
        /**
         * Create hook holder with Base Service as argument
         */
        fun onService(): HookHolder<BaseService> = BaseHookHolder()

        /**
         * Create hook holder with Exception as argument
         */
        fun onError(): HookHolder<Exception> = ErrorHookHolder()
    }

    /**
     * Add function as KFunction.
     * For example (kotlin)
     * <code>
     *     afterStart += this@MicroServiceIT::afterStartKt
     * </code>
     * @param function The hook function.
     *
     */
    operator fun plusAssign(function: KFunction<Any>) {
        list.add(function)
    }

    /**
     * Add function as lambda.
     * For example (kotlin)
     * <code>
     *     beforeStop += { _ ->  log().info("Microservice is stopped") }
     * </code>
     * For example (java)
     * <code>
     *     beforeStop.plusAssign(unitFunc(func))
     * </code>
     * @param lambda The hook lambda.
     */
    abstract operator fun plusAssign(lambda: (T) -> Unit)
}

/**
 * This class is holder of hooks functions.
 */
private class BaseHookHolder : HookHolder<BaseService>() {

    /** {@inheritDoc} */
    override operator fun plusAssign(lambda: (BaseService) -> Unit) {
        val observer = object {
            /**
             * Invoke lambda.
             * @param arg function's arg
             */
            fun invoke(arg: BaseService) = lambda.invoke(arg)
        }
        list.add(observer::invoke)
    }
}

/**
 * This class is holder of hooks functions.
 */
private class ErrorHookHolder : HookHolder<Exception>() {

    /** {@inheritDoc} */
    override operator fun plusAssign(lambda: (Exception) -> Unit) {
        val observer = object {
            /**
             * Invoke lambda.
             * @param arg function's  arg
             */
            fun invoke(arg: Exception) = lambda.invoke(arg)
        }
        list.add(observer::invoke)
    }
}