---
title: WebServer - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.embedded](../index.html) / [WebServer](.)

# WebServer

`interface WebServer : `[`EmbeddedService`](../-embedded-service/index.html) [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/embedded/WebServer.kt#L10)

**Author**
myunusov

**Version**
1.0

**Since**

### Properties

| [baseUri](base-uri.html) | `abstract val baseUri: `[`URI`](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html) |
| [name](name.html) | `abstract var name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| [entries](entries.html) | `abstract fun entries(): `[`WebEntries`](../-web-entries/index.html) |

### Inherited Functions

| [restart](../-embedded-service/restart.html) | `abstract fun restart(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Relaunch server. |
| [start](../-embedded-service/start.html) | `abstract fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Start server. |
| [stop](../-embedded-service/stop.html) | `abstract fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Stop server. |

### Inheritors

| [WebServerGrizzlyImpl](../../org.maxur.mserv.core.embedded.grizzly/-web-server-grizzly-impl/index.html) | `open class WebServerGrizzlyImpl : `[`BaseService`](../../org.maxur.mserv.core.domain/-base-service/index.html)`, WebServer` |

