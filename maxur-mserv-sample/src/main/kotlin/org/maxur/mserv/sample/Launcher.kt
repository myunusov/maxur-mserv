package org.maxur.mserv.sample

import org.maxur.mserv.core.embedded.WebServer
import org.maxur.mserv.core.service.msbuilder.Kotlin
import org.maxur.mserv.core.service.properties.PropertiesSource
import org.maxur.mserv.sample.params.ConfigParams
import org.slf4j.LoggerFactory

/**
 * Application Launcher
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
object Launcher {

    private fun log() = LoggerFactory.getLogger(Launcher::class.java)

    /**
     * Command line entry point. This method kicks off the building of a application  object
     * and executes it.
     *
     * @param args - arguments of command.
     */
    @JvmStatic fun main(args: Array<String>) {
        Kotlin.service {
            title = ":name"
            packages = "org.maxur.mserv.sample"
            properties { format = "hocon" }
            services += rest { afterStart += this@Launcher::afterWebServiceStart }
            beforeStart += this@Launcher::beforeStart
            afterStart += { service ->  log().info("${service.name} is started") }
            afterStop += { _ ->  log().info("Microservice is stopped") }
            onError += { exception ->  log().error(exception.message, exception) }
        }.start()
    }

    fun beforeStart(configParams: ConfigParams, config: PropertiesSource) {
        log().info("Properties Source is '${config.format}'\n")
        configParams.log()
    }

    fun afterWebServiceStart(service: WebServer) {
        log().info("${service.name} is started on ${service.baseUri}\"")
        log().info(service.entries().toString())
    }


}

