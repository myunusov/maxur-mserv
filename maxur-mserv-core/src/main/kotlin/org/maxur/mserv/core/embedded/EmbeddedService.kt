package org.maxur.mserv.core.embedded

/**
 * This class represents Embedded to micro-service Service
 * with start and stop functions
 */
interface EmbeddedService {

    /**
     * Start Web server.
     */
    fun start()

    /**
     * Stop Web server.
     */
    fun stop()

}
