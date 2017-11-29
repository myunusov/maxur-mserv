package org.maxur.mserv.core.command

import org.maxur.mserv.core.Id
import java.time.Instant

/**
 * The Domain Event
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>24.11.2017</pre>
 */
abstract class Event {

    /** This Identifier of Events. It's not mandatory property */
    var id: Id<Event> = Id.Unknown
        get() = field
        private set(value) {
            field = value
        }

    val occurredOn = Instant.now()

    /* XXX May be immutable */
    /** Identify this Event as [identifier] */
    fun identifyAs(identifier: Id<Event>): Event {
        this.id = identifier
        return this
    }
}
