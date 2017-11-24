package org.maxur.mserv.frame.event

import org.maxur.mserv.core.command.Event

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>24.11.2017</pre>
 */

/** On start web server */
class WebServerStartedEvent : Event()

/** On stop web server */
class WebServerStoppedEvent : Event()