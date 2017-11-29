package org.maxur.mserv.frame.service.logger

import org.slf4j.LoggerFactory

/**
 *
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/29/2017</pre>
 */
class LoggerSlf4JImpl : Logger {

    override fun error(source: Class<Any>, message: String, error: Exception?) {
        error?.apply {
            logger(source).error(message, error)
        } ?:
            logger(source).error(message)
    }

    override fun warn(source: Class<Any>, message: String) {
        logger(source).warn(message)
    }

    override fun info(source: Class<Any>, message: String) {
        logger(source).info(message)
    }

    override fun debug(source: Class<Any>, message: String) {
        logger(source).debug(message)
    }

    override fun trace(source: Class<Any>, message: String) {
        logger(source).trace(message)
    }

    // TODO Cash it
    private fun logger(source: Class<Any>) = LoggerFactory.getLogger(source.javaClass)
}