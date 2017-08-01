---
title: LocatorFactoryHK2Impl - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.service.hk2](../index.html) / [LocatorFactoryHK2Impl](.)

# LocatorFactoryHK2Impl

`class LocatorFactoryHK2Impl` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/service/hk2/LocatorFactoryHK2Impl.kt#L23)

**Author**
myunusov

**Version**
1.0

**Since**

### Types

| [LocatorBinder](-locator-binder/index.html) | `class LocatorBinder : AbstractBinder` |

### Constructors

| [&lt;init&gt;](-init-.html) | `LocatorFactoryHK2Impl(init: LocatorFactoryHK2Impl.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)` |

### Properties

| [binders](binders.html) | `val binders: `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<Binder>` |
| [locator](locator.html) | `val locator: ServiceLocator` |
| [packages](packages.html) | `var packages: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

### Functions

| [bind](bind.html) | `fun bind(vararg binders: Binder): LocatorFactoryHK2Impl`<br>`fun bind(func: (`[`Locator`](../../org.maxur.mserv.core/-locator/index.html)`) -> `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, vararg classes: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [make](make.html) | `fun make(): `[`Locator`](../../org.maxur.mserv.core/-locator/index.html) |

