package org.maxur.mserv.core.service.hk2

import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.hk2.api.ServiceLocatorState
import org.maxur.mserv.core.LocatorImpl
import org.maxur.mserv.core.service.properties.Properties
import javax.inject.Inject

/**
 * Locator is the registry for services. The HK2 ServiceLocator adapter.
 * <p>
 * @param locator The HK2 ServiceLocator.
 */
class LocatorHK2Impl @Inject constructor(private val locator: ServiceLocator) : LocatorImpl {

    override val name: String = locator.name

    /** {@inheritDoc} */
    @Suppress("UNCHECKED_CAST")
    override fun <T> implementation(): T = locator as T

    /** {@inheritDoc} */
    override fun names(contractOrImpl: Class<*>): List<String> =
        locator.getAllServiceHandles(contractOrImpl).map { it.activeDescriptor.name }

    /** {@inheritDoc} */
    override fun <T> property(key: String, clazz: Class<T>): T? =
        locator.getService(Properties::class.java).read(key, clazz)

    /** {@inheritDoc} */
    override fun <T> service(contractOrImpl: Class<T>, name: String?): T? =
        when (name) {
            null -> locator.getService<T>(contractOrImpl)
            else -> locator.getAllServiceHandles(contractOrImpl)
                .filter { it.activeDescriptor.name.equals(name, true) }
                .map { it.service }
                .firstOrNull()
        }

    /** {@inheritDoc} */
    override fun <T> services(contractOrImpl: Class<T>): List<T> =
        locator.getAllServices(contractOrImpl).map { it as T }

    /** {@inheritDoc} */
    override fun configurationError() = service(ErrorHandler::class.java)?.latestError

    /** {@inheritDoc} */
    override fun close() {
        if (locator.state == ServiceLocatorState.RUNNING)
            locator.shutdown()
    }

}
