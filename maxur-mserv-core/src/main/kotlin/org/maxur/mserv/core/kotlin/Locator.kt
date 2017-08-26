package org.maxur.mserv.core.kotlin

import org.maxur.mserv.core.LocatorImpl
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>26.08.2017</pre>
 */
class Locator @Inject constructor(impl: LocatorImpl) : LocatorImpl by impl {

    companion object {

        /**
         * Current Locator's property.
         */
        val current get() = Locator(LocatorImpl.holder.get())

        /**
         * Gets the best service from this locator that implements
         * this contract or has this implementation and has the given name (Kotlin edition).
         * <p>
         * @param contractOrImpl May not be null, and is the contract
         * or concrete implementation to get the best instance of
         * @param name Is the name of the implementation to be returned
         * @return An instance of the contract or impl.  May return
         * null if there is no provider that provides the given
         * implementation or contract
         */
        fun <T : Any> bean(contractOrImpl: KClass<T>, name: String? = null): T? = current.service(contractOrImpl, name)

        @Suppress("UNCHECKED_CAST")
            /**
             * Gets the best service from this locator that satisfied to function's parameter.
             * <p>
             * @param parameter The function's parameter
             * @return An instance of the contract or impl.  May return
             * null if there is no provider that provides the given
             * implementation or contract
             */
        fun <T : Any> bean(parameter: KParameter): T? = current.service(parameter.type.classifier as KClass<T>)

        /**
         * Gets all services from this locator that implement this contract or have this
         * implementation (Kotlin edition).
         * <p>
         * @param contractOrImpl May not be null, and is the contract
         * or concrete implementation to get the best instance of
         * @return A list of services implementing this contract
         * or concrete implementation.  May return an empty list
         */
        fun <T : Any> beans(contractOrImpl: KClass<T>): List<T> = current.services(contractOrImpl)

        /**
         * This method will shutdown every service associated with this Locator.
         * Those services that have a preDestroy shall have their preDestroy called
         */
        fun stop() = current.shutdown()
    }

    /**
     * Gets the best service from this locator that implements
     * this contract or has this implementation and has the given name (Kotlin edition).
     * <p>
     * @param contractOrImpl May not be null, and is the contract
     * or concrete implementation to get the best instance of
     * @param name Is the name of the implementation to be returned
     * @return An instance of the contract or impl.
     * @throw IllegalStateException if there is no provider that
     * provides the given implementation or contract
     */
    fun <T : Any> locate(contractOrImpl: KClass<T>, name: String): T = locate(contractOrImpl.java, name)

    /**
     * Gets the best service from this locator that satisfied to function's parameter.
     * <p>
     * @param parameter The function's parameter
     * @return An instance of the contract or impl.  May return
     * null if there is no provider that provides the given
     * implementation or contract
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> service(parameter: KParameter): T? = service(parameter.type.classifier as KClass<T>)

    /**
     * Gets the best service from this locator that implements
     * this contract or has this implementation and has the given name (Java edition).
     * <p>
     * @param contractOrImpl May not be null, and is the contract
     * or concrete implementation to get the best instance of
     * @param name Is the name of the implementation to be returned
     * @return An instance of the contract or impl.  May return
     * null if there is no provider that provides the given
     * implementation or contract
     */
    fun <T : Any> service(contractOrImpl: KClass<T>, name: String? = null): T? = service(contractOrImpl.java, name)

    /**
     * Gets all services from this locator that implement this contract or have this
     * implementation (Java edition).
     * <p>
     * @param contractOrImpl May not be null, and is the contract
     * or concrete implementation to get the best instance of
     * @return A list of services implementing this contract
     * or concrete implementation.  May return an empty list
     */
    fun <T : Any> services(contractOrImpl: KClass<T>): List<T> = services(contractOrImpl.java)

    /**
     * Gets all services names from this locator that implement this contract or have this
     * implementation (Kotlin edition).
     * <p>
     * @param contractOrImpl May not be null, and is the contract
     * or concrete implementation to get the best instance of
     * @return A list of services names implementing this contract
     * or concrete implementation.  May return an empty list
     */
    fun <T : Any> names(contractOrImpl: KClass<T>): List<String> = names(contractOrImpl.java)

    /**
     * Gets property value by key name.
     * <p>
     * @param key The key name
     * @return The property value as String or nul
     */
    fun property(key: String): String? = property(key, String::class)

    /**
     * Gets property value by key name (Kotlin edition).
     * <p>
     * @param key The key name
     * @param clazz The required type.
     * @return The property value of required type or nul
     */
    fun <T : Any> property(key: String, clazz: KClass<T>): T? = property(key, clazz.java)

    /** {@inheritDoc} */
    override fun toString(): String = name

    /** {@inheritDoc} */
    override fun equals(other: Any?): Boolean = other is Locator && other.name.equals(name)

}