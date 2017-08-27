package org.maxur.mserv.core

object TestLocatorHolder : LocatorHolder {
    private var locator: ThreadLocal<LocatorImpl> = ThreadLocal()
    override fun get() = locator.get() ?: NullLocator
    override fun put(value: LocatorImpl) = locator.set(value)
    override fun remove(name: String) = locator.set(null)
}
