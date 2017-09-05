package org.maxur.mserv.core.builder

import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.domain.Holder
import org.maxur.mserv.core.service.properties.PropertiesFactoryHoconImpl
import org.maxur.mserv.core.service.properties.PropertiesFactoryJsonImpl
import org.maxur.mserv.core.service.properties.PropertiesFactoryYamlImpl

object Kotlin {
    fun service(init: KBuilder.() -> Unit): MicroService = KBuilder(init).build()
}

class KBuilder() : MicroServiceBuilder() {

    var name: String = "Anonymous"
        set(value) {
            nameHolder = Holder.string(value)
        }

    constructor(init: KBuilder.() -> Unit) : this() {
        init()
    }

    fun withoutProperties() {
        properties += PropertiesBuilder.NullPropertiesBuilder
    }

    fun file(init: PropertiesBuilder.BasePropertiesBuilder.() -> Unit) =
            PropertiesBuilder.BasePropertiesBuilder().apply { init() }

    fun service(init: ServiceBuilder.() -> Unit) = ServiceBuilder().apply { init() }

    fun rest(init: ServiceBuilder.() -> Unit) = ServiceBuilder().apply {
        type = "Grizzly"
        properties = ":webapp"
        init()
    }
}

fun hocon(init: PredefinedPropertiesBuilder.() -> Unit = {})
    = object : PredefinedPropertiesBuilder("hocon", PropertiesFactoryHoconImpl(), init) {}

fun json(init: PredefinedPropertiesBuilder.() -> Unit = {})
    = object : PredefinedPropertiesBuilder("json", PropertiesFactoryJsonImpl(), init) {}

fun yaml(init: PredefinedPropertiesBuilder.() -> Unit = {})
    = object : PredefinedPropertiesBuilder("yaml", PropertiesFactoryYamlImpl(), init) {}