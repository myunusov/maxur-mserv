package org.maxur.mserv.core.builder

import com.fasterxml.jackson.databind.ObjectMapper
import org.maxur.mserv.core.LocatorConfig
import org.maxur.mserv.core.LocatorImpl
import org.maxur.mserv.core.core.checkError
import org.maxur.mserv.core.kotlin.Locator
import org.maxur.mserv.core.service.jackson.ObjectMapperProvider
import java.util.concurrent.atomic.AtomicInteger

/**
 * The Service Locator Builder.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>26.08.2017</pre>
 */
abstract class LocatorBuilder(val init: LocatorConfig.() -> Unit) {

    companion object {
        private var nameCount = AtomicInteger()
    }

    protected val name get() = "locator ${nameCount.andIncrement}"

    /**
     * List of project service packages for service locator lookup.
     */
    var packages: Set<String> = setOf()

    /**
     * Build service locator.
     */
    fun build(): Locator = checkError({
        val locator = make()
        locator.configure {
            bind(org.maxur.mserv.core.kotlin.Locator(locator))
            bind(org.maxur.mserv.core.java.Locator(locator))
        }
        locator.registerAsSingleton()
        configure(locator) {
            init()
            bind(ObjectMapperProvider::class).to(ObjectMapper::class)
        }
        org.maxur.mserv.core.kotlin.Locator(locator)
    }, { e -> Locator.current.onConfigurationError(e) })

    protected abstract fun make(): LocatorImpl

    protected abstract fun configure(locator: LocatorImpl, function: LocatorConfig.() -> Unit): LocatorConfig

}