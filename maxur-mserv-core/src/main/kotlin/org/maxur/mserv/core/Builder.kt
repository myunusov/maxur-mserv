package org.maxur.mserv.core

import org.maxur.mserv.frame.kotlin.Locator

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