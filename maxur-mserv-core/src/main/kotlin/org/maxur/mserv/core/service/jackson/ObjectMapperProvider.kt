package org.maxur.mserv.core.service.jackson

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.fasterxml.jackson.module.paranamer.ParanamerModule
import dk.nykredit.jackson.dataformat.hal.JacksonHALModule
import org.glassfish.hk2.api.Factory

class ObjectMapperProvider : Factory<ObjectMapper> {

    companion object {
        fun config(objectMapper: ObjectMapper): ObjectMapper = objectMapper.also {
            it.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE)
            it.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
            it.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
            it.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            it.registerModule(KotlinModule())
            it.registerModule(Jdk8Module())
            it.registerModule(ParanamerModule())
            it.registerModule(JavaTimeModule())
            it.registerModule(JacksonHALModule())
            it.registerModule(ParameterNamesModule())
        }
    }

    private val objectMapper: ObjectMapper = config(ObjectMapper())

    override fun dispose(instance: ObjectMapper?) = Unit
    override fun provide(): ObjectMapper = objectMapper

}