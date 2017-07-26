@file:Suppress("unused")

package org.maxur.mserv.core.service.properties

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.jvnet.hk2.annotations.Contract
import org.jvnet.hk2.annotations.Service

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
@Contract
abstract class PropertiesSourceFactory {
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
            PropertiesSourceJacksonImpl(JsonFactory(), "json", source)
}

@Service(name = "Yaml")
class PropertiesSourceFactoryYamlImpl : PropertiesSourceFactory() {
    override fun make(source: PropertiesSource): PropertiesSource =
            PropertiesSourceJacksonImpl(YAMLFactory(), "yaml", source)
}

@Service(name = "Hocon")
class PropertiesSourceFactoryHoconImpl : PropertiesSourceFactory() {
    override fun make(source: PropertiesSource): PropertiesSource =
            PropertiesSourceHoconImpl(source)
}

