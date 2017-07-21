package org.maxur.mserv.core.service.properties

import com.fasterxml.jackson.core.JsonFactory
import com.typesafe.config.ConfigException
import org.jvnet.hk2.annotations.Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI


/**
 * The Properties Service Factory Json Implementation
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
@Service(name = "Json")
class PropertiesServiceFactoryJsonImpl: PropertiesServiceFactory() {

    companion object {
        val log: Logger = LoggerFactory.getLogger(PropertiesServiceFactoryYamlImpl::class.java)
    }

    override fun make(source: PropertiesSource): PropertiesService {
        val effectiveSource = effectiveSourceFrom(source)
        try {
            return PropertiesServiceJacksonImpl(effectiveSource, JsonFactory())
        } catch(e: ConfigException.Missing) {
            throw IllegalArgumentException (
                    """The '${effectiveSource.uri}' file not found. Add it with '${effectiveSource.rootKey}' section"""
            )
        }
    }

    private fun effectiveSourceFrom(source: PropertiesSource): PropertiesSource =
            PropertiesSource(
                    source.format,
                    source.uri ?: URI.create("classpath:///application.json"),
                    source.rootKey ?: "/"
            )

}