package org.maxur.mserv.core

/**
 * Nul Object for Locator.
 * Throw IllegalStateException on any call.
 */
object NullLocator : LocatorImpl {

    /** {@inheritDoc} */
    override val name: String = "null-locator"

    /** {@inheritDoc} */
    override fun configurationError() = null

    /** {@inheritDoc} */
    override fun <T> service(contractOrImpl: Class<T>, name: String?): T? = error()

    /** {@inheritDoc} */
    override fun <T> services(contractOrImpl: Class<T>): List<T> = error()

    /** {@inheritDoc} */
    override fun names(contractOrImpl: Class<*>): List<String> = error()

    /** {@inheritDoc} */
    override fun <R> property(key: String, clazz: Class<R>): R? = error()

    /** {@inheritDoc} */
    override fun close() = Unit

    /** {@inheritDoc} */
    override fun <T> implementation(): T = error()

    private fun <T> error(): T =
            throw IllegalStateException("Service Locator is not initialized.")
}