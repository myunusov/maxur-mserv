package org.maxur.mserv.frame.java

import org.maxur.mserv.frame.LocatorImpl
import javax.inject.Inject

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>26.08.2017</pre>
 */
class Locator @Inject constructor(impl: LocatorImpl) : LocatorImpl by impl {

    companion object {

        /**
         * Get active Instance of Locator Service.
         */
        @JvmStatic
        val instance
            get() = Locator(LocatorImpl.holder.get())

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
        @JvmStatic
        fun <T> bean(contractOrImpl: Class<T>): T? = instance.service(contractOrImpl)

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
        @JvmStatic
        fun <T> bean(contractOrImpl: Class<T>, name: String): T? = instance.service(contractOrImpl, name)

        /**
         * Gets all services from this locator that implement this contract or have this
         * implementation (Java edition).
         * <p>
         * @param contractOrImpl May not be null, and is the contract
         * or concrete implementation to get the best instance of
         * @return A list of services implementing this contract
         * or concrete implementation.  May return an empty list
         */
        @JvmStatic
        fun <T> beans(contractOrImpl: Class<T>): List<T> = instance.services(contractOrImpl)

        /**
         * This method will shutdown every service associated with this Locator.
         * Those services that have a preDestroy shall have their preDestroy called
         */
        @JvmStatic
        fun stop() = instance.shutdown()
    }

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
     * Gets property value by key name.
     * <p>
     * @param key The key name
     * @return The property value as String or nul
     */
    fun property(key: String): String? = property(key, String::class.java)

    /** {@inheritDoc} */
    override fun toString(): String = name

    /** {@inheritDoc} */
    override fun equals(other: Any?): Boolean = other is org.maxur.mserv.frame.kotlin.Locator && other.name.equals(name)
}