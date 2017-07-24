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
abstract class PropertiesServiceFactory {

    @Inject
    @Self
    private var descriptor: ActiveDescriptor<*>? = null

    lateinit var name: String

    @PostConstruct
    fun init() {
        name = descriptor?.name ?: "Undefined"
    }

    open fun make(source: PropertiesSource): PropertiesSource {
        val result = propertiesSource(source)
        result.open()
        return result
    }
    protected abstract fun propertiesSource(source: PropertiesSource): PropertiesSource
}

@Service(name = "None")
class PropertiesServiceFactoryNullImpl : PropertiesServiceFactory() {

    override fun propertiesSource(source: PropertiesSource): PropertiesSource {
        if (source.isConfigured())
            throw IllegalArgumentException("None properties source is configured")
        return source
    }
}

@Service(name = "Json")
class PropertiesServiceFactoryJsonImpl : PropertiesServiceFactory() {
    override fun propertiesSource(source: PropertiesSource): PropertiesSource =
            PropertiesServiceJacksonImpl(JsonFactory(), "application.json", source)
}

@Service(name = "Yaml")
class PropertiesServiceFactoryYamlImpl : PropertiesServiceFactory() {
    override fun propertiesSource(source: PropertiesSource): PropertiesSource =
            PropertiesServiceJacksonImpl(YAMLFactory(), "application.yaml", source)
}

@Service(name = "Hocon")
class PropertiesServiceFactoryHoconImpl : PropertiesServiceFactory() {
    override fun propertiesSource(source: PropertiesSource): PropertiesSource =
            PropertiesServiceHoconImpl(source)
}

