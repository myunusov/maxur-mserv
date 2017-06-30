package org.maxur.mserv.sample

import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.service.hk2.DSL
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
    @JvmStatic fun main(args: Array<String>)  {
        DSL.service {
            title = ":name"
            packages = "org.maxur.mserv.sample"
            observers {
                beforeStart = this@Launcher::beforeStart
                afterStop = this@Launcher::afterStop
                onError = this@Launcher::onError
            }
            properties {
                format = "Hocon"
            }
            services {
               rest {}
            }
        }.start()
    }
    
    fun beforeStart(service: MicroService) {
        (service.bean(ConfigParams::class.java))!!.log()
        log().info("${service.name} is started")
    }
    
    fun afterStop(service: MicroService) {
        log().info("${service.name} is stopped")
    }

    fun onError(@Suppress("UNUSED_PARAMETER") service: MicroService, exception: Exception) {
        log().error(exception.message, exception)
    }
    

}

