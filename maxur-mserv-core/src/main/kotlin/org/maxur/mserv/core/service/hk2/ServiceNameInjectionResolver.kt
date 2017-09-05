package org.maxur.mserv.core.service.hk2

import org.glassfish.hk2.api.Injectee
import org.glassfish.hk2.api.InjectionResolver
import org.glassfish.hk2.api.ServiceHandle
import org.maxur.mserv.core.annotation.ServiceName
import org.maxur.mserv.core.kotlin.Locator
import javax.inject.Inject

/**
 * Resolve Service name by ServiceName annotations.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>05.09.2017</pre>
 */
class ServiceNameInjectionResolver @Inject constructor(val locator: Locator) : InjectionResolver<ServiceName> {

    override fun resolve(injectee: Injectee, root: ServiceHandle<*>?) =
            injectee.injecteeDescriptor.name

    override fun isConstructorParameterIndicator(): Boolean {
        return false
    }

    override fun isMethodParameterIndicator(): Boolean {
        return false
    }
}
