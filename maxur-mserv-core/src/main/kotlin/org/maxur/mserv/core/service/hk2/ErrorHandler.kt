@file:Suppress("unused")

package org.maxur.mserv.core.service.hk2

import org.glassfish.hk2.api.ErrorInformation
import org.glassfish.hk2.api.ErrorService
import org.jvnet.hk2.annotations.Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * Application Configurations
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>01.09.2015</pre>
 */
@Service
open class ErrorHandler: ErrorService {

    companion object {
        val log: Logger = LoggerFactory.getLogger(ErrorHandler::class.java)
    }

    var latestError: Exception? = null

    override fun onFailure(errorInformation: ErrorInformation) {
        latestError = errorInformation.associatedException
        if (log.isDebugEnabled) {
            log.error("Bean initialization error: ", latestError)
        }
    }

}
