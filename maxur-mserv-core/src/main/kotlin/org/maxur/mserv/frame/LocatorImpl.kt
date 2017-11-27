package org.maxur.mserv.frame

import org.maxur.mserv.frame.kotlin.Locator

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>26.08.2017</pre>
 */
interface LocatorImpl {

    companion object {
        /**
         *  The Locator holder
         */
        var holder: LocatorHolder = SingleHolder()
    }

    /**
     * This method will shutdown every service associated with this Locator.
     * Those services that have a preDestroy shall have their preDestroy called
     */
    fun shutdown() {
        holder.remove(this.name)
        close()
    }

    /**
     * Calls on configuration error.
     * @param error The cause of error (optional).
     */
    fun <T> onConfigurationError(error: Exception? = null): T {
        val errorMessage =
            configurationError()?.message
                ?: error?.message
                ?: "Unknown error"
        shutdown()
        throw IllegalStateException("A MicroService is not created. $errorMessage")
    }

    /**
     * The locator's name.
     */
    val name: String

    /**
     * This will analyze the given object and inject into its fields and methods.
     * The object injected in this way will not be managed by HK2
     *
     * @param injectMe The object to be analyzed and injected into
     */
    fun inject(injectMe: Any)

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
    fun <T> service(contractOrImpl: Class<T>, name: String? = null): T?

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
    fun <T> locate(contractOrImpl: Class<T>, name: String?): T = service(contractOrImpl, name) ?:
        throw IllegalStateException(
            "Service '$name' is not supported. Try one from this list: ${names(contractOrImpl)}"
        )

    /**
     * Gets all services from this locator that implement this contract or have this
     * implementation (Kotlin edition).
     * <p>
     * @param contractOrImpl May not be null, and is the contract
     * or concrete implementation to get the best instance of
     * @return A list of services implementing this contract
     * or concrete implementation.  May return an empty list
     */
    fun <T> services(contractOrImpl: Class<T>): List<T>

    /**
     * Gets all services names from this locator that implement this contract or have this
     * implementation (Java edition).
     * <p>
     * @param contractOrImpl May not be null, and is the contract
     * or concrete implementation to get the best instance of
     * @return A list of services names implementing this contract
     * or concrete implementation.  May return an empty list
     */
    fun names(contractOrImpl: Class<*>): List<String>

    /**
     * Gets property value by key name (Java edition).
     * <p>
     * @param key The key name
     * @param clazz The required type.
     * @return The property value of required type or nul
     */
    fun <T> property(key: String, clazz: Class<T>): T?

    /**
     * Returns platforms implementation of Locator.
     * @return The platforms implementation of Locator.
     */
    fun <T> implementation(): T

    /**
     *  Returns last configuration error.
     *  @return last configuration error as Exception
     */
    fun configurationError(): Exception?

    /**
     * Close locator.
     */
    fun close()

    /**
     * Register Locator Implementation as Singleton.
     */
    fun registerAsSingleton() = holder.put(this)

    fun configure(function: LocatorConfig.() -> Unit) =
        Locator.current.let {
            config().apply {
                function()
                apply()
            }
        }

    fun config(): LocatorConfig
}