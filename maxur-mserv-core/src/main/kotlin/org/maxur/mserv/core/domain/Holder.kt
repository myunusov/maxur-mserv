package org.maxur.mserv.core.domain

import org.maxur.mserv.core.kotlin.Locator
import kotlin.reflect.KClass

abstract class Holder<Type : Any> {

    companion object {
        fun string(value: String): Holder<String> = when {
            value.startsWith(":") -> Holder.get { locator -> locator.property(value.substringAfter(":"))!! }
            else -> Holder.wrap(value)
        }

        fun <Type : Any> none(): Holder<Type> = Wrapper(null)
        fun <Type : Any> wrap(value: Type?): Holder<Type> = Wrapper(value)
        fun <Type : Any> get(func: (Locator) -> Type): Holder<Type> = Descriptor1(func)
        fun <Type : Any> get(func: (Locator, clazz: KClass<out Type>) -> Type): Holder<Type> = Descriptor2(func)
    }

    inline fun <reified R : Type> get(locator: Locator): R? {
        return get(locator, R::class) as R
    }

    open fun get(): Type? = throw UnsupportedOperationException("This holder don't support get() without parameters")

    abstract fun get(locator: Locator, clazz: KClass<out Type>): Type?
}

private class Descriptor1<Type : Any>(val func: (Locator) -> Type) : Holder<Type>() {
    override fun get(locator: Locator, clazz: KClass<out Type>): Type? = func(locator)
}

private class Descriptor2<Type : Any>(val func: (Locator, KClass<out Type>) -> Type) : Holder<Type>() {
    override fun get(locator: Locator, clazz: KClass<out Type>): Type? = func(locator, clazz)
}

private class Wrapper<Type : Any>(val value: Type?) : Holder<Type>() {
    override fun get(): Type? = value
    override fun get(locator: Locator, clazz: KClass<out Type>): Type? = value
}

