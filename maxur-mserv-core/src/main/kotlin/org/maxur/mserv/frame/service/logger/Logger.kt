package org.maxur.mserv.frame.service.logger

interface Logger {
    fun error(source: Class<Any>, message: String, error: Exception? = null)
    fun warn(source: Class<Any>, message: String)
    fun info(source: Class<Any>, message: String)
    fun debug(source: Class<Any>, message: String)
    fun trace(source: Class<Any>, message: String)
}