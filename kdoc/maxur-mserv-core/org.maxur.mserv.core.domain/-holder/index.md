---
title: Holder - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.domain](../index.html) / [Holder](.)

# Holder

`abstract class Holder<Type : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/domain/Holder.kt#L6)

### Constructors

| [&lt;init&gt;](-init-.html) | `Holder()` |

### Functions

| [get](get.html) | `fun <R : Type> get(locator: `[`Locator`](../../org.maxur.mserv.core/-locator/index.html)`): R?`<br>`open fun get(): Type?`<br>`abstract fun get(locator: `[`Locator`](../../org.maxur.mserv.core/-locator/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out Type>): Type?` |

### Companion Object Functions

| [get](get.html) | `fun <Type : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(func: (`[`Locator`](../../org.maxur.mserv.core/-locator/index.html)`) -> Type): Holder<Type>`<br>`fun <Type : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(func: (`[`Locator`](../../org.maxur.mserv.core/-locator/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out Type>) -> Type): Holder<Type>` |
| [none](none.html) | `fun <Type : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> none(): Holder<Type>` |
| [string](string.html) | `fun string(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Holder<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [wrap](wrap.html) | `fun <Type : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> wrap(value: Type?): Holder<Type>` |

