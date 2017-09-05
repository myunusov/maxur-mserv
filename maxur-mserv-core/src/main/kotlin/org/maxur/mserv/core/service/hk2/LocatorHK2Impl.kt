package org.maxur.mserv.core.service.hk2

import gov.va.oia.HK2Utilities.HK2RuntimeInitializer
import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.api.MultiException
import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.hk2.api.ServiceLocatorState
import org.glassfish.hk2.api.TypeLiteral
import org.glassfish.hk2.utilities.ServiceLocatorUtilities
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.hk2.utilities.binding.ScopedBindingBuilder
import org.glassfish.hk2.utilities.binding.ServiceBindingBuilder
import org.maxur.mserv.core.LocatorConfig
import org.maxur.mserv.core.LocatorImpl
import org.maxur.mserv.core.core.Either
import org.maxur.mserv.core.core.ErrorResult
import org.maxur.mserv.core.core.Result
import org.maxur.mserv.core.core.Value
import org.maxur.mserv.core.core.fold
import org.maxur.mserv.core.core.left
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

    /**
     * Return result or throws IllegalStateException
     * @return result
     */
    fun <E : Throwable, V> Result<E, V>.result(): V = when (this) {
        is Value -> value
        is ErrorResult -> throw convertError(error)
    }

    private fun convertError(error: Throwable): IllegalStateException = when (error) {
        is IllegalStateException -> error
        is MultiException ->
            if (error.errors.size == 1)
                convertError(error.errors[0])
            else
                IllegalStateException(error)
        else -> IllegalStateException(error)
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

    override fun config(): org.maxur.mserv.core.LocatorConfig = Config(this)

    class Config(locatorImpl: LocatorImpl) : LocatorConfig(locatorImpl) {

        override fun <T : Any> makeDescriptor(bean: Bean<T>, contract: Contract<T>?): Descriptor<T> =
                object : Descriptor<T>(bean, contract?.let { mutableSetOf(contract) } ?: mutableSetOf()) {
                    @Suppress("UNCHECKED_CAST")
                    override fun toSpecificContract(contract: Any) {
                        if (contract is TypeLiteral<*>)
                            contracts.add(ContractTypeLiteral(contract as TypeLiteral<in T>))
                    }
                }

        override fun apply() {
            val binder = object : AbstractBinder() {
                override fun configure() {
                    descriptors.forEach { makeBinders(it) }
                }
            }
            ServiceLocatorUtilities.bind(locator.implementation<ServiceLocator>(), binder)
        }

        private fun AbstractBinder.makeBinders(descriptor: Descriptor<out Any>) {
            if (descriptor.contracts.isEmpty()) {
                throw IllegalStateException("Contract must be")
            }
            descriptor.contracts.forEach {
                val contract = it
                builder(descriptor).fold({
                    val builder = it
                    when (contract) {
                        is ContractClass -> builder.to(contract.contract.java)
                        is ContractTypeLiteral -> builder.to(contract.literal)
                    }
                    descriptor.name?.let { builder.named(it) }
                    builder.`in`(Singleton::class.java)
                }, {
                    val builder = it
                    when (contract) {
                        is ContractClass -> builder.to(contract.contract.java)
                        is ContractTypeLiteral -> builder.to(contract.literal)
                    }
                    descriptor.name?.let { builder.named(it) }
                })
            }
        }

        private fun <T : Any> AbstractBinder.builder(descriptor: Descriptor<T>)
                : Either<ServiceBindingBuilder<in T>, ScopedBindingBuilder<in T>> {
            val bean = descriptor.bean
            return when (bean) {
                is BeanFunction -> left(bindFactory(ServiceProvider(locator, bean.func)))
                is BeanObject -> {
                    right(bind(bean.impl))
                }
                is BeanSingleton<T> -> {
                    if (bean.impl.isSubclassOf(Factory::class)) {
                        @Suppress("UNCHECKED_CAST")
                        val factoryClass: KClass<Factory<T>> = bean.impl as KClass<Factory<T>>
                        left(bindFactory(factoryClass.java))
                    } else
                        left(bind(bean.impl.java))
                }
                else -> throw IllegalStateException("Unknown description")
            }
        }

        private class ServiceProvider<T>(val locator: LocatorImpl, val func: (Locator) -> T) : Factory<T> {
            val result: T by lazy { func.invoke(Locator(locator)) }
            /** {@inheritDoc} */
            override fun dispose(instance: T) = Unit

            /** {@inheritDoc} */
            override fun provide(): T = result
        }

        class ContractTypeLiteral<T : Any>(val literal: org.glassfish.hk2.api.TypeLiteral<in T>) : Contract<T>()
    }

}
