/*-
 * #%L
 * Restrulz
 * %%
 * Copyright (C) 2015 - 2017 Maxim Yunusov
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 * #L%
 */
package org.maxur.mserv.frame.rest

import java.io.Serializable
import java.util.Arrays
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
