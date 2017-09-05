@file:Suppress("unused")

package org.maxur.mserv.core.service.properties

import org.maxur.mserv.core.core.ErrorResult
import org.maxur.mserv.core.core.Result
import org.maxur.mserv.core.core.Value
import org.maxur.mserv.core.core.fold
import org.maxur.mserv.core.kotlin.Locator
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

    val sources: List<PropertiesSource> = listOf(this)

    companion object {
        /**
         * Open properties resource.
         *
         * @param format the properties format
         * @param rootKey the root key of properties
         * @param uri the uri of properties source
         */
        fun open(format: String, uri: URI? = null, rootKey: String? = null): Properties =
                Locator.current.locate(PropertiesFactory::class, format)
                        .make(object : PropertiesSource(format, uri, rootKey) {})
                        .result()

        private fun <E : Throwable, V> Result<E, V>.result(): V = when (this) {
            is Value -> value
            is ErrorResult -> throw when (error) {
                is IllegalStateException -> error
                else -> IllegalStateException(error)
            }
        }

        fun default(): Properties {
            Locator.beans(PropertiesFactory::class).map {
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
