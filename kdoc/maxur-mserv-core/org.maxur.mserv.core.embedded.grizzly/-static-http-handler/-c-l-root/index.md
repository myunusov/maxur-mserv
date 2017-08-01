---
title: StaticHttpHandler.CLRoot - maxur-mserv-core
---

[maxur-mserv-core](../../../index.html) / [org.maxur.mserv.core.embedded.grizzly](../../index.html) / [StaticHttpHandler](../index.html) / [CLRoot](.)

# CLRoot

`inner class CLRoot : `[`Root`](../../-root/index.html) [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/embedded/grizzly/StaticHttpHandler.kt#L516)

### Constructors

| [&lt;init&gt;](-init-.html) | `CLRoot(uri: `[`URI`](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html)`, classLoader: `[`ClassLoader`](http://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html)`)`<br>`CLRoot(path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, classLoader: `[`ClassLoader`](http://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html)`)` |

### Properties

| [classLoader](class-loader.html) | `val classLoader: `[`ClassLoader`](http://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html) |
| [path](path.html) | `val path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| [lookupResource](lookup-resource.html) | `fun lookupResource(resourcePath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, mayBeFolder: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Resource`](../-resource/index.html)`?` |
| [validate](validate.html) | `fun validate(): `[`Root`](../../-root/index.html) |

