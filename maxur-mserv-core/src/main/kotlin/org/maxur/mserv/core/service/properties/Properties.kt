package org.maxur.mserv.core.service.properties

import org.jvnet.hk2.annotations.Contract
import java.net.URI
import kotlin.reflect.KClass

@Contract
interface Properties {

    /** the List of properties sources. */
    val sources: List<PropertiesSource>

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
    fun <P : Any> read(key: String, clazz: KClass<P>): P? = read(key, clazz.java)

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

    fun URI.withoutScheme() =
        if (scheme.isNullOrEmpty())
            toString()
        else
            toString().substring(scheme.length + 1).trimStart('/')
}

class MapProperties(val map: MutableMap<String, Any>) : Properties {

    override val sources: List<PropertiesSource> = listOf(NullProperties)

    override fun asString(key: String): String? = map[key] as String?

    override fun asLong(key: String): Long? = map[key] as Long?

    override fun asInteger(key: String): Int? = map[key] as Int?

    override fun asURI(key: String): URI? = map[key] as URI?

    @Suppress("UNCHECKED_CAST")
    override fun <P> read(key: String, clazz: Class<P>): P? = map[key] as P?
}