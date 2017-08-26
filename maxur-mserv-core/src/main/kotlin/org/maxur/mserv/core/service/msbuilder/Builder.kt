package org.maxur.mserv.core.service.msbuilder

import org.maxur.mserv.core.Locator

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