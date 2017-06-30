package org.maxur.mserv.core

interface Locator {

    fun <T> locate(name: String, clazz: Class<T>): T = service(clazz, name) ?:
            throw IllegalStateException(
                    "Service '$name' is not supported. Try one from this list: ${names(clazz)}"
            )

    fun <T> service(clazz: Class<T>): T?

    fun <T> service(clazz: Class<T>, name: String?): T?

    fun names(clazz: Class<*>): List<String>

    fun property(key: String): String

    fun <R> properties(key: String, clazz: Class<R>): R?

    fun <T> implementation(): T

}
