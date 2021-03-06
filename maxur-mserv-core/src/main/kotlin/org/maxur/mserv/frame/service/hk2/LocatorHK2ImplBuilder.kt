package org.maxur.mserv.frame.service.hk2

import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.TypeLiteral
import org.maxur.mserv.frame.LocatorConfig
import org.maxur.mserv.frame.LocatorImpl
import org.maxur.mserv.frame.annotation.ServiceName
import org.maxur.mserv.frame.annotation.Value
import org.maxur.mserv.frame.runner.LocatorBuilder

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
class LocatorHK2ImplBuilder : LocatorBuilder() {

    override fun make() = LocatorHK2Impl(name, packages)

    override fun configure(locator: LocatorImpl, function: LocatorConfig.() -> Unit) =
            locator.configure {
                bind(PropertiesInjectionResolver::class).to(object : TypeLiteral<InjectionResolver<Value>>() {})
                bind(ServiceNameInjectionResolver::class).to(object : TypeLiteral<InjectionResolver<ServiceName>>() {})
                function()
            }
}