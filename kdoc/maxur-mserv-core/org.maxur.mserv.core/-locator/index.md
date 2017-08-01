---
title: Locator - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core](../index.html) / [Locator](.)

# Locator

`interface Locator` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/Locator.kt#L8)

### Functions

| [implementation](implementation.html) | `abstract fun <T> implementation(): T` |
| [init](init.html) | `open fun init(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [locate](locate.html) | `open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> locate(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): T`<br>`open fun <T> locate(clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<T>, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): T` |
| [names](names.html) | `open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> names(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>`abstract fun names(clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<*>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [property](property.html) | `open fun property(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>`open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> property(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>): T?`<br>`abstract fun <T> property(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<T>): T?` |
| [service](service.html) | `open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> service(parameter: `[`KParameter`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-parameter/index.html)`): T?`<br>`open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> service(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): T?`<br>`open fun <T> service(clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<T>): T?`<br>`abstract fun <T> service(clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<T>, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): T?` |
| [services](services.html) | `open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> services(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>`<br>`abstract fun <T> services(clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<T>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>` |
| [shutdown](shutdown.html) | `abstract fun shutdown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| [current](current.html) | `var current: Locator` |

### Companion Object Functions

| [service](service.html) | `fun <T> service(clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<T>): T?`<br>`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> service(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>): T?`<br>`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> service(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): T?`<br>`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> service(parameter: `[`KParameter`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-parameter/index.html)`): T?` |
| [services](services.html) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> services(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>` |
| [shutdown](shutdown.html) | `fun shutdown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| [LocatorHK2Impl](../../org.maxur.mserv.core.service.hk2/-locator-h-k2-impl/index.html) | `class LocatorHK2Impl : Locator` |

