package org.maxur.mserv.frame.event

import org.maxur.mserv.core.command.Event

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>24.11.2017</pre>
 */

/** On stop microservice */
class MicroserviceStoppedEvent : Event()

/** On start microservice */
class MicroserviceStartedEvent : Event()

/** On fail microservice */
class MicroserviceFailedEvent : Event()