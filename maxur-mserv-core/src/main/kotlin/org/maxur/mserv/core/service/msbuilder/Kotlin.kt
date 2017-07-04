package org.maxur.mserv.core.service.msbuilder

import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.domain.Holder


object Kotlin {

    fun service(init: KBuilder.() -> Unit): MicroService {
        try {
            return KBuilder(init)
                .build()
        } catch (e: Exception) {
            return NullService()
        }
    }
}

class KBuilder(): MSBuilder() {

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

    fun properties(init: PropertiesHolder.() -> Unit) {
        propertiesHolder = PropertiesHolder().apply { init() }
    }

    fun service(init: ServiceHolder.() -> Unit) = ServiceHolder().apply { init() }

    fun rest(init: ServiceHolder.() -> Unit) = ServiceHolder().apply {
        type = "Grizzly"
        properties = ":webapp"
        init()
    }    
}


