@file:Suppress("unused")

package org.maxur.mserv.core.service.properties

import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.core.fold
import java.net.URI

/**
 * Represent the Properties source configuration.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
abstract class PropertiesSource(
        open val format: String? = null,
        open val uri: URI? = null,
        open val rootKey: String? = null
) {

    companion object {
        /**
         * Open properties resource.
         *
         * @param format the properties format
         * @param rootKey the root key of properties
         * @param uri the uri of properties source
         */
        fun open(format: String, uri: URI? = null, rootKey: String? = null): Properties =
                Locator
                        .service(PropertiesFactory::class, format)!!
                        .make(object : PropertiesSource(format, uri, rootKey) {})
                        .fold({ throw it }, { it })

        fun default(): Properties {
            Locator.services(PropertiesFactory::class).map {
                it.make(object : PropertiesSource() {})
                        .fold({ }, { return it })
            }
            return NullProperties
        }

        fun nothing(): Properties = NullProperties
    }
}

object NullProperties : PropertiesSource(), Properties {
    override fun asString(key: String): String? = error(key)
    override fun asLong(key: String): Long? = error(key)
    override fun asInteger(key: String): Int? = error(key)
    override fun asURI(key: String): URI? = error(key)
    override fun <P> read(key: String, clazz: Class<P>): P? = error(key)
    private fun <T> error(key: String): T =
            throw IllegalStateException("Service Configuration is not found. Key '$key' unresolved")
}
