package org.maxur.mserv.core.service.hk2

import org.maxur.mserv.core.MicroService


object DSL {

    fun service(init: MicroServiceBuilder.() -> Unit): MicroService {
        try {
            return MicroServiceBuilder(init)
                .build()
        } catch (e: Exception) {
            return object: MicroService {
                override var name: String  = "noname"
                override fun <T> bean(clazz: Class<T>): T? = throw UnsupportedOperationException()
                override fun start() = Unit
                override fun deferredStop() = Unit
                override fun stop()  = Unit
                override fun deferredRestart() = Unit
            }
        }
    }
}