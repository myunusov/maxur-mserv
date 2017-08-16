package org.maxur.mserv.core

import javax.annotation.PostConstruct
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

@Suppress("UNCHECKED_CAST")
interface Locator {

    private object NullLocator : Locator {
        override fun <T> service(clazz: Class<T>, name: String?): T? = error()
        override fun <T> services(clazz: Class<T>): List<T> = error()
        override fun names(clazz: Class<*>): List<String> = error()
        override fun <R> property(key: String, clazz: Class<R>): R? = error()
        override fun shutdown(): Unit = error()
        override fun <T> implementation(): T = error()
        private fun <T> error(): T =
                throw IllegalStateException("Service Locator is not initialized.")
    }

    companion object {
        private var localCurrent: ThreadLocal<Locator> = ThreadLocal()

        var current: Locator = NullLocator
            get() = localCurrent.get() ?: field
            set(value) {
                localCurrent.set(value)
                field = value
            }

        fun <T> service(clazz: Class<T>): T? = current.service(clazz)
        fun <T : Any> service(clazz: KClass<T>): T? = current.service(clazz)
        fun <T : Any> service(clazz: KClass<T>, name: String): T? = current.service(clazz, name)
        fun <T : Any> service(parameter: KParameter): T? = current.service(parameter)
        fun <T : Any> services(clazz: KClass<T>): List<T> = current.services(clazz)
        fun shutdown() = current.shutdown()
    }

    @PostConstruct
    fun init() {
        Locator.current = this
    }

    fun <T : Any> locate(clazz: KClass<T>, name: String): T = locate(clazz.java, name)
    fun <T> locate(clazz: Class<T>, name: String): T = service(clazz, name) ?:
            throw IllegalStateException(
                    "Service '$name' is not supported. Try one from this list: ${names(clazz)}"
            )

    fun <T : Any> service(parameter: KParameter): T? = service(parameter.type.classifier as KClass<T>)
    fun <T : Any> service(clazz: KClass<T>, name: String? = null): T? = service(clazz.java, name)
    fun <T> service(clazz: Class<T>): T? = service(clazz, null)
    fun <T> service(clazz: Class<T>, name: String?): T?

    fun <T : Any> services(clazz: KClass<T>): List<T> = services(clazz.java)
    fun <T> services(clazz: Class<T>): List<T>


    fun <T : Any> names(clazz: KClass<T>): List<String> = names(clazz.java)
    fun names(clazz: Class<*>): List<String>

    fun property(key: String): String? = property(key, String::class)
    fun <T : Any> property(key: String, clazz: KClass<T>): T? = property(key, clazz.java)
    fun <T> property(key: String, clazz: Class<T>): T?

    fun shutdown()

    fun <T> implementation(): T

}
