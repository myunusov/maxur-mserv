package org.maxur.mserv.core.service.hk2

import gov.va.oia.HK2Utilities.HK2RuntimeInitializer
import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.hk2.api.ServiceLocatorState
import org.glassfish.hk2.utilities.ServiceLocatorUtilities
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.hk2.utilities.binding.ScopedBindingBuilder
import org.glassfish.hk2.utilities.binding.ServiceBindingBuilder
import org.maxur.mserv.core.LocatorConfig
import org.maxur.mserv.core.LocatorImpl
import org.maxur.mserv.core.core.Either
import org.maxur.mserv.core.core.fold
import org.maxur.mserv.core.core.left
import org.maxur.mserv.core.core.result
import org.maxur.mserv.core.core.right
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
            else -> locator.getService(contractOrImpl, name)
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

    override fun config(): org.maxur.mserv.core.LocatorConfig = Config(this)
    
    class Config(locatorImpl: LocatorImpl) : LocatorConfig(locatorImpl) {

        override fun bind(impl: Any): Descriptor {
            return super.bind(impl)
        }

        override fun apply() {
            val binder = object : AbstractBinder() {
                override fun configure() {
                    descriptors.forEach { makeBinders(it) }
                }
            }
            ServiceLocatorUtilities.bind(locator.implementation<ServiceLocator>(), binder)
        }

        private fun AbstractBinder.makeBinders(descriptor: Descriptor) {
            val contract = descriptor.contract
            builder(descriptor).fold({
                val builder = it
                when (contract) {
                    is ContractSet -> contract.contracts.forEach { builder.to(it.java) }
                    is ContractSelf -> builder.to(contract.contract.java)
                    is ContractTypeLiteral -> builder.to(contract.literal)
                    is ContractNone -> throw IllegalStateException("Contract must be")
                }
                descriptor.name ?.let { builder.named(it) }
                builder.`in`(Singleton::class.java)
            }, {
                val builder = it
                when (contract) {
                    is ContractSet -> contract.contracts.forEach { builder.to(it.java) }
                    is ContractSelf -> builder.to(contract.contract.java)
                    is ContractTypeLiteral -> builder.to(contract.literal)
                    is ContractNone -> throw IllegalStateException("Contract must be")
                }
                descriptor.name ?.let { builder.named(it) }
            })
        }

        private fun AbstractBinder.builder(descriptor: Descriptor)
                : Either<ServiceBindingBuilder<out Any>, ScopedBindingBuilder<out Any>> = when (descriptor) {
            is DescriptorFunction -> left(bindFactory(ServiceProvider(locator, descriptor.func)))
            is DescriptorObject -> {
                right(bind(descriptor.impl))
            }
            is DescriptorSingleton -> {
                if (descriptor.impl.isSubclassOf(Factory::class)) {
                    @Suppress("UNCHECKED_CAST")
                    val factoryClass: KClass<Factory<Any>> = descriptor.impl as KClass<Factory<Any>>
                    left(bindFactory(factoryClass.java))
                } else
                    left(bind(descriptor.impl.java))
            }
            else -> throw IllegalStateException("Unknown description")
        }


        private class ServiceProvider<T>(val locator: LocatorImpl, val func: (Locator) -> T) : Factory<T> {
            val result: T by lazy { func.invoke(Locator(locator)) }
            /** {@inheritDoc} */
            override fun dispose(instance: T) = Unit

            /** {@inheritDoc} */
            override fun provide(): T = result
        }
    }

}
