package org.maxur.mserv.core

/**
 * Abstract Class as template for service composite
 * @param list list of Composite leaf.
 */
abstract class Composite<T>(protected val list: MutableList<T> = ArrayList()) {

    /**
     * add new item to Composite.
     * @param value The new Item.
     */
    fun add(value: T) = list.add(value)

    /**
     * add new item to Composite.
     * @param value The new Item.
     */
    operator fun plusAssign(value: T) {
        add(value)
    }
}