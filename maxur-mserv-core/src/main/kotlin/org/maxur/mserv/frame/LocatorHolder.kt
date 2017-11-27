package org.maxur.mserv.frame

/**
 * Holder for Locator.
 */
interface LocatorHolder {
    /**
     * Returns a Locator.
     * @return the Locator.
     */
    fun get(): LocatorImpl

    /**
     * Put the Locator to holder.
     * @param value The Locator.
     */
    fun put(value: LocatorImpl)

    /**
     * Remove The Locator from Holder by it's name.
     * @param name The name of the Locator.
     */
    fun remove(name: String)
}

internal class SingleHolder : LocatorHolder {

    private var locator: LocatorImpl = NullLocator

    override fun get() = locator

    override fun put(value: LocatorImpl) {
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
