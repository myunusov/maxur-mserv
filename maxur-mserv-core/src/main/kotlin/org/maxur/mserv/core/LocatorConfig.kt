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
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> bind(implementation: T): Descriptor<T> =
            Descriptor(BeanObject(implementation), ContractSelf(implementation::class as KClass<in T>)).also {
                descriptors.add(it)
            }

    /**
     * Bind service [implementation] class
     * @param implementation The Service implementation class
     */
    fun <T : Any> bind(implementation: KClass<in T>): Descriptor<T> =
            Descriptor(BeanSingleton(implementation), ContractSelf(implementation)).also {
                descriptors.add(it)
            }

    /**
     * Bind factory by [function]
     * @param function The Service creation function
     */
    fun <T : Any> bindFactory(function: (Locator) -> T): Descriptor<T> =
            Descriptor(BeanFunction(function), ContractNone()).also {
                descriptors.add(it)
            }

    abstract fun apply()

    class Descriptor<T : Any>(var bean: Bean<T>, var contract: Contract<T>, var name: String? = null) {

        @Suppress("UNCHECKED_CAST")
        fun to(contract: Any) {
            if (contract is TypeLiteral<*>)
                this.contract = LocatorConfig.ContractTypeLiteral(contract as TypeLiteral<in T>)
            if (contract is KClass<*>) {
                when (this.contract) {
                    is ContractSet -> (this.contract as ContractSet).contracts.add(contract as KClass<T>)
                    else -> this.contract = ContractSet<T>(mutableSetOf(contract as KClass<T>))
                }
            }
        }

        fun to(vararg contracts: KClass<in T>): Descriptor<T> = this.apply {
            when (contract) {
                is ContractSet -> (contract as ContractSet).contracts.addAll(contracts.toMutableSet())
                else -> contract = ContractSet(contracts.toMutableSet())
            }
        }

        fun named(name: String): Descriptor<T> = this.apply {
            this.name = name
        }

    }

    abstract class Bean<T : Any>
    class BeanFunction<T : Any>(val func: (Locator) -> T) : Bean<T>()
    class BeanSingleton<T : Any>(val impl: KClass<in T>) : Bean<T>()
    @Suppress("UNCHECKED_CAST")
    class BeanObject<T : Any>(val impl: T) : Bean<T>()

    abstract class Contract<T : Any>
    class ContractNone<T : Any> : Contract<T>()
    class ContractSelf<T : Any>(val contract: KClass<in T>) : Contract<T>()
    class ContractSet<T : Any>(val contracts: MutableSet<KClass<in T>>) : Contract<T>()
    class ContractTypeLiteral<T : Any>(val literal: org.glassfish.hk2.api.TypeLiteral<in T>) : Contract<T>()

}