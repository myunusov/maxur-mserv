package org.maxur.mserv.core.service.msbuilder

import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.domain.Holder

object Kotlin {
    fun service(init: KBuilder.() -> Unit): MicroService = KBuilder(init).build()
}

class KBuilder(): MicroServiceBuilder() {

    var title: String = "Anonymous"
        set(value) {
            titleHolder = Holder.string(value)
        }

    var packages: String = ""
        set(value) {
            packagesHolder.addAll(value.split("\\s*,\\s*"))
        }


    constructor(init: KBuilder.() -> Unit) : this() {
        init()
    }

    fun withoutProperties() {
        propertiesHolder = PropertiesHolder.NullPropertiesHolder
    }

    fun properties(init: PropertiesHolder.BasePropertiesHolder.() -> Unit) {
        val holder = PropertiesHolder.BasePropertiesHolder()
        propertiesHolder = holder
        holder.apply { init() }
    }

    fun service(init: ServiceHolder.() -> Unit) = ServiceHolder().apply { init() }

    fun rest(init: ServiceHolder.() -> Unit) = ServiceHolder().apply {
        type = "Grizzly"
        properties = ":webapp"
        init()
    }    
}


