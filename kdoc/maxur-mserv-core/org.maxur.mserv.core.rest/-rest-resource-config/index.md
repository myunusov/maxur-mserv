---
title: RestResourceConfig - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.rest](../index.html) / [RestResourceConfig](.)

# RestResourceConfig

`@Contract abstract class RestResourceConfig : ResourceConfig` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/rest/RestResourceConfig.kt#L21)

**Author**
myunusov

**Version**
1.0

**Since**

### Constructors

| [&lt;init&gt;](-init-.html) | `RestResourceConfig()` |

### Properties

| [packages](packages.html) | `val packages: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

### Functions

| [property](property.html) | `fun property(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`): ResourceConfig` |
| [register](register.html) | `fun register(component: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`): ResourceConfig`<br>`fun register(componentClass: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<*>): ResourceConfig` |
| [resources](resources.html) | `fun resources(vararg packages: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): ResourceConfig`<br>Adds array of package names which will be used to scan for components |

