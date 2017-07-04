package org.maxur.mserv.core.embedded

import java.net.URI

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
interface WebServer:  EmbeddedService {

    var name: String

    val baseUri: URI

    fun entries(): WebEntries
    
}

