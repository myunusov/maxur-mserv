@file:Suppress("unused")

package org.maxur.mserv.core.core

/**
 * The typed Identifier.
 *
 * @param <T> Type of Entity Identifier A globally unique identifier for objects
 */
interface Id<T> {
    /**
     * @return identifier as String.
     */
    fun asString(): String
}