---
title: BaseMicroService - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core](../index.html) / [BaseMicroService](.)

# BaseMicroService

`class BaseMicroService : `[`BaseService`](../../org.maxur.mserv.core.domain/-base-service/index.html)`, `[`MicroService`](../-micro-service/index.html) [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/MicroService.kt#L53)

### Parameters

`embeddedService` - Embedded service (may be composite)

`locator` - Service Locator

### Constructors

| [&lt;init&gt;](-init-.html) | `BaseMicroService(embeddedService: `[`EmbeddedService`](../../org.maxur.mserv.core.embedded/-embedded-service/index.html)`, locator: `[`Locator`](../-locator/index.html)`)` |

### Properties

| [embeddedService](embedded-service.html) | `val embeddedService: `[`EmbeddedService`](../../org.maxur.mserv.core.embedded/-embedded-service/index.html) |
| [name](name.html) | `var name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The service name |
| [version](version.html) | `val version: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The service version |

### Inherited Properties

| [afterStart](../../org.maxur.mserv.core.domain/-base-service/after-start.html) | `var afterStart: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [afterStop](../../org.maxur.mserv.core.domain/-base-service/after-stop.html) | `var afterStop: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [beforeStart](../../org.maxur.mserv.core.domain/-base-service/before-start.html) | `var beforeStart: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [beforeStop](../../org.maxur.mserv.core.domain/-base-service/before-stop.html) | `var beforeStop: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [locator](../../org.maxur.mserv.core.domain/-base-service/locator.html) | `val locator: `[`Locator`](../-locator/index.html) |
| [onError](../../org.maxur.mserv.core.domain/-base-service/on-error.html) | `var onError: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [state](../../org.maxur.mserv.core.domain/-base-service/state.html) | `var state: `[`State`](../../org.maxur.mserv.core.domain/-base-service/-state/index.html) |

### Functions

| [deferredRestart](deferred-restart.html) | `fun deferredRestart(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Restart this Service |
| [deferredStop](deferred-stop.html) | `fun deferredStop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Stop this Service |
| [launch](launch.html) | `fun launch(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>launch this service. |
| [relaunch](relaunch.html) | `fun relaunch(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>relaunch this service. |
| [shutdown](shutdown.html) | `fun shutdown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>shutdown this service. |

### Inherited Functions

| [restart](../../org.maxur.mserv.core.domain/-base-service/restart.html) | `fun restart(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Immediately restart this Service. |
| [start](../../org.maxur.mserv.core.domain/-base-service/start.html) | `fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Start this Service |
| [stop](../../org.maxur.mserv.core.domain/-base-service/stop.html) | `fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Immediately shuts down this Service. |

