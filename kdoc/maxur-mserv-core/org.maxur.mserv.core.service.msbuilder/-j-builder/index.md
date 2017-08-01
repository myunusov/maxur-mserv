---
title: JBuilder - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.service.msbuilder](../index.html) / [JBuilder](.)

# JBuilder

`class JBuilder : `[`MicroServiceBuilder`](../-micro-service-builder/index.html)`, `[`IJBuilder`](../-i-j-builder/index.html) [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/service/msbuilder/Java.kt#L33)

### Constructors

| [&lt;init&gt;](-init-.html) | `JBuilder()` |

### Inherited Properties

| [afterStart](../-micro-service-builder/after-start.html) | `val afterStart: `[`HookHolder`](../-hook-holder/index.html) |
| [afterStop](../-micro-service-builder/after-stop.html) | `val afterStop: `[`HookHolder`](../-hook-holder/index.html) |
| [beforeStart](../-micro-service-builder/before-start.html) | `val beforeStart: `[`HookHolder`](../-hook-holder/index.html) |
| [beforeStop](../-micro-service-builder/before-stop.html) | `val beforeStop: `[`HookHolder`](../-hook-holder/index.html) |
| [binder](../-micro-service-builder/binder.html) | `var binder: Binder?` |
| [binders](../-micro-service-builder/binders.html) | `var binders: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<Binder>` |
| [bindersHolder](../-micro-service-builder/binders-holder.html) | `var bindersHolder: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<Binder>` |
| [onError](../-micro-service-builder/on-error.html) | `val onError: `[`ErrorHookHolder`](../-error-hook-holder/index.html) |
| [packagesHolder](../-micro-service-builder/packages-holder.html) | `var packagesHolder: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [propertiesHolder](../-micro-service-builder/properties-holder.html) | `var propertiesHolder: `[`PropertiesHolder`](../-properties-holder/index.html) |
| [services](../-micro-service-builder/services.html) | `val services: `[`ServicesHolder`](../-services-holder/index.html) |
| [titleHolder](../-micro-service-builder/title-holder.html) | `var titleHolder: `[`Holder`](../../org.maxur.mserv.core.domain/-holder/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

### Functions

| [afterStart](after-start.html) | `fun afterStart(func: `[`Consumer`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)`<in `[`BaseService`](../../org.maxur.mserv.core.domain/-base-service/index.html)`>): JBuilder` |
| [afterStop](after-stop.html) | `fun afterStop(func: `[`Consumer`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)`<in `[`BaseService`](../../org.maxur.mserv.core.domain/-base-service/index.html)`>): JBuilder` |
| [beforeStart](before-start.html) | `fun beforeStart(func: `[`Consumer`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)`<in `[`BaseService`](../../org.maxur.mserv.core.domain/-base-service/index.html)`>): JBuilder` |
| [beforeStop](before-stop.html) | `fun beforeStop(func: `[`Consumer`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)`<in `[`BaseService`](../../org.maxur.mserv.core.domain/-base-service/index.html)`>): JBuilder` |
| [onError](on-error.html) | `fun onError(func: `[`Consumer`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)`<`[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`>): JBuilder` |
| [packages](packages.html) | `fun packages(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): JBuilder` |
| [properties](properties.html) | `fun properties(format: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`JPropertiesBuilder`](../-j-properties-builder/index.html)<br>`fun properties(): JBuilder` |
| [rest](rest.html) | `fun rest(): JBuilder` |
| [service](service.html) | `fun service(type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, properties: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): JBuilder` |
| [start](start.html) | `fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [title](title.html) | `fun title(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): JBuilder` |
| [withoutProperties](without-properties.html) | `fun withoutProperties(): JBuilder` |

### Inherited Functions

| [build](../-micro-service-builder/build.html) | `open fun build(): `[`MicroService`](../../org.maxur.mserv.core/-micro-service/index.html) |

