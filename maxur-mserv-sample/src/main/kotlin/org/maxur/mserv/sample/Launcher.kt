package org.maxur.mserv.sample

import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.service.msbuilder.Kotlin
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

            properties {
                format = "hocon"
            }

            services += rest {}

            beforeStart += this@Launcher::beforeStart
            afterStop += { log().info("Service is stopped") }
            onError += { exception ->  log().error(exception.message, exception) }

        }.start()
    }

    fun beforeStart(service: MicroService, configParams: ConfigParams) {
        configParams.log()
        log().info("${service.name} is started")
    }


}

