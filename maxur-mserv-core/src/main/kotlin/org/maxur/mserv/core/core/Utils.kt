package org.maxur.mserv.core.core

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>26.08.2017</pre>
 */

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its result
 * or calls onError function on amy exception.
 * @param block main function returns value of [R] type
 * @param onError hook on exception returns value of [R] type
 * @param T The type of checked class
 * @param R The type of result block function
 */
inline fun <T, R> T.checkError(block: T.() -> R, onError: (Exception) -> R): R = try {
    this.run(block)
} catch (e: Exception) {
    onError(e)
}
