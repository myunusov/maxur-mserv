---
title: MicroServiceBuilder - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.service.msbuilder](../index.html) / [MicroServiceBuilder](.)

# MicroServiceBuilder

`abstract class MicroServiceBuilder` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/service/msbuilder/MicroServiceBuilder.kt#L24)

**Todo**
Implement support of default options for best service choice

### Constructors

| [&lt;init&gt;](-init-.html) | `MicroServiceBuilder()` |

### Properties

| [afterStart](after-start.html) | `val afterStart: `[`HookHolder`](../-hook-holder/index.html) |
| [afterStop](after-stop.html) | `val afterStop: `[`HookHolder`](../-hook-holder/index.html) |
| [beforeStart](before-start.html) | `val beforeStart: `[`HookHolder`](../-hook-holder/index.html) |
| [beforeStop](before-stop.html) | `val beforeStop: `[`HookHolder`](../-hook-holder/index.html) |
| [binder](binder.html) | `var binder: Binder?` |
| [binders](binders.html) | `var binders: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<Binder>` |
| [bindersHolder](binders-holder.html) | `var bindersHolder: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<Binder>` |
| [onError](on-error.html) | `val onError: `[`ErrorHookHolder`](../-error-hook-holder/index.html) |
| [packagesHolder](packages-holder.html) | `var packagesHolder: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [propertiesHolder](properties-holder.html) | `var propertiesHolder: `[`PropertiesHolder`](../-properties-holder/index.html) |
| [services](services.html) | `val services: `[`ServicesHolder`](../-services-holder/index.html) |
| [titleHolder](title-holder.html) | `var titleHolder: `[`Holder`](../../org.maxur.mserv.core.domain/-holder/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

### Functions

| [build](build.html) | `open fun build(): `[`MicroService`](../../org.maxur.mserv.core/-micro-service/index.html) |

### Inheritors

| [JBuilder](../-j-builder/index.html) | `class JBuilder : MicroServiceBuilder, `[`IJBuilder`](../-i-j-builder/index.html) |
| [KBuilder](../-k-builder/index.html) | `class KBuilder : MicroServiceBuilder` |

