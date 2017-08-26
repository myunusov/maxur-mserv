package org.maxur.mserv.core

/**
 * Locator is the registry for services.
 * <p>
 * @param name The Name of this Locator.
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseLocator(val impl: LocatorImpl = NullLocator) : LocatorImpl by impl {

    /** {@inheritDoc} */
    override fun toString(): String = name
}

