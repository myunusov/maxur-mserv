---
title: Value - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.utils](../index.html) / [Value](.)

# Value

`data class Value<out T> : `[`Either`](../-either.html)`<`[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)`, T>` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/utils/Either.kt#L42)

### Constructors

| [&lt;init&gt;](-init-.html) | `Value(value: T)` |

### Properties

| [value](value.html) | `val value: T` |

### Extension Functions

| [apply](../apply.html) | `infix fun <E, V, V2> `[`Either`](../-either.html)`<E, (V) -> V2>.apply(f: `[`Either`](../-either.html)`<E, V>): `[`Either`](../-either.html)`<E, V2>` |
| [flatMap](../flat-map.html) | `infix fun <E, V, V2> `[`Either`](../-either.html)`<E, V>.flatMap(f: (V) -> `[`Either`](../-either.html)`<E, V2>): `[`Either`](../-either.html)`<E, V2>` |
| [fold](../fold.html) | `fun <E, V, A> `[`Either`](../-either.html)`<E, V>.fold(e: (E) -> A, v: (V) -> A): A` |
| [map](../map.html) | `infix fun <E, V, V2> `[`Either`](../-either.html)`<E, V>.map(f: (V) -> V2): `[`Either`](../-either.html)`<E, V2>` |
| [mapError](../map-error.html) | `infix fun <E, E2, V> `[`Either`](../-either.html)`<E, V>.mapError(f: (E) -> E2): `[`Either`](../-either.html)`<E2, V>` |

