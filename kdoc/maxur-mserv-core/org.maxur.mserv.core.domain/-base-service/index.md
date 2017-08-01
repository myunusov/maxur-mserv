---
title: BaseService - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.domain](../index.html) / [BaseService](.)

# BaseService

`abstract class BaseService` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/domain/BaseService.kt#L11)

### Types

| [State](-state/index.html) | `enum class State`<br>Represent State of micro-service |

### Constructors

| [&lt;init&gt;](-init-.html) | `BaseService(locator: `[`Locator`](../../org.maxur.mserv.core/-locator/index.html)`)` |

### Properties

| [afterStart](after-start.html) | `var afterStart: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [afterStop](after-stop.html) | `var afterStop: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [beforeStart](before-start.html) | `var beforeStart: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [beforeStop](before-stop.html) | `var beforeStop: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [locator](locator.html) | `val locator: `[`Locator`](../../org.maxur.mserv.core/-locator/index.html) |
| [name](name.html) | `abstract var name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The service name |
| [onError](on-error.html) | `var onError: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [state](state.html) | `var state: `[`State`](-state/index.html) |

### Functions

| [launch](launch.html) | `abstract fun launch(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>launch this service. |
| [relaunch](relaunch.html) | `abstract fun relaunch(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>relaunch this service. |
| [restart](restart.html) | `fun restart(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Immediately restart this Service. |
| [shutdown](shutdown.html) | `abstract fun shutdown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>shutdown this service. |
| [start](start.html) | `fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Start this Service |
| [stop](stop.html) | `fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Immediately shuts down this Service. |

### Inheritors

| [BaseMicroService](../../org.maxur.mserv.core/-base-micro-service/index.html) | `class BaseMicroService : BaseService, `[`MicroService`](../../org.maxur.mserv.core/-micro-service/index.html) |
| [WebServerGrizzlyImpl](../../org.maxur.mserv.core.embedded.grizzly/-web-server-grizzly-impl/index.html) | `open class WebServerGrizzlyImpl : BaseService, `[`WebServer`](../../org.maxur.mserv.core.embedded/-web-server/index.html) |

