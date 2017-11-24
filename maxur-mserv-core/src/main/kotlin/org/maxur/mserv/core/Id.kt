@file:Suppress("unused")

package org.maxur.mserv.core

/**
 * The typed Identifier.
 *
 * @param <T> Type of Entity Identifier A globally unique identifier for objects
 */
interface Id<T> {

    /** This identifier as String. */
    val value: String
}