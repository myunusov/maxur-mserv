package org.maxur.mserv.core.service.hk2

import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.MicroService


object DSL {

    fun service(init: MicroServiceBuilder.() -> Unit): MicroService {
        try {
            return MicroServiceBuilder(init)
                .build()
        } catch (e: Exception) {
            val locator: Locator = object : Locator {
                override fun <T> service(clazz: Class<T>): T? = throw UnsupportedOperationException()
                override fun <T> service(clazz: Class<T>, name: String?): T? = throw UnsupportedOperationException()
                override fun names(clazz: Class<*>): List<String> = throw UnsupportedOperationException()
                override fun property(key: String): String = throw UnsupportedOperationException()
                override fun <R> properties(key: String, clazz: Class<R>): R? = throw UnsupportedOperationException()
                override fun shutdown() = throw UnsupportedOperationException()
                override fun <T> implementation(): T= throw UnsupportedOperationException()
            }
            return object: MicroService(locator) {
                override fun shutdown() = Unit
                override fun launch() = Unit
                override val version: String = ""
                override var name: String  = "noname"
                override fun <T> bean(clazz: Class<T>): T? = throw UnsupportedOperationException()
            }
        }
    }
}