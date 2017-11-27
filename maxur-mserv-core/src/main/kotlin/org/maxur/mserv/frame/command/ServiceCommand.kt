package org.maxur.mserv.frame.command

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.swagger.annotations.ApiModel
import org.maxur.mserv.core.command.Command
import org.maxur.mserv.frame.MicroService
import org.maxur.mserv.frame.runner.MicroServiceRunner
import javax.inject.Inject

/** The service command */
@ApiModel(
    value = "ServiceCommand",
    description = "This class represents the service command"
)
@JsonDeserialize(using = ServiceCommandDeserializer::class)
abstract class ServiceCommand protected constructor(type: String) : Command(type) {

    /** The microservice */
    @Inject
    protected lateinit var service: MicroService

    companion object {
        internal fun stop() = Stop()
        internal fun restart() = Restart()
    }

    /** Stop Microservice Command */
    class Stop : ServiceCommand {

        internal constructor() : super("stop")

        constructor(service: MicroService) : this() {
            this.service = service
        }

        /** {@inheritDoc} */
        override fun run() {
            post(service.stop())
        }
    }

    /** Restart Microservice Command */
    class Restart : ServiceCommand {

        /** The microservice */
        @Inject
        private lateinit var runner: MicroServiceRunner

        internal constructor() : super("restart")

        constructor(service: MicroService, runner: MicroServiceRunner) : this() {
            this.service = service
            this.runner = runner
        }

        /** {@inheritDoc} */
        override fun run() {
            post(service.stop())
            post(runner.start())
        }
    }
}