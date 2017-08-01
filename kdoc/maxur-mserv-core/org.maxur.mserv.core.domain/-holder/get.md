---
title: Holder.get - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.domain](../index.html) / [Holder](index.html) / [get](.)

# get

`inline fun <reified R : Type> get(locator: `[`Locator`](../../org.maxur.mserv.core/-locator/index.html)`): R?` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/domain/Holder.kt#L19)
`open fun get(): Type?` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/domain/Holder.kt#L23)
`abstract fun get(locator: `[`Locator`](../../org.maxur.mserv.core/-locator/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out Type>): Type?` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/domain/Holder.kt#L25)
`fun <Type : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(func: (`[`Locator`](../../org.maxur.mserv.core/-locator/index.html)`) -> Type): `[`Holder`](index.html)`<Type>` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/domain/Holder.kt#L15)
`fun <Type : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(func: (`[`Locator`](../../org.maxur.mserv.core/-locator/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out Type>) -> Type): `[`Holder`](index.html)`<Type>` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/domain/Holder.kt#L16)