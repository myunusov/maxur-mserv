package org.maxur.mserv.core.core

/**
 * Represents a result of one of two possible types (a disjoint union.)
 * Instances of Result are either an instance of Value or Error (Throwable).
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/25/13</pre>
 *
 * @see https://github.com/MarioAriasC/funKTionale/blob/master/funktionale-either/src/test/kotlin/org/funktionale/either/EitherTest.kt
 * @see https://medium.com/@lupajz/you-either-love-it-or-you-havent-used-it-yet-a55f9b866dbe
 *
 */
sealed class Result<out E : Throwable, out V>

/**
 * Create Value.
 * @param value The value
 * @return the Result
 */
fun <V> value(value: V): Result<Nothing, V> = Value(value)

/**
 * Create Error.
 * @param value The error
 * @return the Result
 */
fun <E : Throwable> error(value: E): Result<E, Nothing> = ErrorResult(value)

/**
 * Run action and create Result.
 * @param action The action
 * @return the Result
 */
fun <E : Throwable, V> either(action: () -> V): Result<E, V> =
    try {
        value(action())
    } catch (e: Throwable) {
        @Suppress("UNCHECKED_CAST")
        error(e as E)
    }

/**
 * Map correct result by function
 * @param function The function result to result
 * @return new result
 */
inline infix fun <E : Throwable, V, V2> Result<E, V>.map(function: (V) -> V2): Result<E, V2>
    = when (this) {
    is ErrorResult -> this
    is Value -> Value(function(this.value))
}

/**
 * Map error by function
 * @param function The function
 * @return new result
 */
inline infix fun <E : Throwable, E2 : Throwable, V> Result<E, V>.mapError(f: (E) -> E2): Result<E2, V>
    = when (this) {
    is ErrorResult -> ErrorResult(f(error))
    is Value -> this
}

/**
 * FlatMap correct result by function
 * @param function The function value to value
 * @return new result
 */
inline infix fun <E : Throwable, V, V2> Result<E, V>.flatMap(function: (V) -> Result<E, V2>): Result<E, V2>
    = when (this) {
    is ErrorResult -> this
    is Value -> function(value)
}

/**
 * Fold either
 * @param errorFunction the left function
 * @param valueFunction the right function
 * @return new result
 */
inline fun <E : Throwable, V, A> Result<E, V>.fold(errorFunction: (E) -> A, valueFunction: (V) -> A): A
    = when (this) {
    is ErrorResult -> errorFunction(this.error)
    is Value -> valueFunction(this.value)
}

/**
 * Error result class
 * @param error Any Throwable
 */
data class ErrorResult<out T : Throwable>(val error: T) : Result<T, Nothing>()

/**
 * Correct result class
 * @param value Anu correct value
 */
data class Value<out T>(val value: T) : Result<Nothing, T>()
