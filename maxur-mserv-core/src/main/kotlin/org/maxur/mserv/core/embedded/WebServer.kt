package org.maxur.mserv.core.embedded

import org.jvnet.hk2.annotations.Contract
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
@Contract
abstract class WebServer: EmbeddedService() {

    companion object {
        val log: Logger = LoggerFactory.getLogger(WebServer::class.java)
    }

    abstract val name: String

    abstract val baseUri: URI

    /**
     * Start Web server.
     */
    override fun start() {
        log.info("Start Web Server")
        launch()
        log.info("$name is started on $baseUri")
        log.info(entries().toString())
    }

    /**
     * Stop Web server.
     */
    override fun stop() {
        log.info("Stop Web Server")
        shutdown()
        log.info("Web Server is stopped")
    }

    protected abstract fun entries(): WebEntries
    

    /**
     * web server launch
     */
    protected abstract fun launch()

    /**
     * web server shutdown
     */
    protected abstract fun shutdown()

}

