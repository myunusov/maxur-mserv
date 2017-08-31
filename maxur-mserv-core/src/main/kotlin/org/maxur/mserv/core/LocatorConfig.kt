package org.maxur.mserv.core

import org.glassfish.hk2.api.TypeLiteral
import org.maxur.mserv.core.kotlin.Locator
import kotlin.reflect.KClass

abstract class LocatorConfig(protected val locator: LocatorImpl) {

    protected val descriptors = mutableListOf<Descriptor<out Any>>()

    /**
     * Bind service [implementation]
     * @param implementation The Service implementation
     */
    fun <T : Any> bind(implementation: T): Descriptor<T> =
            DescriptorObject(implementation).also {
                descriptors.add(it)
            }

    /**
     * Bind service [implementation] class
     * @param implementation The Service implementation class
     */
    fun <T : Any> bind(implementation: KClass<in T>): Descriptor<T> =
            DescriptorSingleton(implementation).also {
                descriptors.add(it)
            }

    /**
     * Bind factory by [function]
     * @param function The Service creation function
     */
    fun <T : Any> bindFactory(function: (Locator) -> T): Descriptor<T> =
            DescriptorFunction(function).also {
                descriptors.add(it)
            }

    abstract fun apply()

    abstract class Descriptor<T : Any>(var contract: Contract<T>, var name: String? = null) {

        fun to(vararg contracts: KClass<in T>): Descriptor<T> = this.apply {
            when (contract) {
                is ContractSet -> (contract as ContractSet).contracts.addAll(contracts.toMutableSet())
                else -> contract = ContractSet(contracts.toMutableSet())
            }
        }

        fun to(literal: TypeLiteral<in T>) = this.apply {
            contract = LocatorConfig.ContractTypeLiteral(literal)
        }

        fun named(name: String): Descriptor<T> = this.apply {
            this.name = name
        }

    }

    class DescriptorFunction<T : Any>(val func: (Locator) -> T) : Descriptor<T>(ContractNone<T>())
    class DescriptorSingleton<T : Any>(val impl: KClass<in T>) : Descriptor<T>(ContractSelf<T>(impl))
    class DescriptorObject<T : Any>(val impl: T) : Descriptor<T>(ContractSelf<T>(impl::class as KClass<in T>))

    abstract class Contract<T : Any>
    class ContractNone<T : Any> : Contract<T>()
    class ContractSelf<T : Any>(val contract: KClass<in T>) : Contract<T>()
    class ContractSet<T : Any>(val contracts: MutableSet<KClass<in T>>) : Contract<T>()
    class ContractTypeLiteral<T : Any>(val literal: org.glassfish.hk2.api.TypeLiteral<in T>) : Contract<T>()

}