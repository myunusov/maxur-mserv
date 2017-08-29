package org.maxur.mserv.core

import org.glassfish.hk2.api.TypeLiteral
import org.maxur.mserv.core.kotlin.Locator
import kotlin.reflect.KClass

abstract class LocatorConfig {

    protected val descriptors = mutableListOf<Descriptor>()

    /**
     * Bind service [impl]ementation class to [contracts] or implementations.
     * @param impl The Service implementation
     * @param contracts List of contracts or implementation.
     */
    fun bind(impl: Any, vararg contracts: KClass<out Any>) {
        if (contracts.isEmpty())
            descriptors.add(Descriptor.Object(impl, impl::class))
        else
            descriptors.add(Descriptor.Object(impl, *contracts))
    }

    /**
     * Bind service [impl]ementation class to [typeLiteral].
     * @param impl The Service implementation class
     * @param typeLiteral typeLiteral.
     */
    fun bind(impl: KClass<out Any>, typeLiteral: TypeLiteral<out Any>) {
        descriptors.add(Descriptor.Literal(impl, typeLiteral))
    }

    /**
     * Bind service [impl]ementation class to [contracts] or implementations.
     * @param impl The Service implementation class
     * @param contracts List of contracts or implementation.
     */
    fun bind(impl: KClass<out Any>, vararg contracts: KClass<out Any>) {
        if (contracts.isEmpty())
            descriptors.add(Descriptor.Singleton(impl, impl::class))
        else
            descriptors.add(Descriptor.Singleton(impl, *contracts))
    }

    /**
     * Bind service creation [function] to [contracts] or implementations.
     * @param function The Service creation function
     * @param contracts List of contracts or implementation.
     */
    fun bind(function: (Locator) -> Any, vararg contracts: KClass<out Any>) {
        descriptors.add(Descriptor.Function(function, *contracts))
    }

    abstract fun bindTo(locator: LocatorImpl)

    sealed class Descriptor(vararg val contracts: KClass<out Any>) {
        class Function(val func: (Locator) -> Any, vararg contracts: KClass<out Any>) : Descriptor(*contracts)
        class Singleton(val impl: KClass<out Any>, vararg contracts: KClass<out Any>) : Descriptor(*contracts)
        class Object(val impl: Any, vararg contracts: KClass<out Any>) : Descriptor(*contracts)
        class Literal(val impl: KClass<out Any>, val literal: TypeLiteral<out Any>) : Descriptor(impl::class)
    }
}