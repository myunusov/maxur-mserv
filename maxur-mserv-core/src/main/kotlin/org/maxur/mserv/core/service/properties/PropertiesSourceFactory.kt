@file:Suppress("unused")

package org.maxur.mserv.core.service.properties

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.glassfish.hk2.api.ActiveDescriptor
import org.glassfish.hk2.api.Self
import org.jvnet.hk2.annotations.Contract
import org.jvnet.hk2.annotations.Service
import javax.annotation.PostConstruct
import javax.inject.Inject

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
@Contract
abstract class PropertiesSourceFactory {
    @Inject
    @Self
    private var descriptor: ActiveDescriptor<*>? = null

    lateinit var name: String
    @PostConstruct
    fun init() {
        name = descriptor?.name ?: "Undefined"
    }
    abstract fun make(source: PropertiesSource): PropertiesSource
}

@Service(name = "None")
class PropertiesSourceFactoryNullImpl : PropertiesSourceFactory() {
    override fun make(source: PropertiesSource): PropertiesSource {
        if (source.isConfigured())
            throw IllegalArgumentException("None properties source is configured")
        return source
    }
}

@Service(name = "Json")
class PropertiesSourceFactoryJsonImpl : PropertiesSourceFactory() {
    override fun make(source: PropertiesSource): PropertiesSource =
            PropertiesServiceJacksonImpl(JsonFactory(), "json", source)
}

@Service(name = "Yaml")
class PropertiesSourceFactoryYamlImpl : PropertiesSourceFactory() {
    override fun make(source: PropertiesSource): PropertiesSource =
            PropertiesServiceJacksonImpl(YAMLFactory(), "yaml", source)
}

@Service(name = "Hocon")
class PropertiesSourceFactoryHoconImpl : PropertiesSourceFactory() {
    override fun make(source: PropertiesSource): PropertiesSource =
            PropertiesServiceHoconImpl(source)
}

