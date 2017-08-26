package org.maxur.mserv.core.service.msbuilder

import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.core.checkError
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

/**
 * The Service Locator Builder.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>26.08.2017</pre>
 */
abstract class LocatorBuilder {

    companion object {
        private var nameCount = AtomicInteger()
    }

    protected val name get() = "locator ${nameCount.andIncrement}"

    /**
     * List of project service packages for service locator lookup.
     */
    lateinit var packages: List<String>

    /**
     * Build service locator.
     */
    fun build(): Locator = checkError(
        { buildLocator() },
        { e -> Locator.current.onConfigurationError(e) }
    )

    /**
     * Bind service creation function to contract or implementations.
     * @param function The Service creation function
     * @param classes List of contracts or implementation.
     */
    abstract fun bind(function: (Locator) -> Any, vararg classes: KClass<out Any>)

    protected abstract fun buildLocator(): Locator

}