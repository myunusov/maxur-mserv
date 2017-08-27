package org.maxur.mserv.core.builder

import org.maxur.mserv.core.kotlin.Locator

/**
 * Composite Builder
 * It's Build Composite instance by part.
 */
abstract class CompositeBuilder<T : Any> : Builder<T>, Composite<Builder<T?>>() {

    protected fun buildListWith(locator: Locator): List<T> = list
        .map { it.build(locator) }
        .filterNotNull()

    protected fun buildListWith(locator: Locator, predicate: (Builder<T?>) -> Boolean): List<T> = list
        .filter(predicate)
        .map { it.build(locator) }
        .filterNotNull()
}