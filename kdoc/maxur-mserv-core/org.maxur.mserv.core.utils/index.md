---
title: org.maxur.mserv.core.utils - maxur-mserv-core
---

[maxur-mserv-core](../index.html) / [org.maxur.mserv.core.utils](.)

## Package org.maxur.mserv.core.utils

### Types

| [Either](-either.html) | `sealed class Either<out L, out R>` |
| [Error](-error/index.html) | `data class Error<out T> : `[`Either`](-either.html)`<T, `[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)`>` |
| [Value](-value/index.html) | `data class Value<out T> : `[`Either`](-either.html)`<`[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)`, T>` |

### Functions

| [apply](apply.html) | `infix fun <E, V, V2> `[`Either`](-either.html)`<E, (V) -> V2>.apply(f: `[`Either`](-either.html)`<E, V>): `[`Either`](-either.html)`<E, V2>` |
| [either](either.html) | `fun <V> either(action: () -> V): `[`Either`](-either.html)`<`[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`, V>` |
| [error](error.html) | `fun <E> error(value: E): `[`Either`](-either.html)`<E, `[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)`>` |
| [flatMap](flat-map.html) | `infix fun <E, V, V2> `[`Either`](-either.html)`<E, V>.flatMap(f: (V) -> `[`Either`](-either.html)`<E, V2>): `[`Either`](-either.html)`<E, V2>` |
| [fold](fold.html) | `fun <E, V, A> `[`Either`](-either.html)`<E, V>.fold(e: (E) -> A, v: (V) -> A): A` |
| [map](map.html) | `infix fun <E, V, V2> `[`Either`](-either.html)`<E, V>.map(f: (V) -> V2): `[`Either`](-either.html)`<E, V2>` |
| [mapError](map-error.html) | `infix fun <E, E2, V> `[`Either`](-either.html)`<E, V>.mapError(f: (E) -> E2): `[`Either`](-either.html)`<E2, V>` |
| [value](value.html) | `fun <V> value(value: V): `[`Either`](-either.html)`<`[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)`, V>` |

