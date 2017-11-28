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
    lateinit var id: Id<out Event>
    val occurredOn = Instant.now()
}
