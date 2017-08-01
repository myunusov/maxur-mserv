---
title: LocatorHK2Impl - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.service.hk2](../index.html) / [LocatorHK2Impl](.)

# LocatorHK2Impl

`class LocatorHK2Impl : `[`Locator`](../../org.maxur.mserv.core/-locator/index.html) [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/service/hk2/LocatorHK2Impl.kt#L9)

### Constructors

| [&lt;init&gt;](-init-.html) | `LocatorHK2Impl(locator: ServiceLocator)` |

### Properties

| [locator](locator.html) | `val locator: ServiceLocator` |

### Functions

| [implementation](implementation.html) | `fun <T> implementation(): T` |
| [names](names.html) | `fun names(clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<*>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [property](property.html) | `fun <T> property(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<T>): T?` |
| [service](service.html) | `fun <T> service(clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<T>, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): T?` |
| [services](services.html) | `fun <T> services(clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<T>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>` |
| [shutdown](shutdown.html) | `fun shutdown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inherited Functions

| [init](../../org.maxur.mserv.core/-locator/init.html) | `open fun init(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [locate](../../org.maxur.mserv.core/-locator/locate.html) | `open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> locate(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): T`<br>`open fun <T> locate(clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<T>, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): T` |
| [names](../../org.maxur.mserv.core/-locator/names.html) | `open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> names(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [property](../../org.maxur.mserv.core/-locator/property.html) | `open fun property(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>`open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> property(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>): T?` |
| [service](../../org.maxur.mserv.core/-locator/service.html) | `open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> service(parameter: `[`KParameter`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-parameter/index.html)`): T?`<br>`open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> service(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): T?`<br>`open fun <T> service(clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<T>): T?` |
| [services](../../org.maxur.mserv.core/-locator/services.html) | `open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> services(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>` |

