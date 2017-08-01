---
title: IJBuilder - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.service.msbuilder](../index.html) / [IJBuilder](.)

# IJBuilder

`interface IJBuilder` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/service/msbuilder/Java.kt#L16)

### Functions

| [afterStart](after-start.html) | `abstract fun afterStart(func: `[`Consumer`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)`<in `[`BaseService`](../../org.maxur.mserv.core.domain/-base-service/index.html)`>): `[`JBuilder`](../-j-builder/index.html) |
| [afterStop](after-stop.html) | `abstract fun afterStop(func: `[`Consumer`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)`<in `[`BaseService`](../../org.maxur.mserv.core.domain/-base-service/index.html)`>): `[`JBuilder`](../-j-builder/index.html) |
| [beforeStart](before-start.html) | `abstract fun beforeStart(func: `[`Consumer`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)`<in `[`BaseService`](../../org.maxur.mserv.core.domain/-base-service/index.html)`>): `[`JBuilder`](../-j-builder/index.html) |
| [beforeStop](before-stop.html) | `abstract fun beforeStop(func: `[`Consumer`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)`<in `[`BaseService`](../../org.maxur.mserv.core.domain/-base-service/index.html)`>): `[`JBuilder`](../-j-builder/index.html) |
| [build](build.html) | `abstract fun build(): `[`MicroService`](../../org.maxur.mserv.core/-micro-service/index.html) |
| [onError](on-error.html) | `abstract fun onError(func: `[`Consumer`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)`<`[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`>): `[`JBuilder`](../-j-builder/index.html) |
| [packages](packages.html) | `abstract fun packages(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`JBuilder`](../-j-builder/index.html) |
| [properties](properties.html) | `abstract fun properties(format: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`JPropertiesBuilder`](../-j-properties-builder/index.html)<br>`abstract fun properties(): `[`JBuilder`](../-j-builder/index.html) |
| [rest](rest.html) | `abstract fun rest(): `[`JBuilder`](../-j-builder/index.html) |
| [service](service.html) | `abstract fun service(type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, properties: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`JBuilder`](../-j-builder/index.html) |
| [start](start.html) | `abstract fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [title](title.html) | `abstract fun title(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`JBuilder`](../-j-builder/index.html) |
| [withoutProperties](without-properties.html) | `abstract fun withoutProperties(): `[`JBuilder`](../-j-builder/index.html) |

### Inheritors

| [JBuilder](../-j-builder/index.html) | `class JBuilder : `[`MicroServiceBuilder`](../-micro-service-builder/index.html)`, IJBuilder` |
| [JPropertiesBuilder](../-j-properties-builder/index.html) | `class JPropertiesBuilder : IJBuilder` |

