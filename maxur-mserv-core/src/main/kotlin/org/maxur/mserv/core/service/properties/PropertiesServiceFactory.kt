@file:Suppress("unused")

package org.maxur.mserv.core.service.properties

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.typesafe.config.ConfigException
import org.glassfish.hk2.api.ActiveDescriptor
import org.glassfish.hk2.api.Self
import org.jvnet.hk2.annotations.Contract
import org.jvnet.hk2.annotations.Service
import java.net.URI
import javax.annotation.PostConstruct
import javax.inject.Inject

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
@Contract
abstract class PropertiesServiceFactory(val defaultFileName: String, val defaultRooKey: String = "/") {

    @Inject
    @Self
    private var descriptor: ActiveDescriptor<*>? = null

    lateinit var name: String

    @PostConstruct
    fun init() {
        name = descriptor?.name ?: "Undefined"
    }

    open fun make(source: PropertiesSource): PropertiesService {
        val effectiveSource = effectiveSourceFrom(source)
        try {
            return propertiesService(effectiveSource)
        } catch(e: ConfigException.Missing) {
            throw IllegalArgumentException (
                    """The '${effectiveSource.uri}' file not found. Add it with '${effectiveSource.rootKey}' section"""
            )
        }
    }

    protected abstract fun propertiesService(effectiveSource: PropertiesSource): PropertiesService

    private fun effectiveSourceFrom(source: PropertiesSource): PropertiesSource {
        return PropertiesSource(
                source.format,
                source.uri ?: URI.create("classpath:///$defaultFileName"),
                source.rootKey ?: defaultRooKey
        )
    }
}

@Service(name = "None")
class PropertiesServiceFactoryNullImpl : PropertiesServiceFactory("none") {
    override fun propertiesService(effectiveSource: PropertiesSource): PropertiesService =
            object : PropertiesService {
        override val source = effectiveSource
        override fun asString(key: String): String? = error(key)
        override fun asLong(key: String): Long? = error(key)
        override fun asInteger(key: String): Int? = error(key)
        override fun asURI(key: String): URI? = error(key)
        override fun <P> read(key: String, clazz: Class<P>): P? = error(key)
        private fun <T> error(key: String): T =
                throw IllegalStateException("Service Configuration is not found. Key '$key' unresolved")
    }
    override fun make(source: PropertiesSource): PropertiesService =
            if (source.isConfigured())
                throw IllegalArgumentException("None properties source is configured")
            else
                propertiesService(source)
}

@Service(name = "Json")
class PropertiesServiceFactoryJsonImpl: PropertiesServiceFactory("application.json") {
    override fun propertiesService(effectiveSource: PropertiesSource) =
            PropertiesServiceJacksonImpl(effectiveSource, JsonFactory())
}

@Service(name = "Yaml")
class PropertiesServiceFactoryYamlImpl: PropertiesServiceFactory("application.yaml") {
    override fun propertiesService(effectiveSource: PropertiesSource) =
            PropertiesServiceJacksonImpl(effectiveSource, YAMLFactory())
}

@Service(name = "Hocon")
class PropertiesServiceFactoryHoconImpl : PropertiesServiceFactory("application.conf", "DEFAULTS") {
    override fun propertiesService(effectiveSource: PropertiesSource): PropertiesServiceHoconImpl
            = PropertiesServiceHoconImpl(effectiveSource)
}