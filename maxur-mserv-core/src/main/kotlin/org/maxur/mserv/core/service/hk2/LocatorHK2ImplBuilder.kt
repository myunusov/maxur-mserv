package org.maxur.mserv.core.service.hk2

import com.fasterxml.jackson.databind.ObjectMapper
import gov.va.oia.HK2Utilities.HK2RuntimeInitializer
import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.hk2.api.TypeLiteral
import org.glassfish.hk2.utilities.ServiceLocatorUtilities
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.maxur.mserv.core.LocatorImpl
import org.maxur.mserv.core.annotation.Value
import org.maxur.mserv.core.builder.LocatorBuilder
import org.maxur.mserv.core.kotlin.Locator
import org.maxur.mserv.core.service.jackson.ObjectMapperProvider
import javax.inject.Singleton
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

// TODO pull bind logic to Abstract class without org.glassfish.hk2.utilities.Binder
/**
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
class LocatorHK2ImplBuilder : LocatorBuilder() {

    private val descriptors: MutableList<Descriptor> = mutableListOf()

    init {
        bind(PropertiesInjectionResolver::class, object : TypeLiteral<InjectionResolver<Value>>() {})
        bind(ObjectMapperProvider::class, ObjectMapper::class)
    }

    override fun bind(locator: Locator) {
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

    override fun make(): Locator = makeServiceLocator()?.let {
        it.getService(Locator::class.java)
                ?: throw IllegalStateException(it.getService(ErrorHandler::class.java)?.latestError)
    } ?: throw IllegalStateException("Service Locator is not created.")

    private fun makeServiceLocator() = if (packages.isNotEmpty()) {
        HK2RuntimeInitializer.init(name, true, *packages.toTypedArray())
    } else {
        ServiceLocatorUtilities.createAndPopulateServiceLocator(name)
    }.also {
        ServiceLocatorUtilities.enableImmediateScope(it)
        ServiceLocatorUtilities.bind(it, LocatorBinder())
    }

    /** {@inheritDoc} */
    override fun bind(impl: KClass<out Any>, typeLiteral: TypeLiteral<out Any>) {
        descriptors.add(Descriptor.Literal(impl, typeLiteral))
    }

    /** {@inheritDoc} */
    override fun bind(impl: KClass<out Any>, vararg contracts: KClass<out Any>) {
        if (contracts.isEmpty())
            descriptors.add(Descriptor.Singleton(impl, impl::class))
        else
            descriptors.add(Descriptor.Singleton(impl, *contracts))
    }

    /** {@inheritDoc} */
    override fun bind(function: (Locator) -> Any, vararg contracts: KClass<out Any>) {
        descriptors.add(Descriptor.Function(function, *contracts))
    }

    private sealed class Descriptor(vararg val contracts: KClass<out Any>) {
        class Function(val func: (Locator) -> Any, vararg contracts: KClass<out Any>) : Descriptor(*contracts)
        class Singleton(val impl: KClass<out Any>, vararg contracts: KClass<out Any>) : Descriptor(*contracts)
        class Literal(val impl: KClass<out Any>, val literal: TypeLiteral<out Any>) : Descriptor(impl::class)
    }

    private class ServiceProvider<T>(val func: (Locator) -> T) : Factory<T> {
        val locator: Locator by lazy { Locator.current }
        val result: T by lazy { func.invoke(locator) }
        /** {@inheritDoc} */
        override fun dispose(instance: T) = Unit

        /** {@inheritDoc} */
        override fun provide(): T = result
    }

    // TODO pull up it
    class LocatorBinder : AbstractBinder() {
        /** {@inheritDoc} */
        override fun configure() {
            bind(LocatorHK2Impl::class.java).to(LocatorImpl::class.java).`in`(Singleton::class.java)
            bind(Locator::class.java).to(Locator::class.java).`in`(Singleton::class.java)
            bind(org.maxur.mserv.core.java.Locator::class.java)
                    .to(org.maxur.mserv.core.java.Locator::class.java).`in`(Singleton::class.java)
        }

    }
}