---
title: Either - maxur-mserv-core
---

[maxur-mserv-core](../index.html) / [org.maxur.mserv.core.utils](index.html) / [Either](.)

# Either

`sealed class Either<out L, out R>` [(source)](https://github.com/myunusov/maxur-mserv/tree/master/maxur-mserv-core/src/main/kotlin/org/maxur/mserv/core/utils/Either.kt#L6)

### Extension Functions

| [apply](apply.html) | `infix fun <E, V, V2> Either<E, (V) -> V2>.apply(f: Either<E, V>): Either<E, V2>` |
| [flatMap](flat-map.html) | `infix fun <E, V, V2> Either<E, V>.flatMap(f: (V) -> Either<E, V2>): Either<E, V2>` |
| [fold](fold.html) | `fun <E, V, A> Either<E, V>.fold(e: (E) -> A, v: (V) -> A): A` |
| [map](map.html) | `infix fun <E, V, V2> Either<E, V>.map(f: (V) -> V2): Either<E, V2>` |
| [mapError](map-error.html) | `infix fun <E, E2, V> Either<E, V>.mapError(f: (E) -> E2): Either<E2, V>` |

### Inheritors

| [Error](-error/index.html) | `data class Error<out T> : Either<T, `[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)`>` |
| [Value](-value/index.html) | `data class Value<out T> : Either<`[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)`, T>` |

