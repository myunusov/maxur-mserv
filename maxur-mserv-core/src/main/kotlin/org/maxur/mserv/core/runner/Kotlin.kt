package org.maxur.mserv.core.runner

import org.maxur.mserv.core.domain.Holder
import org.maxur.mserv.core.service.properties.PropertiesFactoryHoconImpl
import org.maxur.mserv.core.service.properties.PropertiesFactoryJsonImpl
import org.maxur.mserv.core.service.properties.PropertiesFactoryYamlImpl

object Kotlin {
    fun runner(init: KRunner.() -> Unit) = KRunner(init)
}

class KRunner() : MicroServiceRunner() {

    var name: String = "Anonymous"
        set(value) {
            nameHolder = Holder.string(value)
        }

    constructor(init: KRunner.() -> Unit) : this() {
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