package org.maxur.mserv.core.service.properties

import java.net.URI

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>27.08.2017</pre>
 */
class CompositeProperties(val properties: List<Properties> = emptyList()) : Properties {

    override val sources = properties.flatMap { it.sources }

    override fun asString(key: String): String? = properties.map({ it.asString(key) }).firstOrNull()

    override fun asLong(key: String): Long? = properties.map({ it.asLong(key) }).firstOrNull()

    override fun asInteger(key: String): Int? = properties.map({ it.asInteger(key) }).firstOrNull()

    override fun asURI(key: String): URI? = properties.map({ it.asURI(key) }).firstOrNull()

    override fun <P> read(key: String, clazz: Class<P>): P? = properties.map({ it.read(key, clazz) }).firstOrNull()
}