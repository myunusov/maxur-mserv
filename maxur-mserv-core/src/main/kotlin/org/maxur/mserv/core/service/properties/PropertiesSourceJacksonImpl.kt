@file:Suppress("unused")

package org.maxur.mserv.core.service.properties

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.maxur.mserv.core.core.Result
import org.maxur.mserv.core.core.tryTo
import org.maxur.mserv.core.service.jackson.ObjectMapperProvider
import java.io.File
import java.io.InputStream
import java.net.URI
import java.nio.file.Paths

class PropertiesFactoryJsonImpl : PropertiesFactory() {
    override fun make(uri: URI?, rootKey: String?): Result<Exception, Properties> =
            tryTo { PropertiesSourceJacksonImpl(JsonFactory(), "json", uri, rootKey) }
}

class PropertiesFactoryYamlImpl : PropertiesFactory() {
    override fun make(uri: URI?, rootKey: String?): Result<Exception, Properties> =
            tryTo { PropertiesSourceJacksonImpl(YAMLFactory(), "yaml", uri, rootKey) }
}

internal class PropertiesSourceJacksonImpl(
        factory: JsonFactory,
        defaultExt: String,
        uri: URI? = null,
        rootKey: String? = null
) : Properties, PropertiesSource() {

    override val rootKey = rootKey ?: "/"
    override val format = defaultExt.capitalize()
    override val uri = uri ?: URI.create("classpath:///application.$defaultExt")

    private val mapper = ObjectMapperProvider.config(ObjectMapper(factory))

    private var root: JsonNode = (
            if (rootKey == null) rootNode(this.uri) else rootNode(this.uri)?.get(this.rootKey))
            ?: throw IllegalStateException(
            "The properties source '${this.uri}' not found. You need create one with '${this.rootKey}' section"
    )

    private fun rootNode(uri: URI): JsonNode? = when (uri.scheme) {
        null -> mapper.readTree(File(uri.toString()))
        "file" -> mapper.readTree(Paths.get(uri).toFile())
        "classpath" -> inputStreamByResource(uri)?.let { mapper.readTree(it) }
        else -> throw IllegalArgumentException(
                """Unsupported schema '${uri.scheme}' to properties source. Must be one of [file, classpath]"""
        )
    }

    private fun inputStreamByResource(uri: URI): InputStream? =
            this::class.java.getResourceAsStream("/" + uri.withoutScheme())

    override fun asString(key: String): String? = node(key).asText()
    override fun asLong(key: String): Long? = node(key).asLong()
    override fun asInteger(key: String): Int? = node(key).asInt()
    override fun asURI(key: String): URI? = mapper.treeToValue(node(key), URI::class.java)
    override fun <P> read(key: String, clazz: Class<P>): P? {
        try {
            return mapper.treeToValue(node(key), clazz)
        } catch (e: JsonMappingException) {
            throw IllegalStateException("Configuration parameter '$key' is not parsed.", e)
        }
    }

    private fun node(key: String) = root.get(key) ?:
            throw IllegalStateException("Configuration parameter '$key' is not found.")

}