@file:Suppress("unused")

package org.maxur.mserv.core.service.properties

import com.fasterxml.jackson.databind.ObjectMapper
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory
import com.typesafe.config.Config
import com.typesafe.config.ConfigException
import com.typesafe.config.ConfigFactory
import org.maxur.mserv.core.core.Result
import org.maxur.mserv.core.core.tryTo
import org.maxur.mserv.core.service.jackson.ObjectMapperProvider
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.Paths
import java.time.Duration

class PropertiesFactoryHoconImpl : PropertiesFactory() {

    override fun make(uri: URI?, rootKey: String?): Result<Exception, Properties> =
            tryTo { PropertiesSourceHoconImpl(uri, rootKey) }
}

internal class PropertiesSourceHoconImpl(
        private val sourceUri: URI? = null,
        rootKey: String? = null
) : Properties, PropertiesSource() {

    override val format = "Hocon"
    override val rootKey = rootKey ?: "DEFAULTS"

    private var root: Config = try {
        rootNode().getConfig(this.rootKey)
    } catch (e: ConfigException.Missing) {
        throw IllegalStateException("The properties source '${sourceUri ?: "<default>"}' not found. " +
                "You need create one with '${this.rootKey}' section")
    }

    private fun rootNode(): Config =
            if (sourceUri == null)
                ConfigFactory.load()
            else {
                when (sourceUri.scheme) {
                    null -> loadFrom(File(sourceUri.toString()))
                    "file" -> loadFrom(Paths.get(sourceUri).toFile())
                    "classpath" -> ConfigFactory.load(sourceUri.withoutScheme())
                    else -> throw IllegalArgumentException(
                            "Unsupported schema '${sourceUri.scheme}' to properties source. " +
                                    "Must be one of [file, classpath]"
                    )
                }
            }

    override val uri: URI get() = root.origin()?.url()?.toURI() ?: sourceUri ?: URI("")

    private val mapper = ObjectMapperProvider.config(ObjectMapper(HoconFactory()))

    @Suppress("UNCHECKED_CAST")
    override fun <P> read(key: String, clazz: Class<P>): P? =
            when (clazz) {
                String::class.java -> asString(key) as P?
                Int::class.java, Integer::class.java -> asInteger(key) as P?
                Long::class.java -> asLong(key) as P?
                URI::class.java -> asURI(key) as P?
                Double::class.java -> root.getDouble(key) as P?
                Duration::class.java -> root.getDuration(key) as P?
                else -> asObject(key, clazz) as P?
            }

    private fun loadFrom(file: File): Config =
            if (!file.exists())
                throw IllegalArgumentException("Properties file '${file.absolutePath}' is not found")
            else
                ConfigFactory.parseFile(file)

    override fun asURI(key: String): URI? {
        val string = asString(key)
        return when (string) {
            null -> null
            else -> URI.create(string)
        }
    }

    private fun asObject(key: String, clazz: Class<*>): Any? {
        try {
            return getValue(key, { it.getObject(key) })
                    ?.let { mapper.readValue(it.render(), clazz) }
        } catch (e: Exception) {
            when (e) {
                is IOException, is ConfigException.WrongType -> {
                    throw IllegalStateException("Configuration parameter '$key' is not parsed.", e)
                }
                else -> throw e
            }
        }
    }

    override fun asString(key: String): String? = getValue(key, { it.getString(key) })
    override fun asLong(key: String): Long? = getValue(key, { it.getLong(key) })
    override fun asInteger(key: String): Int? = getValue(key, { it.getInt(key) })

    private fun <T> getValue(key: String, transform: (Config) -> T?): T? = try {
        root.let { transform.invoke(it) }
    } catch (e: ConfigException.Missing) {
        throw IllegalStateException("Configuration parameter '$key' is not found.", e)
    }
}