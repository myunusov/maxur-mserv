package org.maxur.mserv.frame.embedded

import org.maxur.mserv.core.command.Event

/**
 * This class represents Embedded to micro-service Service
 * with start and stop functions
 */
interface EmbeddedService {

    /**
     * Start server.
     */
    fun start(): List<Event>

    /**
     * Stop server.
     */
    fun stop(): List<Event>
}
