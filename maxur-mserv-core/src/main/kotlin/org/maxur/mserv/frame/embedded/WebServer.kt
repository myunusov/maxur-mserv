package org.maxur.mserv.frame.embedded

import java.net.URI

/**
 * The Web Server interface.
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
interface WebServer : EmbeddedService {

    /** Web Server name */
    var name: String

    /** Web Server base URI */
    val baseUri: URI

    /** Web Entries */
    fun entries(): WebEntries
}

