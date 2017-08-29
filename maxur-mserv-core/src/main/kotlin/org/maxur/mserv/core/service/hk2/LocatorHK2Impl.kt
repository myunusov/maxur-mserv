package org.maxur.mserv.core.service.hk2

import gov.va.oia.HK2Utilities.HK2RuntimeInitializer
import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.hk2.api.ServiceLocatorState
import org.glassfish.hk2.utilities.ServiceLocatorUtilities
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.maxur.mserv.core.LocatorConfig
import org.maxur.mserv.core.LocatorImpl
import org.maxur.mserv.core.core.result
import org.maxur.mserv.core.core.tryTo
import org.maxur.mserv.core.kotlin.Locator
import org.maxur.mserv.core.service.properties.Properties
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * Locator is the registry for services. The HK2 ServiceLocator adapter.
 * <p>
 * @property name The locator name
 * @param packages The set of package names to scan recursively.
 */
class LocatorHK2Impl @Inject constructor(override val name: String, packages: Set<String> = emptySet()) : LocatorImpl {

    private val locator: ServiceLocator by lazy {
        if (packages.isNotEmpty()) {
            HK2RuntimeInitializer.init(name, true, *packages.toTypedArray())
        } else {
            ServiceLocatorUtilities.createAndPopulateServiceLocator(name)
        }.also {
            ServiceLocatorUtilities.enableImmediateScope(it)
        }
    }

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
    override fun <T> service(contractOrImpl: Class<T>, name: String?): T? = tryTo {
        when (name) {
            null -> locator.getService<T>(contractOrImpl)
            else -> locator.getAllServiceHandles(contractOrImpl)
                    .filter { it.activeDescriptor.name.equals(name, true) }
                    .map { it.service }
                    .firstOrNull()
        }
    }.result()

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

    override fun config(): org.maxur.mserv.core.LocatorConfig = Config()

    class Config : LocatorConfig() {

        override fun bindTo(locator: LocatorImpl) {
            val binder = object : AbstractBinder() {
                override fun configure() {
                    descriptors.forEach { makeBinders(it) }
                }
            }
            ServiceLocatorUtilities.bind(locator.implementation<ServiceLocator>(), binder)
        }

        private fun AbstractBinder.makeBinders(descriptor: Descriptor) {
            descriptor.contracts.forEach {
                when (descriptor) {
                    is Descriptor.Function -> bindFactory(ServiceProvider(descriptor.func)).to(it.java)
                    is Descriptor.Literal -> {
                        bind(descriptor.impl.java).to(descriptor.literal).`in`(Singleton::class.java)
                    }
                    is Descriptor.Object -> {
                        bind(descriptor.impl).to(it.java)
                    }
                    is Descriptor.Singleton -> {
                        if (descriptor.impl.isSubclassOf(Factory::class)) {
                            @Suppress("UNCHECKED_CAST")
                            val factoryClass: KClass<Factory<Any>> = descriptor.impl as KClass<Factory<Any>>
                            bindFactory(factoryClass.java).to(it.java).`in`(Singleton::class.java)
                        } else
                            bind(descriptor.impl.java).to(it.java).`in`(Singleton::class.java)
                    }
                }
            }
        }

        private class ServiceProvider<T>(val func: (Locator) -> T) : Factory<T> {
            val locator: Locator by lazy { Locator.current }
            val result: T by lazy { func.invoke(locator) }
            /** {@inheritDoc} */
            override fun dispose(instance: T) = Unit

            /** {@inheritDoc} */
            override fun provide(): T = result
        }
    }

}
