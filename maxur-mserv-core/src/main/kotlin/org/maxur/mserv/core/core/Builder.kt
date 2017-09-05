package org.maxur.mserv.core.core

import org.maxur.mserv.core.kotlin.Locator

/**
 * Object Builder.
 * It Build instance of class by locator.
 */
interface Builder<T> {

    /**
     * Build instance of class by locator.
     * @param locator The Service Locator.
     */
    fun build(locator: Locator): T

}