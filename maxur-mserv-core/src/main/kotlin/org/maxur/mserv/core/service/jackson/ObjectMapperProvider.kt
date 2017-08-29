package org.maxur.mserv.core.service.jackson

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.fasterxml.jackson.module.paranamer.ParanamerModule
import dk.nykredit.jackson.dataformat.hal.JacksonHALModule
import org.glassfish.hk2.api.Factory
import org.jvnet.hk2.annotations.Service

/**
 * Factory for [ObjectMapper] instance.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>26.08.2017</pre>
 */
@Service
class ObjectMapperProvider : Factory<ObjectMapper> {

    companion object {
        /**
         *   Instance of [ObjectMapper].
         */
        val objectMapper = config(ObjectMapper())

        /**
         * Configure [ObjectMapper] instance
         */
        fun config(mapper: ObjectMapper): ObjectMapper {
            return mapper
                .setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                // .registerModule(KotlinModule())  XXX error on Path class deserialization
                .registerModule(Jdk8Module())
                .registerModule(ParanamerModule())
                .registerModule(JavaTimeModule())
                .registerModule(JacksonHALModule())
                .registerModule(ParameterNamesModule())
        }
    }

    /** {@inheritDoc} */
    override fun dispose(instance: ObjectMapper?) = Unit

    /** {@inheritDoc} */
    override fun provide(): ObjectMapper = objectMapper

}