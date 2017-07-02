package org.maxur.mserv.core.service.hk2

import org.maxur.mserv.core.MicroService


object DSL {

    fun service(init: MicroServiceBuilder.() -> Unit): MicroService {
        try {
            return MicroServiceBuilder(init)
                .build()
        } catch (e: Exception) {
            return object: MicroService() {
                override fun shutdown() = Unit
                override fun launch() = Unit
                override val version: String = ""
                override var name: String  = "noname"
                override fun <T> bean(clazz: Class<T>): T? = throw UnsupportedOperationException()
            }
        }
    }
}