---
title: PropertiesSource - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.service.properties](../index.html) / [PropertiesSource](.)

# PropertiesSource

`abstract class PropertiesSource` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/service/properties/PropertiesSource.kt#L16)

Represent the Properties source configuration.

**Author**
myunusov

**Version**
1.0

**Since**

### Constructors

| [&lt;init&gt;](-init-.html) | `PropertiesSource(format: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, uri: `[`URI`](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html)`? = null, rootKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)`<br>Represent the Properties source configuration. |

### Properties

| [format](format.html) | `open val format: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [rootKey](root-key.html) | `open val rootKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [uri](uri.html) | `open val uri: `[`URI`](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html)`?` |

### Companion Object Functions

| [default](default.html) | `fun default(): `[`Properties`](../-properties/index.html) |
| [nothing](nothing.html) | `fun nothing(): `[`Properties`](../-properties/index.html) |
| [open](open.html) | `fun open(format: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, uri: `[`URI`](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html)`? = null, rootKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`Properties`](../-properties/index.html)<br>Open properties resource. |

### Inheritors

| [NullProperties](../-null-properties/index.html) | `object NullProperties : PropertiesSource, `[`Properties`](../-properties/index.html) |

