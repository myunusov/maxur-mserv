package org.maxur.mserv.core.service.hk2

import org.glassfish.hk2.api.ServiceLocator
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.service.properties.Properties
import javax.inject.Inject

class LocatorHK2Impl @Inject constructor(val locator: ServiceLocator) : Locator {

    @Suppress("UNCHECKED_CAST")
    override fun <T> implementation(): T = locator as T

    override fun names(clazz: Class<*>): List<String> =
            locator.getAllServiceHandles(clazz).map { it.activeDescriptor.name }

    override fun <T> property(key: String, clazz: Class<T>): T? =
            locator.getService(Properties::class.java).read(key, clazz)

    override fun <T> service(clazz: Class<T>, name: String?): T? =
            when (name) {
                null -> locator.getService<T>(clazz)
                else -> locator.getAllServiceHandles(clazz)
                        .filter { it.activeDescriptor.name.equals(name, true) }
                        .map { it.service }
                        .firstOrNull()
            }

    override fun <T> services(clazz: Class<T>): List<T> = locator.getAllServices(clazz).map { it as T }

    override fun shutdown() = locator.shutdown()

}
