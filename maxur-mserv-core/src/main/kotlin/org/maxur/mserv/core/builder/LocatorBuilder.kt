package org.maxur.mserv.core.builder

import com.fasterxml.jackson.databind.ObjectMapper
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.TypeLiteral
import org.maxur.mserv.core.annotation.Value
import org.maxur.mserv.core.core.checkError
import org.maxur.mserv.core.kotlin.Locator
import org.maxur.mserv.core.service.hk2.PropertiesInjectionResolver
import org.maxur.mserv.core.service.jackson.ObjectMapperProvider
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

/**
 * The Service Locator Builder.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>26.08.2017</pre>
 */
abstract class LocatorBuilder(val init: Config.() -> Unit) {

    companion object {
        private var nameCount = AtomicInteger()
    }

    protected val name get() = "locator ${nameCount.andIncrement}"

    protected abstract fun makeConfig(init: Config.() -> Unit): Config

    /**
     * List of project service packages for service locator lookup.
     */
    var packages: Set<String> = setOf()
        get() = if (field.isEmpty()) {
            emptySet()
        } else {
            field.union(listOf("org.maxur.mserv.core"))
        }

    /**
     * Build service locator.
     */
    fun build(): Locator = checkError({
            make().also {
                makeConfig {
                    bind(it)
                    bind(org.maxur.mserv.core.java.Locator(it))
                }.bindTo(it)
                it.registerAsSingleton()
                makeConfig(init)
                    .apply {
                        // TODO Push down
                        bind(PropertiesInjectionResolver::class, object : TypeLiteral<InjectionResolver<Value>>() {})
                        bind(ObjectMapperProvider::class, ObjectMapper::class)
                    }
                    .bindTo(it)
            }
        }, { e -> Locator.current.onConfigurationError(e) })

    protected abstract fun make(): Locator

    abstract class Config(init: Config.() -> Unit) {

        init {
            init()
        }

        /**
         * Bind service [impl]ementation class to [typeLiteral].
         * @param impl The Service implementation class
         * @param typeLiteral typeLiteral.
         */
        abstract fun bind(impl: KClass<out Any>, typeLiteral: TypeLiteral<out Any>)

        /**
         * Bind service [impl]ementation class to [contracts] or implementations.
         * @param impl The Service implementation class
         * @param contracts List of contracts or implementation.
         */
        abstract fun bind(impl: KClass<out Any>, vararg contracts: KClass<out Any>)

        /**
         * Bind service [impl]ementation class to [contracts] or implementations.
         * @param impl The Service implementation
         * @param contracts List of contracts or implementation.
         */
        abstract fun bind(impl: Any, vararg contracts: KClass<out Any>)

        /**
         * Bind service creation [function] to [contracts] or implementations.
         * @param function The Service creation function
         * @param contracts List of contracts or implementation.
         */
        abstract fun bind(function: (Locator) -> Any, vararg contracts: KClass<out Any>)

        abstract fun bindTo(locator: Locator)
    }

    sealed class Descriptor(vararg val contracts: KClass<out Any>) {
        class Function(val func: (Locator) -> Any, vararg contracts: KClass<out Any>) : Descriptor(*contracts)
        class Singleton(val impl: KClass<out Any>, vararg contracts: KClass<out Any>) : Descriptor(*contracts)
        class Object(val impl: Any, vararg contracts: KClass<out Any>) : Descriptor(*contracts)
        class Literal(val impl: KClass<out Any>, val literal: TypeLiteral<out Any>) : Descriptor(impl::class)
    }

}