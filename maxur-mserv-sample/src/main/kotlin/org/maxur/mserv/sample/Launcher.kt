package org.maxur.mserv.sample

import org.maxur.mserv.core.MicroService
import org.maxur.mserv.core.domain.Service
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
                beforeStart = { this@Launcher.beforeStart(it as MicroService) }
                afterStop = this@Launcher::afterStop
                onError = this@Launcher::onError
            }
            properties {
                format = "hocon"
            }
            services {
               rest {}
            }
        }.start()
    }
    
    fun beforeStart(service: Service) {
        ((service as MicroService).bean(ConfigParams::class.java))!!.log()
        log().info("${service.name} is started")
    }
    
    fun afterStop(service: Service) {
        log().info("${service.name} is stopped")
    }

    fun onError(@Suppress("UNUSED_PARAMETER") service: Service, exception: Exception) {
        log().error(exception.message, exception)
    }
    

}

