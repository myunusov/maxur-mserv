package org.maxur.mserv.core

/**
 * The Domain Event
 */
@Suppress("unused")
class Event<T>(
    /** identifier **/
    id: Id<Event<*>>
) : Entity<Event<*>>(id)
