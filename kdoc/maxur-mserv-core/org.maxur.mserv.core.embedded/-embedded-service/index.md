---
title: EmbeddedService - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.embedded](../index.html) / [EmbeddedService](.)

# EmbeddedService

`interface EmbeddedService` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/embedded/EmbeddedService.kt#L7)

This class represents Embedded to micro-service Service
with start and stop functions

### Functions

| [restart](restart.html) | `abstract fun restart(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Relaunch server. |
| [start](start.html) | `abstract fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Start server. |
| [stop](stop.html) | `abstract fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Stop server. |

### Inheritors

| [CompositeService](../-composite-service/index.html) | `class CompositeService : EmbeddedService` |
| [WebServer](../-web-server/index.html) | `interface WebServer : EmbeddedService` |

