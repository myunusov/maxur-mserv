package org.maxur.mserv.core

import org.glassfish.hk2.api.TypeLiteral
import org.maxur.mserv.core.kotlin.Locator
import kotlin.reflect.KClass

abstract class LocatorConfig {

    protected val descriptors = mutableListOf<Descriptor>()

    /**
     * Bind service [impl]ementation class to [contracts] or implementations.
     * @param impl The Service implementation
     */
    fun bind(impl: Any): Descriptor =
            Descriptor.Object(impl).also {
                descriptors.add(it)
            }

    /**
     * Bind service [impl]ementation class to [contracts] or implementations.
     * @param impl The Service implementation class
     */
    fun bind(impl: KClass<out Any>): Descriptor =
            Descriptor.Singleton(impl).also {
                descriptors.add(it)
            }

    /**
     * Bind service creation [function] to [contracts] or implementations.
     * @param function The Service creation function
     */
    fun bind(function: (Locator) -> Any): Descriptor =
            Descriptor.Function(function).also {
                descriptors.add(it)
            }

    abstract fun bindTo(locator: LocatorImpl)

    sealed class Descriptor(var contract: Contract, var name: String? = null) {

        class Function(val func: (Locator) -> Any) : Descriptor(Contract.None())

        class Singleton(val impl: KClass<out Any>) : Descriptor(Contract.Self(impl))

        class Object(val impl: Any) : Descriptor(Contract.Self(impl::class))

        fun to(vararg contracts: KClass<out Any>): Descriptor = this.apply {
            kotlin.synchronized(contract) {
                when (contract) {
                    is Contract.Set -> (contract as Contract.Set).contracts.addAll(contracts.toMutableSet())
                    else -> contract = Contract.Set(contracts.toMutableSet())
                }
            }
        }

        fun named(name: String): Descriptor = this.apply {
            this.name = name
        }

        fun to(literal: TypeLiteral<out Any>): Descriptor = this.apply {
            contract = Contract.TypeLiteral(literal)
        }
    }

    sealed class Contract {
        class None : Contract()
        class Self(val contract: KClass<out Any>) : Contract()
        class Set(val contracts: MutableSet<KClass<out Any>>) : Contract()
        class TypeLiteral(val literal: org.glassfish.hk2.api.TypeLiteral<out Any>) : Contract()
    }
}