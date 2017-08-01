---
title: StaticHttpHandler.UnknownResource - maxur-mserv-core
---

[maxur-mserv-core](../../../index.html) / [org.maxur.mserv.core.embedded.grizzly](../../index.html) / [StaticHttpHandler](../index.html) / [UnknownResource](.)

# UnknownResource

`inner class UnknownResource : `[`Resource`](../-resource/index.html) [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/embedded/grizzly/StaticHttpHandler.kt#L213)

### Constructors

| [&lt;init&gt;](-init-.html) | `UnknownResource(url: `[`URL`](http://docs.oracle.com/javase/8/docs/api/java/net/URL.html)`)` |

### Properties

| [path](path.html) | `val path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [url](url.html) | `val url: `[`URL`](http://docs.oracle.com/javase/8/docs/api/java/net/URL.html) |
| [urlConnection](url-connection.html) | `var urlConnection: `[`URLConnection`](http://docs.oracle.com/javase/8/docs/api/java/net/URLConnection.html) |

### Functions

| [isExist](is-exist.html) | `fun isExist(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [mustBeRedirected](must-be-redirected.html) | `fun mustBeRedirected(resourcePath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [process](process.html) | `fun process(request: Request, response: Response): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inherited Functions

| [handle](../-resource/handle.html) | `fun handle(request: Request, response: Response): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [pickupContentType](../-resource/pickup-content-type.html) | `fun pickupContentType(response: Response, path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [sendResource](../-resource/send-resource.html) | `fun sendResource(response: Response, input: `[`InputStream`](http://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

