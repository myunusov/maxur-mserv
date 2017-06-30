package org.maxur.mserv.core.service.hk2

import org.glassfish.hk2.api.ServiceLocator
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.service.properties.PropertiesService
import javax.inject.Inject


class LocatorHK2Impl @Inject constructor(val locator: ServiceLocator) : Locator {

    companion object {
        lateinit var  current: Locator
            private set
    }

    init {
        current = this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> implementation(): T = locator as T

    override fun property(key: String): String = locator.getService(PropertiesService::class.java).asString(key)!!

    override fun <R> properties(key: String, clazz: Class<R>): R? {
        return locator.getService(PropertiesService::class.java).read(key, clazz)
    }

    override fun <T> service(clazz: Class<T>): T? = locator.getService<T>(clazz)

    override fun <T> service(clazz: Class<T>, name: String?): T? =
            if (name == null) {
                locator.getService<T>(clazz)
            } else {
                locator.getService<T>(clazz, name)
            }

    override fun names(clazz: Class<*>): List<String> =
            locator.getAllServiceHandles(clazz).map({ it.activeDescriptor.name })




}





