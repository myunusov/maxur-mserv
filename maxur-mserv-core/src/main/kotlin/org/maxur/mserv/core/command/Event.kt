package org.maxur.mserv.core.command

import java.time.Instant

/**
 * The Domain Event
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>24.11.2017</pre>
 */
abstract class Event {
    val occurredOn = Instant.now()
}
