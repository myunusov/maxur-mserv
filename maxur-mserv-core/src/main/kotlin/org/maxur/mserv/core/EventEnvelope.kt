package org.maxur.mserv.core

import org.maxur.mserv.core.command.Event

/**
 * The Domain [Event] Envelope with [id]
 */
@Suppress("unused")
class EventEnvelope(
    /** identifier **/
    id: Id<EventEnvelope>,
    /** domain event */
    val event: Event
) : Entity<EventEnvelope>(id)
