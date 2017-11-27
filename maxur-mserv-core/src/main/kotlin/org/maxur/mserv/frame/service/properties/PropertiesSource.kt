@file:Suppress("unused")

package org.maxur.mserv.frame.service.properties

import org.maxur.mserv.core.fold
import org.maxur.mserv.frame.kotlin.Locator
import java.net.URI

/**
 * Represent the Properties source configuration.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
abstract class PropertiesSource {

    abstract val format: String
    abstract val uri: URI
    abstract val rootKey: String

    val sources: List<PropertiesSource> = listOf(this)

    companion object {

        fun default(): Properties {
            Locator.beans(PropertiesFactory::class).map {
                it.make().fold({ }, { return it })
            }
            return NullProperties
        }

        fun nothing(): Properties = NullProperties
    }
}

object NullProperties : PropertiesSource(), Properties {
    override val format = "undefined"
    override val uri = URI("")
    override val rootKey: String = ""
    override fun asString(key: String): String? = error(key)
    override fun asLong(key: String): Long? = error(key)
    override fun asInteger(key: String): Int? = error(key)
    override fun asURI(key: String): URI? = error(key)
    override fun <P> read(key: String, clazz: Class<P>): P? = error(key)
    private fun <T> error(key: String): T =
            throw IllegalStateException("Service Configuration is not found. Key '$key' unresolved")
}
