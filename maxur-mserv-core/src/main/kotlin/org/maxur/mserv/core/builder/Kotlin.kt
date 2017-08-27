package org.maxur.mserv.core.builder

import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.domain.Holder

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

    fun properties(init: PropertiesBuilder.BasePropertiesBuilder.() -> Unit) {
        val holder = PropertiesBuilder.BasePropertiesBuilder()
        properties += holder
        holder.apply { init() }
    }

    fun service(init: ServiceBuilder.() -> Unit) = ServiceBuilder().apply { init() }

    fun rest(init: ServiceBuilder.() -> Unit) = ServiceBuilder().apply {
        type = "Grizzly"
        properties = ":webapp"
        init()
    }
}
