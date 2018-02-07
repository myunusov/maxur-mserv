@file:Suppress("unused")

package org.maxur.mserv.core

import org.maxur.mserv.core.command.Event

/**
 * The typed Identifier.
 *
 * @param <T> Type of Entity Identifier A globally unique identifier for objects
 */
interface Id<T> {

    /** This identifier as String. */
    val value: String

    /** Unknown identifier (null object) */
    object Unknown : Id<Event> {
        override val value = "Unknown"
    }
}