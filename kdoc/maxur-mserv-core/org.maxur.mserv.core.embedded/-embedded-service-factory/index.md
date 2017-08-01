---
title: EmbeddedServiceFactory - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.embedded](../index.html) / [EmbeddedServiceFactory](.)

# EmbeddedServiceFactory

`@Contract abstract class EmbeddedServiceFactory` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/embedded/EmbeddedServiceFactory.kt#L16)

**Author**
myunusov

**Version**
1.0

**Since**

### Constructors

| [&lt;init&gt;](-init-.html) | `EmbeddedServiceFactory()` |

### Properties

| [name](name.html) | `lateinit var name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| [init](init.html) | `fun init(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [make](make.html) | `abstract fun make(properties: `[`Holder`](../../org.maxur.mserv.core.domain/-holder/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>): `[`EmbeddedService`](../-embedded-service/index.html)`?` |

### Inheritors

| [WebServerGrizzlyFactoryImpl](../../org.maxur.mserv.core.embedded.grizzly/-web-server-grizzly-factory-impl/index.html) | `class WebServerGrizzlyFactoryImpl : EmbeddedServiceFactory` |

