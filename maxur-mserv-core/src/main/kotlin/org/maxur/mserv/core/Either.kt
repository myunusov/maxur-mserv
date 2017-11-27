package org.maxur.mserv.core

/**
 * Represents a value of one of two possible types (a disjoint union.)
 * Instances of Either are either an instance of Left or Right.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/25/13</pre>
 */
sealed class Either<out L, out R>

/**
 * Create right Either.
 * @param value Either value.
 * @return Either
 */
fun <R> right(value: R): Either<Nothing, R> = Right(value)

/**
 * Create left Either.
 * @param value Either value.
 * @return Either
 */
fun <L> left(value: L): Either<L, Nothing> = Left(value)

/**
 * Fold either
 * @param leftFunction the left function
 * @param rightFunction the right function
 * @return Either
 */
inline fun <L, R, A> Either<L, R>.fold(leftFunction: (L) -> A, rightFunction: (R) -> A): A = when (this) {
    is Left -> leftFunction(this.left)
    is Right -> rightFunction(this.right)
}

/**
 * Map right either
 * @param function the right function
 * @return Either
 */
inline infix fun <L, R, R2> Either<L, R>.mapRight(function: (R) -> Either<L, R2>): Either<L, R2> = when (this) {
    is Left -> this
    is Right -> function(right)
}

/**
 * Map left either
 * @param function the left function
 * @return Either
 */
inline infix fun <L, L2, R> Either<L, R>.mapLeft(function: (L) -> L2): Either<L2, R> = when (this) {
    is Left -> Left(function(left))
    is Right -> this
}

/**
 * The Left side of the disjoint union, as opposed to the Right side.
 * @param left The value
 */
data class Left<out T>(val left: T) : Either<T, Nothing>() {
    /** {@inheritDoc} */
    override fun toString() = "Left $left"
}

/**
 * The Right side of the disjoint union, as opposed to the Left side.
 * @param right The value
 */
data class Right<out T>(val right: T) : Either<Nothing, T>() {
    /** {@inheritDoc} */
    override fun toString() = "Right $right"
}