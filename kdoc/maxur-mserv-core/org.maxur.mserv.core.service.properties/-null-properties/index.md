---
title: NullProperties - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.service.properties](../index.html) / [NullProperties](.)

# NullProperties

`object NullProperties : `[`PropertiesSource`](../-properties-source/index.html)`, `[`Properties`](../-properties/index.html) [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/service/properties/PropertiesSource.kt#L49)

### Inherited Properties

| [format](../-properties-source/format.html) | `open val format: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [rootKey](../-properties-source/root-key.html) | `open val rootKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [uri](../-properties-source/uri.html) | `open val uri: `[`URI`](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html)`?` |

### Functions

| [asInteger](as-integer.html) | `fun asInteger(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?`<br>return properties by key |
| [asLong](as-long.html) | `fun asLong(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?`<br>return properties by key |
| [asString](as-string.html) | `fun asString(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>return properties by key |
| [asURI](as-u-r-i.html) | `fun asURI(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`URI`](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html)`?`<br>return properties by key |
| [read](read.html) | `fun <P> read(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<P>): P?`<br>return properties by key |

### Inherited Functions

| [read](../-properties/read.html) | `open fun <P : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> read(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<P>): P?`<br>return properties by key |
| [withoutScheme](../-properties/without-scheme.html) | `open fun `[`URI`](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html)`.withoutScheme(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

