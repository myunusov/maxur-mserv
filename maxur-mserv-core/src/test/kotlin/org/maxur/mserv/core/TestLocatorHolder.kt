package org.maxur.mserv.core

object TestLocatorHolder : Locator.LocatorHolder {
    private var locator: ThreadLocal<Locator> = ThreadLocal()
    override fun get() = locator.get() ?: Locator.NullLocator
    override fun put(value: Locator) = locator.set(value)
    override fun remove(name: String) = locator.set(null)
}
