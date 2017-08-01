---
title: KBuilder - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.service.msbuilder](../index.html) / [KBuilder](.)

# KBuilder

`class KBuilder : `[`MicroServiceBuilder`](../-micro-service-builder/index.html) [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/service/msbuilder/Kotlin.kt#L10)

### Constructors

| [&lt;init&gt;](-init-.html) | `KBuilder(init: KBuilder.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)`<br>`KBuilder()` |

### Properties

| [packages](packages.html) | `var packages: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [title](title.html) | `var title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

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

| [properties](properties.html) | `fun properties(init: `[`BasePropertiesHolder`](../-properties-holder/-base-properties-holder/index.html)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [rest](rest.html) | `fun rest(init: `[`ServiceHolder`](../-service-holder/index.html)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`ServiceHolder`](../-service-holder/index.html) |
| [service](service.html) | `fun service(init: `[`ServiceHolder`](../-service-holder/index.html)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`ServiceHolder`](../-service-holder/index.html) |
| [withoutProperties](without-properties.html) | `fun withoutProperties(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inherited Functions

| [build](../-micro-service-builder/build.html) | `open fun build(): `[`MicroService`](../../org.maxur.mserv.core/-micro-service/index.html) |

