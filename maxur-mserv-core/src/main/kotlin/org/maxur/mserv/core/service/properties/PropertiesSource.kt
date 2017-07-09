@file:Suppress("unused")

package org.maxur.mserv.core.service.properties

import java.net.URI

/**
 * Represent the Properties source configuration.
 *
 * @param format the properties format
 * @param rootKey the root key of properties
 * @param uri the uri of properties source
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
data class PropertiesSource(val format: String, val uri: URI?, val rootKey: String?) {

    /**
     * It returns true when properties is configured
     */
    fun isConfigured() = !rootKey.isNullOrEmpty() || uri != null

}
