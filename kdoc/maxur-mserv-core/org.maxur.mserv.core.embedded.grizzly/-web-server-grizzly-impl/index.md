---
title: WebServerGrizzlyImpl - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.embedded.grizzly](../index.html) / [WebServerGrizzlyImpl](.)

# WebServerGrizzlyImpl

`open class WebServerGrizzlyImpl : `[`BaseService`](../../org.maxur.mserv.core.domain/-base-service/index.html)`, `[`WebServer`](../../org.maxur.mserv.core.embedded/-web-server/index.html) [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/embedded/grizzly/WebServerGrizzlyFactoryImpl.kt#L110)

### Constructors

| [&lt;init&gt;](-init-.html) | `WebServerGrizzlyImpl(config: `[`RestAppConfig`](../../org.maxur.mserv.core.embedded/-rest-app-config/index.html)`, locator: `[`Locator`](../../org.maxur.mserv.core/-locator/index.html)`)` |

### Properties

| [baseUri](base-uri.html) | `open val baseUri: `[`URI`](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html) |
| [name](name.html) | `open var name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The service name |

### Inherited Properties

| [afterStart](../../org.maxur.mserv.core.domain/-base-service/after-start.html) | `var afterStart: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [afterStop](../../org.maxur.mserv.core.domain/-base-service/after-stop.html) | `var afterStop: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [beforeStart](../../org.maxur.mserv.core.domain/-base-service/before-start.html) | `var beforeStart: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [beforeStop](../../org.maxur.mserv.core.domain/-base-service/before-stop.html) | `var beforeStop: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [locator](../../org.maxur.mserv.core.domain/-base-service/locator.html) | `val locator: `[`Locator`](../../org.maxur.mserv.core/-locator/index.html) |
| [onError](../../org.maxur.mserv.core.domain/-base-service/on-error.html) | `var onError: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`KFunction`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-function/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| [state](../../org.maxur.mserv.core.domain/-base-service/state.html) | `var state: `[`State`](../../org.maxur.mserv.core.domain/-base-service/-state/index.html) |

### Functions

| [entries](entries.html) | `open fun entries(): `[`WebEntries`](../../org.maxur.mserv.core.embedded/-web-entries/index.html) |
| [launch](launch.html) | `open fun launch(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>launch this service. |
| [relaunch](relaunch.html) | `open fun relaunch(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>relaunch this service. |
| [shutdown](shutdown.html) | `open fun shutdown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>shutdown this service. |
| [title](title.html) | `fun ServerConfiguration.title(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Inherited Functions

| [restart](../../org.maxur.mserv.core.domain/-base-service/restart.html) | `fun restart(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Immediately restart this Service. |
| [start](../../org.maxur.mserv.core.domain/-base-service/start.html) | `fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Start this Service |
| [stop](../../org.maxur.mserv.core.domain/-base-service/stop.html) | `fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Immediately shuts down this Service. |

