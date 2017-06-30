package org.maxur.mserv.core.service.properties

import org.jvnet.hk2.annotations.Contract
import java.net.URI

/**
 * The interface Properties service.

 * @author myunusov
 * *
 * @version 1.0
 * *
 * @since <pre>30.08.2015</pre>
 */
@Contract
interface PropertiesService {

    /**
     * return properties by key

     * @param key properties key
     * *
     * @return properties by key
     */
    fun asString(key: String): String?

    /**
     * return properties by key
     *
     * @param key properties key
     *
     * @return properties by key
     */
    fun asLong(key: String): Long?

    /**
     * return properties by key
     *
     * @param key properties key
     * *
     * @return properties by key
     */
    fun asInteger(key: String): Int?

    /**
     * return properties by key
     *
     * @param key properties key
     *
     * @return properties by key
     */
    fun asURI(key: String): URI?

    /**
     * return properties by key
     *
     * @param key properties key
     *
     * @param clazz properties type
     * *
     * @return properties by key
     */
    fun <P> read(key: String, clazz: Class<P>): P?
}