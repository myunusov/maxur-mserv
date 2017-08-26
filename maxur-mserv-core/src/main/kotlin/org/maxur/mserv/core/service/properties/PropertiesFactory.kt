@file:Suppress("unused")

package org.maxur.mserv.core.service.properties

import org.jvnet.hk2.annotations.Contract
import org.maxur.mserv.core.core.Result

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
@Contract
abstract class PropertiesFactory {
    abstract fun make(source: PropertiesSource): Result<Exception, Properties>
}
