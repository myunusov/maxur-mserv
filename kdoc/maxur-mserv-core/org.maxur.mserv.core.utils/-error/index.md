---
title: Error - maxur-mserv-core
---

[maxur-mserv-core](../../index.html) / [org.maxur.mserv.core.utils](../index.html) / [Error](.)

# Error

`data class Error<out T> : `[`Either`](../-either.html)`<T, `[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)`>` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/utils/Either.kt#L40)

### Constructors

| [&lt;init&gt;](-init-.html) | `Error(error: T)` |

### Properties

| [error](error.html) | `val error: T` |

### Extension Functions

| [apply](../apply.html) | `infix fun <E, V, V2> `[`Either`](../-either.html)`<E, (V) -> V2>.apply(f: `[`Either`](../-either.html)`<E, V>): `[`Either`](../-either.html)`<E, V2>` |
| [flatMap](../flat-map.html) | `infix fun <E, V, V2> `[`Either`](../-either.html)`<E, V>.flatMap(f: (V) -> `[`Either`](../-either.html)`<E, V2>): `[`Either`](../-either.html)`<E, V2>` |
| [fold](../fold.html) | `fun <E, V, A> `[`Either`](../-either.html)`<E, V>.fold(e: (E) -> A, v: (V) -> A): A` |
| [map](../map.html) | `infix fun <E, V, V2> `[`Either`](../-either.html)`<E, V>.map(f: (V) -> V2): `[`Either`](../-either.html)`<E, V2>` |
| [mapError](../map-error.html) | `infix fun <E, E2, V> `[`Either`](../-either.html)`<E, V>.mapError(f: (E) -> E2): `[`Either`](../-either.html)`<E2, V>` |

