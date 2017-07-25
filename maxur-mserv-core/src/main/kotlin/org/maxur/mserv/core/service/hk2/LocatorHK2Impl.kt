package org.maxur.mserv.core.service.hk2

import org.glassfish.hk2.api.ServiceLocator
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.service.properties.PropertiesSource
import javax.inject.Inject


class LocatorHK2Impl @Inject constructor(val locator: ServiceLocator) : Locator {

    init {
        Locator.current = this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> implementation(): T = locator as T

    @Suppress("UNCHECKED_CAST")
    override fun <T> services(clazz: Class<*>): List<T> =
        locator.getAllServices(clazz).map { it as T}

    override fun names(clazz: Class<*>): List<String> =
        locator.getAllServiceHandles(clazz).map { it.activeDescriptor.name }

    override fun property(key: String): String = locator.getService(PropertiesSource::class.java).asString(key)!!

    override fun <R> properties(key: String, clazz: Class<R>): R? {
        return locator.getService(PropertiesSource::class.java).read(key, clazz)
    }

    override fun <T> service(clazz: Class<T>): T? = locator.getService<T>(clazz)

    override fun <T> service(clazz: Class<T>, name: String?): T? =
            when (name) {
                null -> locator.getService<T>(clazz)
                else -> locator.getAllServiceHandles(clazz)
                        .filter { it.activeDescriptor.name.equals(name, true) }
                        .map { it.service }
                        .firstOrNull()
            }

    override fun shutdown() = locator.shutdown()

}





