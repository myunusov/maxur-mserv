---
title: PropertiesInjectionResolver - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.service.hk2](../index.html) / [PropertiesInjectionResolver](.)

# PropertiesInjectionResolver

`class PropertiesInjectionResolver : InjectionResolver<`[`Value`](../../org.maxur.mserv.core.annotation/-value/index.html)`>` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/service/hk2/PropertiesInjectionResolver.kt#L23)

Resolve configuration by ConfigParameter annotations.

**Author**
myunusov

**Version**
1.0

**Since**

### Constructors

| [&lt;init&gt;](-init-.html) | `PropertiesInjectionResolver(service: `[`Properties`](../../org.maxur.mserv.core.service.properties/-properties/index.html)`)`<br>Resolve configuration by ConfigParameter annotations. |

### Properties

| [service](service.html) | `val service: `[`Properties`](../../org.maxur.mserv.core.service.properties/-properties/index.html) |

### Functions

| [isConstructorParameterIndicator](is-constructor-parameter-indicator.html) | `fun isConstructorParameterIndicator(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isMethodParameterIndicator](is-method-parameter-indicator.html) | `fun isMethodParameterIndicator(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [resolve](resolve.html) | `fun resolve(injectee: Injectee, root: ServiceHandle<*>?): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |

### Companion Object Properties

| [log](log.html) | `val log: Logger` |

