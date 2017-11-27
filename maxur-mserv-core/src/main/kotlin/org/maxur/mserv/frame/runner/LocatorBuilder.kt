package org.maxur.mserv.frame.runner

import org.maxur.mserv.frame.LocatorConfig
import org.maxur.mserv.frame.LocatorImpl
import org.maxur.mserv.frame.kotlin.Locator
import java.util.concurrent.atomic.AtomicInteger

/**
 * The Service Locator Builder.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>26.08.2017</pre>
 */
abstract class LocatorBuilder() {

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
    fun build(init: LocatorConfig.() -> Unit): Locator = try {
        val locator = make()
        locator.configure {
            bind(org.maxur.mserv.frame.kotlin.Locator(locator))
            bind(org.maxur.mserv.frame.java.Locator(locator))
        }
        locator.registerAsSingleton()
        configure(locator) {
            init()
        }
        org.maxur.mserv.frame.kotlin.Locator(locator)
    } catch (e: Exception) {
        Locator.current.onConfigurationError(e)
    }

    protected abstract fun make(): LocatorImpl

    protected abstract fun configure(locator: LocatorImpl, function: LocatorConfig.() -> Unit): LocatorConfig
}