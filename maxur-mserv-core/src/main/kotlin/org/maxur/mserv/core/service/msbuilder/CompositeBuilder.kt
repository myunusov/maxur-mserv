package org.maxur.mserv.core.service.msbuilder

import org.maxur.mserv.core.Locator

/**
 * Composite Builder
 * It's Build Composite instance by part.
 */
abstract class CompositeBuilder<T : Any> : Builder<T>, Composite<Builder<T?>>() {

    protected fun buildListWith(locator: Locator): List<T> = list
        .map { it.build(locator) }
        .filterNotNull()
}