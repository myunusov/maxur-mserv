package org.maxur.mserv.core.service.properties

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.maxur.mserv.core.service.jackson.ObjectMapperProvider
import java.io.File
import java.io.InputStream
import java.net.URI
import java.nio.file.Paths

internal class PropertiesSourceJacksonImpl(
        factory: JsonFactory,
        private val defaultFormat: String,
        private val rawSource: PropertiesSource
) : Properties, PropertiesSource {

    override val format get() = defaultFormat.capitalize()
    override val uri: URI get() = rawSource.uri ?: URI.create("classpath:///application.$defaultFormat")
    override val rootKey get() = rawSource.rootKey

    private val mapper = ObjectMapperProvider.config(ObjectMapper(factory))
    private var root: JsonNode = (
            if (rawSource.rootKey != null)
                rootNode(uri)?.get(rawSource.rootKey)
            else
                rootNode(uri)
            ) ?: throw IllegalStateException("The properties source is not found")

    private fun rootNode(uri: URI): JsonNode? = when {
        uri.scheme == null -> mapper.readTree(File(uri.toString()))
        uri.scheme == "file" -> mapper.readTree(Paths.get(uri).toFile())
        uri.scheme == "classpath" -> inputStreamByResource(uri)?.let { mapper.readTree(it) }
        else -> throw IllegalArgumentException(
                """Unsupported schema '${uri.scheme}' to properties source. Must be one of [file, classpath]"""
        )
    }

    private fun inputStreamByResource(uri: URI): InputStream? =
            this::class.java.getResourceAsStream(
                    "/" + uri.toString().substring("classpath".length + 1).trimStart('/')
            )

    override fun asString(key: String): String? = node(key).asText()
    override fun asLong(key: String): Long? = node(key).asLong()
    override fun asInteger(key: String): Int? = node(key).asInt()
    override fun asURI(key: String): URI? = mapper.treeToValue(node(key), URI::class.java)
    override fun <P> read(key: String, clazz: Class<P>): P? {
        try {
            return mapper.treeToValue(node(key), clazz)
        } catch(e: JsonMappingException) {
            throw IllegalStateException("Configuration parameter '$key' is not parsed.", e)
        }
    }

    private fun node(key: String) = root().get(key) ?:
            throw IllegalStateException("Configuration parameter '$key' is not found.")

    fun root(): JsonNode = root ?: throw IllegalStateException("Resource '$uri' is closed")

}