---
title: PropertiesFactory - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.service.properties](../index.html) / [PropertiesFactory](.)

# PropertiesFactory

`@Contract abstract class PropertiesFactory` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/service/properties/PropertiesFactory.kt#L14)

**Author**
myunusov

**Version**
1.0

**Since**

### Constructors

| [&lt;init&gt;](-init-.html) | `PropertiesFactory()` |

### Functions

| [make](make.html) | `abstract fun make(source: `[`PropertiesSource`](../-properties-source/index.html)`): `[`Either`](../../org.maxur.mserv.core.utils/-either.html)`<`[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`, `[`Properties`](../-properties/index.html)`>` |

### Inheritors

| [PropertiesFactoryHoconImpl](../-properties-factory-hocon-impl/index.html) | `class PropertiesFactoryHoconImpl : PropertiesFactory` |
| [PropertiesFactoryJsonImpl](../-properties-factory-json-impl/index.html) | `class PropertiesFactoryJsonImpl : PropertiesFactory` |
| [PropertiesFactoryYamlImpl](../-properties-factory-yaml-impl/index.html) | `class PropertiesFactoryYamlImpl : PropertiesFactory` |

