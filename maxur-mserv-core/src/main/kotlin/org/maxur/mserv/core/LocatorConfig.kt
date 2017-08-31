package org.maxur.mserv.core

import org.glassfish.hk2.api.TypeLiteral
import org.maxur.mserv.core.kotlin.Locator
import kotlin.reflect.KClass

abstract class LocatorConfig(protected val locator: LocatorImpl) {

    protected val descriptors = mutableListOf<Descriptor>()

    /**
     * Bind service [implementation]
     * @param implementation The Service implementation
     */
    open fun bind(implementation: Any): Descriptor =
            DescriptorObject(implementation).also {
                descriptors.add(it)
            }

    /**
     * Bind service [implementation] class
     * @param implementation The Service implementation class
     */
    fun bind(implementation: KClass<out Any>): Descriptor =
            DescriptorSingleton(implementation).also {
                descriptors.add(it)
            }

    /**
     * Bind service creation [function] 
     * @param function The Service creation function
     */
    fun bind(function: (Locator) -> Any): Descriptor =
            DescriptorFunction(function).also {
                descriptors.add(it)
            }

    abstract fun apply()

    abstract class Descriptor(var contract: Contract, var name: String? = null) {

        fun to(vararg contracts: KClass<out Any>): Descriptor = this.apply {
            kotlin.synchronized(contract) {
                when (contract) {
                    is ContractSet -> (contract as ContractSet).contracts.addAll(contracts.toMutableSet())
                    else -> contract = ContractSet(contracts.toMutableSet())
                }
            }
        }

        fun to(literal: TypeLiteral<out Any>): LocatorConfig.Descriptor = this.apply {
            contract = LocatorConfig.ContractTypeLiteral(literal)
        }

        fun named(name: String): Descriptor = this.apply {
            this.name = name
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