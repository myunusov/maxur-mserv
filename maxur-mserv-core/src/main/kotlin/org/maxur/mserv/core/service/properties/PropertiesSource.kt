@file:Suppress("unused")

package org.maxur.mserv.core.service.properties

import org.jvnet.hk2.annotations.Contract
import org.maxur.mserv.core.Locator
import java.net.URI

/**
 * Represent the Properties source configuration.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
@Contract
interface PropertiesSource {

    companion object {
        fun make(format: String, uri: URI?, rootKey: String?): PropertiesSource
                = RawPropertiesSource(format, uri, rootKey)
    }

    val format: String
    val uri: URI?
    val rootKey: String?

    /**
     * open resource
     *
     */
    fun open(): PropertiesSource = this

    /**
     * return properties by key

     * @param key properties key
     * *
     * @return properties by key
     */
    fun asString(key: String): String?

    /**
     * return properties by key
     *
     * @param key properties key
     *
     * @return properties by key
     */
    fun asLong(key: String): Long?

    /**
     * return properties by key
     *
     * @param key properties key
     * *
     * @return properties by key
     */
    fun asInteger(key: String): Int?

    /**
     * return properties by key
     *
     * @param key properties key
     *
     * @return properties by key
     */
    fun asURI(key: String): URI?

    /**
     * return properties by key
     *
     * @param key properties key
     *
     * @param clazz properties type
     * *
     * @return properties by key
     */
    fun <P> read(key: String, clazz: Class<P>): P?

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
internal data class RawPropertiesSource(override val format: String,
                                        override val uri: URI?,
                                        override val rootKey: String?
) : PropertiesSource {
    override fun open() = Locator
            .service(PropertiesSourceFactory::class, format)!!
            .make(this)
    override fun asString(key: String): String? = error(key)
    override fun asLong(key: String): Long? = error(key)
    override fun asInteger(key: String): Int? = error(key)
    override fun asURI(key: String): URI? = error(key)
    override fun <P> read(key: String, clazz: Class<P>): P? = error(key)
    private fun <T> error(key: String): T =
            throw IllegalStateException("Service Configuration is not found. Key '$key' unresolved")
}
