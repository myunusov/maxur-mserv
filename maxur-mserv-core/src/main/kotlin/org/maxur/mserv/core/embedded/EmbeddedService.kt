package org.maxur.mserv.core.embedded

/**
 * This class represents Embedded to micro-service Service
 * with start and stop functions
 */
interface EmbeddedService {

    /**
     * Start server.
     */
    fun start()

    /**
     * Stop server.
     */
    fun stop()
}
