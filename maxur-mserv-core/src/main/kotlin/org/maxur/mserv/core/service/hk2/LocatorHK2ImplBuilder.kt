package org.maxur.mserv.core.service.hk2

import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.TypeLiteral
import org.maxur.mserv.core.LocatorConfig
import org.maxur.mserv.core.annotation.Value
import org.maxur.mserv.core.builder.LocatorBuilder
import org.maxur.mserv.core.kotlin.Locator

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
class LocatorHK2ImplBuilder(init: LocatorConfig.() -> Unit) : LocatorBuilder(init) {

    override fun make() = LocatorHK2Impl(name, packages)

    override fun config(function: LocatorConfig.() -> Unit) =
            Locator.current.configure {
                bind(PropertiesInjectionResolver::class).to(object : TypeLiteral<InjectionResolver<Value>>() {})
                function()
            }
}