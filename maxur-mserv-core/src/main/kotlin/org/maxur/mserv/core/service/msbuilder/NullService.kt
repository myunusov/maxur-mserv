package org.maxur.mserv.core.service.msbuilder

import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.MicroService

class NullService : MicroService(loc) {

    companion object {
        val loc: Locator = object : Locator {
            override fun <T> service(clazz: Class<T>): T? = throw UnsupportedOperationException()
            override fun <T> service(clazz: Class<T>, name: String?): T? = throw UnsupportedOperationException()
            override fun names(clazz: Class<*>): List<String> = throw UnsupportedOperationException()
            override fun property(key: String): String = throw UnsupportedOperationException()
            override fun <R> properties(key: String, clazz: Class<R>): R? = throw UnsupportedOperationException()
            override fun shutdown() = throw UnsupportedOperationException()
            override fun <T> implementation(): T= throw UnsupportedOperationException()
        }
    }

    override fun shutdown() = Unit
    override fun launch() = Unit
    override fun relaunch() = Unit
    override val version: String = ""
    override var name: String  = "noname"
    override fun <T> bean(clazz: Class<T>): T? = throw UnsupportedOperationException()
}