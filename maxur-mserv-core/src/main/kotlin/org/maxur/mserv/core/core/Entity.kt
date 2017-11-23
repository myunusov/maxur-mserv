package org.maxur.mserv.core.core

/**
 * Base entity class. Represents all objects with identifier.
 * @param <T> Type of Entity Identifier
 * @author Maxim Yunusov
 * @version 1.0
 */
abstract class Entity<T>(val id: Id<T>) {

    override fun equals(other: Any?) = (this === other) || ((other is Entity<*>) && (id == other.id))

    override fun hashCode() = id.hashCode()
}