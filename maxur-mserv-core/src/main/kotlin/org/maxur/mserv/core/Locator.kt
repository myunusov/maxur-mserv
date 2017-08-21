package org.maxur.mserv.core

import javax.annotation.PostConstruct
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

/**
 * Locator is the registry for services.
 * <p>
 * @param name The Name of this Locator.
 */
@Suppress("UNCHECKED_CAST")
abstract class Locator(val name: String) {

    /**
     * Nul Object for Locator.
     * Throw IllegalStateException on any call.
     */
    object NullLocator : Locator("null-locator") {
        /** {@inheritDoc} */
        override fun <T> service(contractOrImpl: Class<T>, name: String?): T? = error()

        /** {@inheritDoc} */
        override fun <T> services(contractOrImpl: Class<T>): List<T> = error()

        /** {@inheritDoc} */
        override fun names(contractOrImpl: Class<*>): List<String> = error()

        /** {@inheritDoc} */
        override fun <R> property(key: String, clazz: Class<R>): R? = error()

        /** {@inheritDoc} */
        override fun close() = error<Unit>()

        /** {@inheritDoc} */
        override fun <T> implementation(): T = error()

        private fun <T> error(): T =
            throw IllegalStateException("Service Locator is not initialized.")
    }

    /**
     * Holder for Locator.
     */
    interface LocatorHolder {
        /**
         * Returns a Locator.
         * @return the Locator.
         */
        fun get(): Locator

        /**
         * Put the Locator to holder.
         * @param value The Locator.
         */
        fun put(value: Locator)

        /**
         * Remove The Locator from Holder by it's name.
         * @param name The name of the Locator.
         */
        fun remove(name: String)
    }

    protected class SingleHolder : LocatorHolder {
        private var locator: Locator = NullLocator

        override fun get() = locator

        override fun put(value: Locator) {
            locator = if (locator is NullLocator)
                value
            else
                throw IllegalStateException("You can have only one service locator per microservice")
        }

        override fun remove(name: String) {
            locator = if (locator.name == name)
                NullLocator
            else
                throw IllegalArgumentException("Locator '$name' is not found")
        }
    }

    companion object {
        /**
         * Locator Holder.
         */
        var holder: LocatorHolder = SingleHolder()

        /**
         * Current Locator's property.
         */
        var current: Locator
            get() = holder.get()
            set(value) = holder.put(value)

        private fun removeLocator(name: String) {
            holder.remove(name)
        }

        /**
         * Gets the best service from this locator that implements
         * this contract or has this implementation (Java edition).
         * <p>
         * @param contractOrImpl May not be null, and is the contract
         * or concrete implementation to get the best instance of
         * @return An instance of the contract or impl.  May return
         * null if there is no provider that provides the given
         * implementation or contract
         */
        fun <T> service(contractOrImpl: Class<T>): T? = current.service(contractOrImpl)

        /**
         * Gets the best service from this locator that implements
         * this contract or has this implementation (Kotlin edition).
         * <p>
         * @param contractOrImpl May not be null, and is the contract
         * or concrete implementation to get the best instance of
         * @return An instance of the contract or impl.  May return
         * null if there is no provider that provides the given
         * implementation or contract
         */
        fun <T : Any> service(contractOrImpl: KClass<T>): T? = current.service(contractOrImpl)

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
        fun <T> service(contractOrImpl: Class<T>, name: String): T? = current.service(contractOrImpl, name)

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
        fun <T : Any> service(contractOrImpl: KClass<T>, name: String): T? = current.service(contractOrImpl, name)

        /**
         * Gets the best service from this locator that satisfied to function's parameter.
         * <p>
         * @param parameter The function's parameter
         * @return An instance of the contract or impl.  May return
         * null if there is no provider that provides the given
         * implementation or contract
         */
        fun <T : Any> service(parameter: KParameter): T? = current.service(parameter)

        /**
         * Gets all services from this locator that implement this contract or have this
         * implementation (Java edition).
         * <p>
         * @param contractOrImpl May not be null, and is the contract
         * or concrete implementation to get the best instance of
         * @return A list of services implementing this contract
         * or concrete implementation.  May return an empty list
         */
        fun <T> services(contractOrImpl: Class<T>): List<T> = current.services(contractOrImpl)

        /**
         * Gets all services from this locator that implement this contract or have this
         * implementation (Kotlin edition).
         * <p>
         * @param contractOrImpl May not be null, and is the contract
         * or concrete implementation to get the best instance of
         * @return A list of services implementing this contract
         * or concrete implementation.  May return an empty list
         */
        fun <T : Any> services(contractOrImpl: KClass<T>): List<T> = current.services(contractOrImpl)

        /**
         * This method will shutdown every service associated with this Locator.
         * Those services that have a preDestroy shall have their preDestroy called
         */
        fun shutdown() = if (current !is NullLocator) current.shutdown() else Unit
    }

    @PostConstruct
    private fun init() {
        Locator.current = this
    }

    /**
     * Gets the best service from this locator that implements
     * this contract or has this implementation and has the given name (Java edition).
     * <p>
     * @param contractOrImpl May not be null, and is the contract
     * or concrete implementation to get the best instance of
     * @param name Is the name of the implementation to be returned
     * @return An instance of the contract or impl.
     * @throw IllegalStateException if there is no provider that
     * provides the given implementation or contract
     */
    fun <T> locate(contractOrImpl: Class<T>, name: String): T = service(contractOrImpl, name) ?:
        throw IllegalStateException(
            "Service '$name' is not supported. Try one from this list: ${names(contractOrImpl)}"
        )

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
    fun <T : Any> service(parameter: KParameter): T? = service(parameter.type.classifier as KClass<T>)

    /**
     * Gets the best service from this locator that implements
     * this contract or has this implementation and has the given name (Java edition).
     * <p>
     * @param contractOrImpl May not be null, and is the contract
     * or concrete implementation to get the best instance of
     * @return An instance of the contract or impl.  May return
     * null if there is no provider that provides the given
     * implementation or contract
     */
    fun <T> service(contractOrImpl: Class<T>): T? = service(contractOrImpl, null)

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
    abstract fun <T> service(contractOrImpl: Class<T>, name: String?): T?

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
     * Gets all services from this locator that implement this contract or have this
     * implementation (Kotlin edition).
     * <p>
     * @param contractOrImpl May not be null, and is the contract
     * or concrete implementation to get the best instance of
     * @return A list of services implementing this contract
     * or concrete implementation.  May return an empty list
     */
    abstract fun <T> services(contractOrImpl: Class<T>): List<T>

    /**
     * Gets all services names from this locator that implement this contract or have this
     * implementation (Java edition).
     * <p>
     * @param contractOrImpl May not be null, and is the contract
     * or concrete implementation to get the best instance of
     * @return A list of services names implementing this contract
     * or concrete implementation.  May return an empty list
     */
    abstract fun names(contractOrImpl: Class<*>): List<String>

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
     * Gets property value by key name (Java edition).
     * <p>
     * @param key The key name
     * @param clazz The required type.
     * @return The property value of required type or nul
     */
    abstract fun <T> property(key: String, clazz: Class<T>): T?

    /**
     * Gets property value by key name (Kotlin edition).
     * <p>
     * @param key The key name
     * @param clazz The required type.
     * @return The property value of required type or nul
     */
    fun <T : Any> property(key: String, clazz: KClass<T>): T? = property(key, clazz.java)

    /**
     * Returns platforms implementation of Locator.
     * @return The platforms implementation of Locator.
     */
    abstract fun <T> implementation(): T

    /**
     * This method will shutdown every service associated with this Locator.
     * Those services that have a preDestroy shall have their preDestroy called
     */
    fun shutdown() {
        removeLocator(this.name)
        close()
    }

    protected abstract fun close()

    /** {@inheritDoc} */
    override fun toString(): String = name

}

