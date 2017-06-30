package org.maxur.mserv.core.annotation

/**
 * Configuration Property Value annotation
 *
 * @param key the key of property
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class Value(val key: String)
