package org.maxur.mserv.frame

object TestLocatorHolder : LocatorHolder {
    private var locator: ThreadLocal<LocatorImpl> = ThreadLocal()
    override fun get() = locator.get() ?: NullLocator
    override fun put(value: LocatorImpl) = locator.set(value)
    override fun remove(name: String) = locator.set(null)
}
