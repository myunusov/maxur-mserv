package org.maxur.mserv.core.service.hk2

import com.fasterxml.jackson.databind.ObjectMapper
import gov.va.oia.HK2Utilities.HK2RuntimeInitializer
import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.hk2.api.TypeLiteral
import org.glassfish.hk2.utilities.Binder
import org.glassfish.hk2.utilities.ServiceLocatorUtilities
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.maxur.mserv.core.LocatorImpl
import org.maxur.mserv.core.annotation.Value
import org.maxur.mserv.core.builder.LocatorBuilder
import org.maxur.mserv.core.kotlin.Locator
import org.maxur.mserv.core.service.jackson.ObjectMapperProvider
import javax.inject.Singleton
import kotlin.reflect.KClass

// TODO pull bind logic to Abstract class without org.glassfish.hk2.utilities.Binder
/**
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
class LocatorHK2ImplBuilder : LocatorBuilder() {

    private val binders = ArrayList<Binder>().apply {
        add(ObjectMapperBinder())
        add(PropertiesInjectionResolverBinder())
    }

    override fun bind(locator: Locator) {
        ServiceLocatorUtilities.bind(locator.implementation<ServiceLocator>(), *binders.toTypedArray())
    }

    override fun make(): Locator =
        makeServiceLocator().getService(Locator::class.java) ?:
        throw IllegalStateException(makeServiceLocator().getService(ErrorHandler::class.java)?.latestError)

    private fun makeServiceLocator() = if (packages.isNotEmpty()) {
        HK2RuntimeInitializer.init(
            name,
            true,
            *packages.toTypedArray(), "org.maxur.mserv.core"
        )
    } else {
        ServiceLocatorUtilities.createAndPopulateServiceLocator(name)
    }.also {
        ServiceLocatorUtilities.enableImmediateScope(it)
        ServiceLocatorUtilities.bind(it, LocatorBinder())
    }

    /** {@inheritDoc} */
    override fun bind(function: (Locator) -> Any, vararg classes: KClass<out Any>) {
        binders.add(ServiceBinder(function, *classes))
    }

    private class ServiceBinder(val func: (Locator) -> Any, vararg val classes: KClass<out Any>) : AbstractBinder() {
        /** {@inheritDoc} */
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
        /** {@inheritDoc} */
        override fun dispose(instance: T) = Unit

        /** {@inheritDoc} */
        override fun provide(): T = result
    }

    private class PropertiesInjectionResolverBinder : AbstractBinder() {
        /** {@inheritDoc} */
        override fun configure() {
            bind(PropertiesInjectionResolver::class.java)
                .to(object : TypeLiteral<InjectionResolver<Value>>() {})
                .`in`(Singleton::class.java)
        }
    }

    private class ObjectMapperBinder : AbstractBinder() {
        /** {@inheritDoc} */
        override fun configure() {
            bindFactory(ObjectMapperProvider::class.java)
                .to(ObjectMapper::class.java)
                .`in`(Singleton::class.java)
        }
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