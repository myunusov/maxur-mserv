---
title: BaseService.State - maxur-mserv-core
---

[maxur-mserv-core](../../../index.html) / [org.maxur.mserv.core.domain](../../index.html) / [BaseService](../index.html) / [State](.)

# State

`enum class State` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/domain/BaseService.kt#L61)

Represent State of micro-service

### Enum Values

| [STARTED](-s-t-a-r-t-e-d/index.html) | Running application |
| [STOPPED](-s-t-o-p-p-e-d/index.html) | Stop application |

### Functions

| [launch](launch.html) | `fun launch(service: `[`BaseService`](../index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [relaunch](relaunch.html) | `fun relaunch(service: `[`BaseService`](../index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [restart](restart.html) | `abstract fun restart(service: `[`BaseService`](../index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [shutdown](shutdown.html) | `fun shutdown(service: `[`BaseService`](../index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [start](start.html) | `abstract fun start(service: `[`BaseService`](../index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [stop](stop.html) | `abstract fun stop(service: `[`BaseService`](../index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

