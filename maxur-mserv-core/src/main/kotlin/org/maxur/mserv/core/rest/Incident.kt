package org.maxur.mserv.core.rest

import java.io.Serializable
import java.util.*
import java.util.stream.Collectors

/**
 * This class represents any incident on application to transfer it.

 * @author Maxim Yunusov
 * *
 * @version 1.0
 * *
 * @since <pre>11/25/13</pre>
 */
open class Incident(@Suppress("unused") val message: String) : Serializable {

    companion object {
        fun incidents(vararg messages: String): List<Incident> {
            return Arrays.stream(messages)
                    .map<Incident>({ Incident(it) })
                    .collect(Collectors.toList<Incident>())
        }
    }

}


