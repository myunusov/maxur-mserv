@file:Suppress("unused")

package org.maxur.mserv.core.service.properties.hocon

import com.fasterxml.jackson.databind.ObjectMapper
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory
import com.typesafe.config.Config
import com.typesafe.config.ConfigException
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigObject
import org.jvnet.hk2.annotations.Service
import org.maxur.mserv.core.service.properties.PropertiesService
import org.maxur.mserv.core.service.properties.PropertiesServiceFactory
import org.maxur.mserv.core.service.properties.PropertiesSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.Paths
import java.util.function.Function

/**
 * The Properties Service Factory Hocon Implementation
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
@Service(name = "Hocon")
class PropertiesServiceFactoryHoconImpl : PropertiesServiceFactory() {

    companion object {
        val log: Logger = LoggerFactory.getLogger(PropertiesServiceFactoryHoconImpl::class.java)
    }

    override fun make(source: PropertiesSource): PropertiesService {
        val uri: URI? = source.uri
        val config = when {
                    uri == null -> ConfigFactory.load()
                    uri.scheme == null -> loadFrom(File(uri.toString()))
                    uri.scheme == "file" -> loadFrom(Paths.get(uri).toFile())
                    else -> throw IllegalArgumentException(
                            """Unsupported url '${uri.scheme}' to properties source. Must be one of [file]"""
                    )
                }
        val rootKey = getRootKey(source)
        val effectiveSource = effectiveSourceFrom(source)
        try {
            return PropertiesServiceHoconImpl(config.getConfig(rootKey), effectiveSource)
        } catch(e: ConfigException.Missing) {
            throw IllegalArgumentException (
                    """The '${effectiveSource.uri}' file not found. Add file '$' with '$rootKey' section"""
            )
        }
    }

    private fun effectiveSourceFrom(source: PropertiesSource): PropertiesSource =
            PropertiesSource(
                    source.format,
                    source.uri ?: URI.create("classpath:///application.conf"),
                    source.rootKey ?: "DEFAULTS"
            )

    private fun loadFrom(file: File): Config {
        if (!file.exists()) {
            throw IllegalArgumentException("Properties file '${file.absolutePath}' is not found")
        }
        return ConfigFactory.parseFile(file)
    }

    private fun getRootKey(source: PropertiesSource): String {
        if (source.rootKey != null)
            return source.rootKey
        else {
            log.info("Root key of properties source is not configured. So it's 'DEFAULTS' by default.")
            return "DEFAULTS"
        }
    }

    class PropertiesServiceHoconImpl(val config: Config, override val source: PropertiesSource) : PropertiesService {

        private val objectMapper = ObjectMapper(HoconFactory())

        @Suppress("UNCHECKED_CAST")
        override fun <P> read(key: String, clazz: Class<P>): P? {
            when (clazz) {
                String::class.java -> return asString(key) as P
                Integer::class.java -> return asInteger(key) as P
                Long::class.java -> return asLong(key) as P
                java.net.URI::class.java -> return asURI(key) as P
                else -> return asObject(key, clazz) as P
            }
        }

        override fun asURI(key: String): URI? {
            val string = asString(key)
            return when (string) {
                null -> null
                else -> URI.create(string)
            }
        }

        private fun asObject(key: String, clazz: Class<*>): Any? {
            try {
                val configObject: ConfigObject? = findValue(Function { it?.getObject(key) })
                if (configObject != null) {
                    return objectMapper.readValue(configObject.render(), clazz)
                } else {
                    return null
                }
            } catch (e: IOException) {
                log.error("Configuration parameter '$key' is not parsed.")
                log.error(e.message, e)
                return null
            }
        }

        override fun asString(key: String): String? {
            return getValue(key, Function { it?.getString(key) })
        }

        override fun asLong(key: String): Long? {
            return getValue(key, Function { it?.getLong(key) })
        }

        override fun asInteger(key: String): Int? {
            return getValue(key, Function { it?.getInt(key) })
        }

        private fun <T> getValue(key: String, transform: Function<Config?, T?>): T? {
            try {
                val value = findValue(transform)
                log.debug("Configuration parameter '$key' = '$value'")
                return value
            } catch (e: ConfigException.Missing) {
                log.error("Configuration parameter '$key' is not found.")
                throw e
            }
        }

        private fun <T> findValue(transform: Function<Config?, T?>): T? {
            return transform.apply(config)
        }

    }
}