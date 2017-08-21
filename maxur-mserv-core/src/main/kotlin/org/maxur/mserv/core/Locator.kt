package org.maxur.mserv.core

import javax.annotation.PostConstruct
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

@Suppress("UNCHECKED_CAST")
abstract class Locator(val name: String) {

    object NullLocator : Locator("null-locator") {
        override fun <T> service(clazz: Class<T>, name: String?): T? = error()
        override fun <T> services(clazz: Class<T>): List<T> = error()
        override fun names(clazz: Class<*>): List<String> = error()
        override fun <R> property(key: String, clazz: Class<R>): R? = error()
        override fun close() = error<Unit>()
        override fun <T> implementation(): T = error()
        private fun <T> error(): T =
                throw IllegalStateException("Service Locator is not initialized.")
    }

    interface LocatorHolder {
        fun get(): Locator
        fun put(value: Locator)
        fun remove(name: String)
    }

    companion object {

        var holder: LocatorHolder = object : LocatorHolder {
            var locator: Locator = NullLocator

            override fun get() = locator

            override fun put(value: Locator) {
                locator = if (locator is NullLocator)
                    value
                else
                    throw IllegalStateException("You can have only one service locator per microservice")
            }

            override fun remove(name: String) {
                locator = if (locator.name == name)
                    NullLocator
                else
                    throw IllegalArgumentException("Locator '$name' is not found")
            }
        }

        var current: Locator
            get() = holder.get()
            set(value) = holder.put(value)

        private fun removeLocator(name: String) {
            holder.remove(name)
        }

        fun <T> service(clazz: Class<T>): T? = current.service(clazz)
        fun <T : Any> service(clazz: KClass<T>): T? = current.service(clazz)
        fun <T : Any> service(clazz: KClass<T>, name: String): T? = current.service(clazz, name)
        fun <T : Any> service(parameter: KParameter): T? = current.service(parameter)
        fun <T : Any> services(clazz: KClass<T>): List<T> = current.services(clazz)
        fun shutdown() = if (!(current is NullLocator)) current.shutdown() else Unit
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
    abstract fun <T> service(clazz: Class<T>, name: String?): T?

    fun <T : Any> services(clazz: KClass<T>): List<T> = services(clazz.java)
    abstract fun <T> services(clazz: Class<T>): List<T>

    fun <T : Any> names(clazz: KClass<T>): List<String> = names(clazz.java)
    abstract fun names(clazz: Class<*>): List<String>

    fun property(key: String): String? = property(key, String::class)
    fun <T : Any> property(key: String, clazz: KClass<T>): T? = property(key, clazz.java)
    abstract fun <T> property(key: String, clazz: Class<T>): T?

    abstract fun <T> implementation(): T

    fun shutdown() {
        removeLocator(this.name)
        close()
    }

    protected abstract fun close()

    override fun toString(): String = name

}

