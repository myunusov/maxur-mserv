@file:Suppress("unused")

package org.maxur.mserv.core.service.properties

import org.maxur.mserv.core.Locator
import java.net.URI

/**
 * Represent the Properties source configuration.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
interface PropertiesSource {

    companion object {
        fun open(format: String?, uri: URI? = null, rootKey: String? = null): Properties =
            BasePropertiesSource(format, uri, rootKey).open()
    }

    val format: String?
    val uri: URI?
    val rootKey: String?

    /**
     * It returns true when properties is configured
     */
    fun isConfigured() = !rootKey.isNullOrEmpty() || uri != null

}

/**
 * @param format the properties format
 * @param rootKey the root key of properties
 * @param uri the uri of properties source
 */
private data class BasePropertiesSource(override val format: String?,
                                         override val uri: URI?,
                                         override val rootKey: String?
) : PropertiesSource {

    /**
     * open resource
     */
    fun open(): Properties =
            if (format == null) openDefault() else openDefined(format)

    private fun openDefined(format: String): Properties {
        return Locator
                .service(PropertiesFactory::class, format)!!
                .make(this) ?:
                throw IllegalArgumentException("""The '$uri' file not found. Add it with '$rootKey' section""")
    }

    private fun openDefault(): Properties {
        Locator.services(PropertiesFactory::class)
                .filter { !(it is PropertiesFactoryNullImpl) }
                .map {
                    val properties = it.make(this)
                    if (properties != null) return properties
                }
        throw IllegalArgumentException("""Any property file not found. Add it to classpath""")
    }


}
