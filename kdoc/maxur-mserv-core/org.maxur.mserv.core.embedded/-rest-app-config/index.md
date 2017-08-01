---
title: RestAppConfig - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.embedded](../index.html) / [RestAppConfig](.)

# RestAppConfig

`data class RestAppConfig` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/embedded/RestAppConfig.kt#L8)

### Constructors

| [&lt;init&gt;](-init-.html) | `RestAppConfig(url: `[`URI`](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html)`, rest: `[`RestService`](../../org.maxur.mserv.core.embedded.properties/-rest-service/index.html)`, staticContent: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`StaticContent`](../../org.maxur.mserv.core.embedded.properties/-static-content/index.html)`>, resourceConfig: ResourceConfig)` |

### Properties

| [resourceConfig](resource-config.html) | `val resourceConfig: ResourceConfig` |
| [rest](rest.html) | `val rest: `[`RestService`](../../org.maxur.mserv.core.embedded.properties/-rest-service/index.html) |
| [staticContent](static-content.html) | `val staticContent: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`StaticContent`](../../org.maxur.mserv.core.embedded.properties/-static-content/index.html)`>` |
| [url](url.html) | `val url: `[`URI`](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html) |

### Functions

| [staticContentByPath](static-content-by-path.html) | `fun staticContentByPath(path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`StaticContent`](../../org.maxur.mserv.core.embedded.properties/-static-content/index.html)`?` |

