---
title: StaticHttpHandler.Resource - maxur-mserv-core
---

[maxur-mserv-core](../../../index.html) / [org.maxur.mserv.core.embedded.grizzly](../../index.html) / [StaticHttpHandler](../index.html) / [Resource](.)

# Resource

`abstract inner class Resource` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/embedded/grizzly/StaticHttpHandler.kt#L245)

### Constructors

| [&lt;init&gt;](-init-.html) | `Resource()` |

### Properties

| [path](path.html) | `abstract val path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| [handle](handle.html) | `fun handle(request: Request, response: Response): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isExist](is-exist.html) | `abstract fun isExist(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [mustBeRedirected](must-be-redirected.html) | `abstract fun mustBeRedirected(resourcePath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [pickupContentType](pickup-content-type.html) | `fun pickupContentType(response: Response, path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [process](process.html) | `abstract fun process(request: Request, response: Response): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [sendResource](send-resource.html) | `fun sendResource(response: Response, input: `[`InputStream`](http://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| [BundleResource](../-bundle-resource/index.html) | `inner class BundleResource : Resource` |
| [CLFileResource](../-c-l-file-resource/index.html) | `inner class CLFileResource : Resource` |
| [FileResource](../-file-resource/index.html) | `inner class FileResource : Resource` |
| [JarResource](../-jar-resource/index.html) | `inner class JarResource : Resource` |
| [RedirectedResource](../-redirected-resource/index.html) | `inner class RedirectedResource : Resource` |
| [UnknownResource](../-unknown-resource/index.html) | `inner class UnknownResource : Resource` |

