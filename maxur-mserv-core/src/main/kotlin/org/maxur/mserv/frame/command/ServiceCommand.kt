package org.maxur.mserv.frame.command

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.validator.constraints.NotBlank
import org.maxur.mserv.core.command.Command
import org.maxur.mserv.frame.MicroService
import org.maxur.mserv.frame.runner.MicroServiceRunner
import javax.inject.Inject
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
    override val type: String
) : Command {

    /** The microservice */
    @Inject
    protected lateinit var service: MicroService

    companion object {
        internal fun stop() = Stop()
        internal fun restart() = Restart()
    }

    class Stop : ServiceCommand {

        internal constructor() : super("stop")

        constructor(service: MicroService) : this() {
            this.service = service
        }

        /** {@inheritDoc} */
        override fun run() = service.stop()
    }

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
            service.stop()
            runner.start()
        }
    }
}