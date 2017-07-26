package org.maxur.mserv.core

import kotlin.reflect.KClass
import kotlin.reflect.KParameter

@Suppress("UNCHECKED_CAST")
interface Locator {

    companion object {
        lateinit var current: Locator
        fun <T> service(clazz: Class<T>): T? = current.service(clazz)
        fun <T : Any> service(clazz: KClass<T>): T? = current.service(clazz)
        fun <T : Any> service(clazz: KClass<T>, name: String): T? = current.service(clazz, name)
        fun <T : Any> service(parameter: KParameter): T? = current.service(parameter)
        fun <T : Any> services(clazz: KClass<T>): List<T> = current.services(clazz)
        fun shutdown() = current.shutdown()
    }

    fun <T> locate(clazz: Class<T>, name: String): T = service(clazz, name) ?:
            throw IllegalStateException(
                    "Service '$name' is not supported. Try one from this list: ${names(clazz)}"
            )

    fun <T : Any> locate(clazz: KClass<T>, name: String): T = locate(clazz.java, name)

    fun <T> service(clazz: Class<T>): T?

    fun <T : Any> service(clazz: KClass<T>): T? = service(clazz.java)

    fun <T : Any> service(parameter: KParameter): T? = service(parameter.type.classifier as KClass<T>)

    fun <T : Any> service(clazz: KClass<T>, name: String?): T? = service(clazz.java, name)

    fun <T> service(clazz: Class<T>, name: String?): T?

    fun <T : Any> services(clazz: KClass<T>): List<T> = services(clazz.java)

    fun <T> services(clazz: Class<*>): List<T>

    fun names(clazz: Class<*>): List<String>

    fun <T : Any> names(clazz: KClass<T>): List<String> = names(clazz.java)

    fun property(key: String): String

    fun <R> properties(key: String, clazz: Class<R>): R?

    fun <R : Any> properties(key: String, clazz: KClass<R>): R? = properties(key, clazz.java)

    fun shutdown()

    fun <T> implementation(): T

}
