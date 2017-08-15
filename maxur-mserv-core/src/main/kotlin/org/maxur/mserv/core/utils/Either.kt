@file:Suppress("unused")

package org.maxur.mserv.core.utils

@Suppress("unused")
sealed class Either<out L, out R>

fun <V> value(value: V): Either<Nothing, V> = Value(value)
fun <E> error(value: E): Either<E, Nothing> = Error(value)

fun <V> either(action: () -> V): Either<Exception, V> =
        try {
            value(action())
        } catch (e: Exception) {
            error(e)
        }

inline infix fun <E, V, V2> Either<E, V>.map(f: (V) -> V2): Either<E, V2> = when (this) {
    is Error -> this
    is Value -> Value(f(this.value))
}

infix fun <E, V, V2> Either<E, (V) -> V2>.apply(f: Either<E, V>): Either<E, V2> = when (this) {
    is Error -> this
    is Value -> f.map(this.value)
}

inline infix fun <E, V, V2> Either<E, V>.flatMap(f: (V) -> Either<E, V2>): Either<E, V2> = when (this) {
    is Error -> this
    is Value -> f(value)
}

inline infix fun <E, E2, V> Either<E, V>.mapError(f: (E) -> E2): Either<E2, V> = when (this) {
    is Error -> Error(f(error))
    is Value -> this
}

inline fun <E, V, A> Either<E, V>.fold(e: (E) -> A, v: (V) -> A): A = when (this) {
    is Error -> e(this.error)
    is Value -> v(this.value)
}

data class Error<out T>(val error: T) : Either<T, Nothing>()

data class Value<out T>(val value: T) : Either<Nothing, T>()
