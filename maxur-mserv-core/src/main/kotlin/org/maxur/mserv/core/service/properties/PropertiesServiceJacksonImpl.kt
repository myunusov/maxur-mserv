package org.maxur.mserv.core.service.properties

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.InputStream
import java.net.URI
import java.nio.file.Paths

class PropertiesServiceJacksonImpl(
        factory: JsonFactory,
        val defaultFileName: String,
        val rawSource: PropertiesSource
) : PropertiesSource {

    override val format: String get() = rawSource.format
    override val uri: URI  get() = rawSource.uri ?: URI.create("classpath:///$defaultFileName")
    override val rootKey: String get() = rawSource.rootKey ?: "/"

    private val mapper = ObjectMapper(factory)
    private var root: JsonNode

    init {
        root = rootNode(uri)?.path(rootKey) ?:
            throw IllegalArgumentException("""The '$uri' file not found. Add it with '$rootKey' section""")
    }

    private fun rootNode(uri: URI): JsonNode? = when {
        uri.scheme == null -> mapper.readTree(File(uri.toString()))
        uri.scheme == "file" -> mapper.readTree(Paths.get(uri).toFile())
        uri.scheme == "classpath" -> mapper.readTree(inputStreamByResource(uri))
        else -> throw IllegalArgumentException(
                """Unsupported schema '${uri.scheme}' to properties source. Must be one of [file, classpath]"""
        )
    }

    private fun inputStreamByResource(uri: URI): InputStream {
        val name = "/" + uri.toString().substring("classpath".length + 1).trimStart('/')
        return this::class.java.getResourceAsStream(name) ?:
                throw IllegalArgumentException("""Resource '$name' is not found""")
    }

    override fun asString(key: String): String? = root.path(key).asText()
    override fun asLong(key: String): Long? = root.path(key).asLong()
    override fun asInteger(key: String): Int? = root.path(key).asInt()
    override fun asURI(key: String): URI? = mapper.treeToValue(root.path(key), URI::class.java)
    override fun <P> read(key: String, clazz: Class<P>): P? = mapper.treeToValue(root.path(key), clazz)
}