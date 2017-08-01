---
title: Properties - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.service.properties](../index.html) / [Properties](.)

# Properties

`@Contract interface Properties` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/service/properties/Properties.kt#L8)

### Functions

| [asInteger](as-integer.html) | `abstract fun asInteger(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?`<br>return properties by key |
| [asLong](as-long.html) | `abstract fun asLong(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?`<br>return properties by key |
| [asString](as-string.html) | `abstract fun asString(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>return properties by key |
| [asURI](as-u-r-i.html) | `abstract fun asURI(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`URI`](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html)`?`<br>return properties by key |
| [read](read.html) | `open fun <P : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> read(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<P>): P?`<br>`abstract fun <P> read(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, clazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<P>): P?`<br>return properties by key |
| [withoutScheme](without-scheme.html) | `open fun `[`URI`](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html)`.withoutScheme(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Inheritors

| [NullProperties](../-null-properties/index.html) | `object NullProperties : `[`PropertiesSource`](../-properties-source/index.html)`, Properties` |

