---
title: MicroService - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core](../index.html) / [MicroService](.)

# MicroService

`interface MicroService` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/MicroService.kt#L14)

The micro-service

**Author**
myunusov

**Version**
1.0

**Since**

### Properties

| [name](name.html) | `abstract val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The service name |
| [version](version.html) | `abstract val version: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The service version |

### Functions

| [deferredRestart](deferred-restart.html) | `abstract fun deferredRestart(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Restart this Service |
| [deferredStop](deferred-stop.html) | `abstract fun deferredStop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Stop this Service |
| [start](start.html) | `abstract fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Start this Service |
| [stop](stop.html) | `abstract fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Stop this Service |

### Inheritors

| [BaseMicroService](../-base-micro-service/index.html) | `class BaseMicroService : `[`BaseService`](../../org.maxur.mserv.core.domain/-base-service/index.html)`, MicroService` |

