---
title: GrizzlyHttpContainer - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.embedded.grizzly](../index.html) / [GrizzlyHttpContainer](.)

# GrizzlyHttpContainer

`class GrizzlyHttpContainer : HttpHandler, Container` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/embedded/grizzly/GrizzlyHttpContainer.kt#L34)

### Constructors

| [&lt;init&gt;](-init-.html) | `GrizzlyHttpContainer(application: Application, parentLocator: ServiceLocator)`<br>Create a new Grizzly HTTP container.`GrizzlyHttpContainer(appHandler: ApplicationHandler?)` |

### Functions

| [destroy](destroy.html) | `fun destroy(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getApplicationHandler](get-application-handler.html) | `fun getApplicationHandler(): ApplicationHandler` |
| [getConfiguration](get-configuration.html) | `fun getConfiguration(): ResourceConfig` |
| [reload](reload.html) | `fun reload(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`fun reload(configuration: ResourceConfig): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [service](service.html) | `fun service(request: Request, response: Response): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [start](start.html) | `fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| [log](log.html) | `val log: Logger` |

