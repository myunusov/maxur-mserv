package org.maxur.mserv.core.builder

/**
 * Hold list of string.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>26.08.2017</pre>
 *
 * @property strings the Strings List
 */
class StringsHolder(val strings: MutableSet<String> = mutableSetOf()) {

    /**
     * Add string to strings list.
     * @param value The string with comma separated items
     */
    operator fun plusAssign(value: String) {
        strings.addAll(value.split("\\s*,\\s*"))
    }

    /** {@inheritDoc} */
    override fun toString(): String = strings.joinToString(",")
}