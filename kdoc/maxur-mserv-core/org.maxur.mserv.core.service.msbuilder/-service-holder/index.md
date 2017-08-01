---
title: ServiceHolder - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.service.msbuilder](../index.html) / [ServiceHolder](.)

# ServiceHolder

`class ServiceHolder` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/service/msbuilder/MicroServiceBuilder.kt#L112)

### Constructors

| [&lt;init&gt;](-init-.html) | `ServiceHolder()` |

### Properties

| [afterStart](after-start.html) | `val afterStart: `[`HookHolder`](../-hook-holder/index.html) |
| [afterStop](after-stop.html) | `var afterStop: `[`HookHolder`](../-hook-holder/index.html) |
| [beforeStart](before-start.html) | `val beforeStart: `[`HookHolder`](../-hook-holder/index.html) |
| [beforeStop](before-stop.html) | `var beforeStop: `[`HookHolder`](../-hook-holder/index.html) |
| [properties](properties.html) | `var properties: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
| [ref](ref.html) | `var ref: `[`EmbeddedService`](../../org.maxur.mserv.core.embedded/-embedded-service/index.html)`?` |
| [type](type.html) | `var type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [typeHolder](type-holder.html) | `var typeHolder: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Functions

| [build](build.html) | `fun build(locator: `[`Locator`](../../org.maxur.mserv.core/-locator/index.html)`): `[`EmbeddedService`](../../org.maxur.mserv.core.embedded/-embedded-service/index.html)`?` |

