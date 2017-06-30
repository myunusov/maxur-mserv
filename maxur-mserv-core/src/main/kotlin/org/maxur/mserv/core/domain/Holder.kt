package org.maxur.mserv.core.domain

import org.maxur.mserv.core.Locator

abstract class Holder<Type> {

    companion object {
        fun string(value: String) : Holder<String> = when {
            value.startsWith(":") -> Holder.get { locator -> locator.property(value.substringAfter(":")) }
            else -> Holder.wrap(value)
        }
        fun <Type> none() : Holder<Type?> = Wrapper(null)
        fun <Type> wrap(value: Type) : Holder<Type> = Wrapper(value)
        fun <Type> get(func: (Locator) -> Type) : Holder<Type> = Descriptor1(func)
        fun <Type> get(func: (Locator, clazz: Class<out Type>) -> Type) : Holder<Type> = Descriptor2(func)
    }

    inline fun <reified R : Type>  get(locator: Locator): R? {
        return get(locator, R::class.java) as R
    }

    open fun get(): Type? = throw UnsupportedOperationException("This holder don't support get() without parameters")

    abstract fun get(locator: Locator, clazz: Class<out Type>): Type?
}

private class Descriptor1<Type>(val func: (Locator) -> Type) : Holder<Type>() {
   override fun get(locator: Locator, clazz: Class<out Type>): Type? = func(locator)
}

private class Descriptor2<Type>(val func: (Locator, Class<out Type>) -> Type) : Holder<Type>() {
    override fun get(locator: Locator, clazz: Class<out Type>): Type? = func(locator, clazz)
}

private class Wrapper<Type>(val value: Type) : Holder<Type>() {
    override fun get(): Type? = value
    override fun get(locator: Locator, clazz: Class<out Type>): Type?  = value
}

