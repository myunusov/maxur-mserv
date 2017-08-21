package org.maxur.mserv.core.service.hk2

import com.fasterxml.jackson.databind.ObjectMapper
import gov.va.oia.HK2Utilities.HK2RuntimeInitializer
import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.TypeLiteral
import org.glassfish.hk2.utilities.Binder
import org.glassfish.hk2.utilities.ServiceLocatorUtilities
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.annotation.Value
import org.maxur.mserv.core.service.jackson.ObjectMapperProvider
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
class LocatorFactoryHK2Impl(init: LocatorFactoryHK2Impl.() -> Unit) {

    companion object {
        private var nameCount = AtomicInteger()
    }

    private val newName get() = "locator ${nameCount.andIncrement}"

    var packages: List<String> = emptyList()

    private val binders = ArrayList<Binder>().apply {
        add(ObjectMapperBinder())
        add(PropertiesInjectionResolverBinder())
    }

    init {
        @Suppress("UNUSED_EXPRESSION")
        init()
    }

    fun make(): Locator {
        val serviceLocator = makeLocator()
        val locator = serviceLocator.getService(Locator::class.java)
        ServiceLocatorUtilities.bind(serviceLocator, *binders.toTypedArray())
        return locator
    }

    private fun makeLocator() = if (packages.isNotEmpty()) {
        HK2RuntimeInitializer.init(
                newName,
                true,
                *packages.toTypedArray(), "org.maxur.mserv.core"
        )
    } else {
        ServiceLocatorUtilities.createAndPopulateServiceLocator(newName)
    }.also {
        ServiceLocatorUtilities.enableImmediateScope(it)
        ServiceLocatorUtilities.bind(it, LocatorBinder())
    }

    fun bind(vararg binders: Binder): LocatorFactoryHK2Impl {
        this.binders.addAll(binders)
        return this
    }

    fun bind(func: (Locator) -> Any, vararg classes: KClass<out Any>) {
        binders.add(ServiceBinder(func, *classes))
    }

    private class ServiceBinder(val func: (Locator) -> Any, vararg val classes: KClass<out Any>) : AbstractBinder() {
        override fun configure() {
            val provider = ServiceProvider(func)
            classes.forEach {
                bindFactory(provider).to(it.java)
            }
        }
    }

    private class ServiceProvider<T>(val func: (Locator) -> T) : Factory<T> {
        val locator: Locator by lazy { Locator.current }
        val result: T by lazy { func.invoke(locator) }
        override fun dispose(instance: T) = Unit
        override fun provide(): T = result
    }

    private class PropertiesInjectionResolverBinder : AbstractBinder() {
        override fun configure() {
            bind(PropertiesInjectionResolver::class.java)
                    .to(object : TypeLiteral<InjectionResolver<Value>>() {})
                    .`in`(Singleton::class.java)
        }
    }

    private class ObjectMapperBinder : AbstractBinder() {
        override fun configure() {
            bindFactory(ObjectMapperProvider::class.java)
                    .to(ObjectMapper::class.java)
                    .`in`(Singleton::class.java)
        }
    }

    class LocatorBinder : AbstractBinder() {

        override fun configure() {
            bind(LocatorHK2Impl::class.java)
                    .to(Locator::class.java)
                    .`in`(Singleton::class.java)
        }

    }

}