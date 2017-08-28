package org.maxur.mserv.core.builder

import org.maxur.mserv.core.core.checkError
import org.maxur.mserv.core.kotlin.Locator
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
    var packages: Set<String> = setOf()
        get() = if (field.isEmpty()) { emptySet() } else { field.union(listOf("org.maxur.mserv.core"))  }

    /**
     * Build service locator.
     */
    fun build(): Locator = checkError(
        {
            make().also {
                it.registerAsSingleton()
                bind(it)
            }
        },
        { e -> Locator.current.onConfigurationError(e) })

    /**
     * Bind service creation function to contract or implementations.
     * @param function The Service creation function
     * @param classes List of contracts or implementation.
     */
    abstract fun bind(function: (Locator) -> Any, vararg classes: KClass<out Any>)

    protected abstract fun bind(locator: Locator)

    protected abstract fun make(): Locator

}