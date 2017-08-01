---
title: StaticHttpHandler - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.embedded.grizzly](../index.html) / [StaticHttpHandler](.)

# StaticHttpHandler

`class StaticHttpHandler : StaticHttpHandlerBase` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/embedded/grizzly/StaticHttpHandler.kt#L41)

[HttpHandler](#), which processes requests to a static resources resolved
by a given [ClassLoader](http://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html).

Create HttpHandler, which will handle requests
to the static resources resolved by the given class loader.

### Parameters

`classLoader` - [ClassLoader](http://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html) to be used to resolve the resources

`staticContent` - is the static content configuration

**Author**
Grizzly Team

**Author**
Maxim Yunusov

### Types

| [BundleResource](-bundle-resource/index.html) | `inner class BundleResource : `[`Resource`](-resource/index.html) |
| [CLFileResource](-c-l-file-resource/index.html) | `inner class CLFileResource : `[`Resource`](-resource/index.html) |
| [CLRoot](-c-l-root/index.html) | `inner class CLRoot : `[`Root`](../-root/index.html) |
| [FileResource](-file-resource/index.html) | `inner class FileResource : `[`Resource`](-resource/index.html) |
| [FileRoot](-file-root/index.html) | `inner class FileRoot : `[`Root`](../-root/index.html) |
| [JarResource](-jar-resource/index.html) | `inner class JarResource : `[`Resource`](-resource/index.html) |
| [JarURLInputStream](-jar-u-r-l-input-stream/index.html) | `inner class JarURLInputStream : `[`FilterInputStream`](http://docs.oracle.com/javase/8/docs/api/java/io/FilterInputStream.html) |
| [RedirectedResource](-redirected-resource/index.html) | `inner class RedirectedResource : `[`Resource`](-resource/index.html) |
| [Resource](-resource/index.html) | `abstract inner class Resource` |
| [ResourceLocator](-resource-locator/index.html) | `inner class ResourceLocator` |
| [UnknownResource](-unknown-resource/index.html) | `inner class UnknownResource : `[`Resource`](-resource/index.html) |

### Constructors

| [&lt;init&gt;](-init-.html) | `StaticHttpHandler(path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg roots: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)``StaticHttpHandler(staticContent: `[`StaticContent`](../../org.maxur.mserv.core.embedded.properties/-static-content/index.html)`, classLoader: `[`ClassLoader`](http://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html)` = StaticHttpHandler::class.java.classLoader)`<br>[HttpHandler](#), which processes requests to a static resources resolved by a given [ClassLoader](http://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html). |

### Properties

| [classLoader](class-loader.html) | `val classLoader: `[`ClassLoader`](http://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html) |

### Functions

| [close](close.html) | `fun `[`JarURLConnection`](http://docs.oracle.com/javase/8/docs/api/java/net/JarURLConnection.html)`.close(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [handle](handle.html) | `fun handle(resourcePath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, request: Request, response: Response): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>{@inheritDoc} |
| [respondedFile](responded-file.html) | `fun respondedFile(url: `[`URL`](http://docs.oracle.com/javase/8/docs/api/java/net/URL.html)`): `[`File`](http://docs.oracle.com/javase/8/docs/api/java/io/File.html) |

### Companion Object Properties

| [log](log.html) | `val log: Logger` |

