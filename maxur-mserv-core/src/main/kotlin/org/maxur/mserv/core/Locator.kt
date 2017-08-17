package org.maxur.mserv.core

import java.lang.reflect.Field
import javax.annotation.PostConstruct
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

@Suppress("UNCHECKED_CAST")
interface Locator {

    private object NullLocator : Locator {
        override val name: String = "null"
        override fun <T> service(clazz: Class<T>, name: String?): T? = error()
        override fun <T> services(clazz: Class<T>): List<T> = error()
        override fun names(clazz: Class<*>): List<String> = error()
        override fun <R> property(key: String, clazz: Class<R>): R? = error()
        override fun close() = error<Unit>()
        override fun <T> implementation(): T = error()
        private fun <T> error(): T =
                throw IllegalStateException("Service Locator is not initialized.")
    }

    val name: String

    companion object {
        private var locators: LinkedHashMap<String, Locator> = LinkedHashMap()
        private var threadLocator: ThreadLocal<Locator> = ThreadLocal()

        var current: Locator = NullLocator
            get() = threadLocator.get() ?: locators.lastValue() ?: field
            set(value) {
                locators.put(value.name, value)
                threadLocator.set(value)
            }

        fun locator(name: String): Locator = locators[name] ?: NullLocator

        private fun <K, V> LinkedHashMap<K, V>.lastValue(): V? {
            val tail: Field = this::class.java.getDeclaredField("tail")
            tail.isAccessible = true
            @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
            return  (tail.get(this) as Map.Entry<K, V>?)?.value
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

    fun shutdown() {
        locators.remove(this.name)
        threadLocator.set(null)
        close()
    }

    fun close()

    fun <T> implementation(): T

}

