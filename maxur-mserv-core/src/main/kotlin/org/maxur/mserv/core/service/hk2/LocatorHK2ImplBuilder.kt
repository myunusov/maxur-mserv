package org.maxur.mserv.core.service.hk2

import gov.va.oia.HK2Utilities.HK2RuntimeInitializer
import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.hk2.api.TypeLiteral
import org.glassfish.hk2.utilities.ServiceLocatorUtilities
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.maxur.mserv.core.builder.LocatorBuilder
import org.maxur.mserv.core.kotlin.Locator
import javax.inject.Singleton
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
class LocatorHK2ImplBuilder(init: LocatorBuilder.Config.() -> Unit) : LocatorBuilder(init) {

    override fun makeConfig(init: LocatorBuilder.Config.() -> Unit): LocatorBuilder.Config = Config(init)

    override fun make(): Locator = Locator(LocatorHK2Impl(makeServiceLocator()))

    private fun makeServiceLocator() = if (packages.isNotEmpty()) {
        HK2RuntimeInitializer.init(name, true, *packages.toTypedArray())
    } else {
        ServiceLocatorUtilities.createAndPopulateServiceLocator(name)
    }.also {
        ServiceLocatorUtilities.enableImmediateScope(it)
    }

    class Config(init: LocatorBuilder.Config.() -> Unit) : LocatorBuilder.Config(init) {

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

        /** {@inheritDoc} */
        override fun bind(impl: Any, vararg contracts: KClass<out Any>) {
            if (contracts.isEmpty())
                descriptors.add(Descriptor.Object(impl, impl::class))
            else
                descriptors.add(Descriptor.Object(impl, *contracts))
        }

        override fun bindTo(locator: Locator) {
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
                    is Descriptor.Object -> { bind(descriptor.impl).to(it.java) }
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