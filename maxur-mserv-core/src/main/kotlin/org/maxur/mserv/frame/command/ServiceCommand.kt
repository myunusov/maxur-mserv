package org.maxur.mserv.frame.command

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.maxur.mserv.core.command.Command
import org.maxur.mserv.frame.MicroService
import org.maxur.mserv.frame.service.MicroServiceBuilder
import javax.inject.Inject
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

/** The service command */
@ApiModel(
    value = "ServiceCommand",
    description = "This class represents the service command"
)
@JsonDeserialize(using = ServiceCommandDeserializer::class)
abstract class ServiceCommand protected constructor(
    /** The command type */
    @ApiModelProperty(
        dataType = "string",
        name = "type",
        value = "type of the command",
        notes = "Type of the command",
        required = true,
        allowableValues = "stop, restart",
        example = "restart"
    )
    @NotBlank
    @Pattern(regexp = "^(stop|restart)$")
    val type: String) : Command() {

    companion object {
        internal fun stop() = Stop()
        internal fun restart() = Restart()
    }

    /** Stop Microservice Command */
    class Stop : ServiceCommand {

        /** The microservice */
        @Inject
        protected lateinit var service: MicroService

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

        /** The microservice runner */
        @Inject
        private lateinit var builder: MicroServiceBuilder

        /** The microservice */
        @Inject
        protected lateinit var service: MicroService

        internal constructor() : super("restart")

        constructor(service: MicroService, runner: MicroServiceBuilder) : this() {
            this.service = service
            this.builder = runner
        }

        /** {@inheritDoc} */
        override fun run() {
            post(service.stop())
            post(builder.build().start())
        }
    }

    /** Restart Microservice Command */
    class Start : ServiceCommand {

        /** The microservice runner */
        @Inject
        private lateinit var service: MicroService

        internal constructor() : super("start")

        constructor(service: MicroService) : this() {
            this.service = service
        }

        /** {@inheritDoc} */
        override fun run() {
            post(service.start())
        }
    }
}