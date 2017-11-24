package org.maxur.mserv.frame.service.jackson

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.fasterxml.jackson.module.paranamer.ParanamerModule
import dk.nykredit.jackson.dataformat.hal.JacksonHALModule

/**
 * Factory for [ObjectMapper] instance.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>26.08.2017</pre>
 */
object ObjectMapperProvider {

    /**
     *   Instance of [ObjectMapper].
     */
    @JvmStatic
    val objectMapper = config(jacksonObjectMapper())

    /**
     * Configure [ObjectMapper] instance
     */
    fun config(mapper: ObjectMapper): ObjectMapper = mapper.apply { configuration() }

    private fun ObjectMapper.configuration() {
        setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE)
        setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
        setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        registerModule(Jdk8Module())
        registerModule(ParanamerModule())
        registerModule(JavaTimeModule())
        registerModule(JacksonHALModule())
        registerModule(ParameterNamesModule())
    }
}