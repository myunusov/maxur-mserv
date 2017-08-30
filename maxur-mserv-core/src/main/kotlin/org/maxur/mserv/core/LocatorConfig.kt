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
    open fun bind(impl: Any): Descriptor =
            DescriptorObject(impl).also {
                descriptors.add(it)
            }

    /**
     * Bind service [impl]ementation class to [contracts] or implementations.
     * @param impl The Service implementation class
     */
    fun bind(impl: KClass<out Any>): Descriptor =
            DescriptorSingleton(impl).also {
                descriptors.add(it)
            }

    /**
     * Bind service creation [function] to [contracts] or implementations.
     * @param function The Service creation function
     */
    fun bind(function: (Locator) -> Any): Descriptor =
            DescriptorFunction(function).also {
                descriptors.add(it)
            }

    abstract fun bindTo(locator: LocatorImpl)

    abstract class Descriptor(var contract: Contract, var name: String? = null) {

        fun to(vararg contracts: KClass<out Any>): Descriptor = this.apply {
            kotlin.synchronized(contract) {
                when (contract) {
                    is ContractSet -> (contract as ContractSet).contracts.addAll(contracts.toMutableSet())
                    else -> contract = ContractSet(contracts.toMutableSet())
                }
            }
        }

        fun named(name: String): Descriptor = this.apply {
            this.name = name
        }

        fun to(literal: TypeLiteral<out Any>): Descriptor = this.apply {
            contract = ContractTypeLiteral(literal)
        }
    }
    class DescriptorFunction(val func: (Locator) -> Any) : Descriptor(ContractNone())
    class DescriptorSingleton(val impl: KClass<out Any>) : Descriptor(ContractSelf(impl))
    class DescriptorObject(val impl: Any) : Descriptor(ContractSelf(impl::class))

    abstract class Contract
    class ContractNone : Contract()
    class ContractSelf(val contract: KClass<out Any>) : Contract()
    class ContractSet(val contracts: MutableSet<KClass<out Any>>) : Contract()
    class ContractTypeLiteral(val literal: org.glassfish.hk2.api.TypeLiteral<out Any>) : Contract()

}