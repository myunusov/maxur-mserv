package org.maxur.mserv.core.rest

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import dk.nykredit.jackson.dataformat.hal.HALLink
import dk.nykredit.jackson.dataformat.hal.annotation.Link
import dk.nykredit.jackson.dataformat.hal.annotation.Resource
import io.swagger.annotations.Api
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.hibernate.validator.constraints.NotBlank
import org.maxur.mserv.core.MicroService
import java.net.URI
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.constraints.Pattern
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * The type Running command resource.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/29/13</pre>
 */
@Path("/service/command")
@Api(value = "/service/command", description = "Endpoint for Service specific operations")
class RunningCommandResource @Inject constructor(val service: MicroService) {

    @GET
    @Produces("application/hal+json")
    @ApiOperation(value = "Represent this service running commands",
            response = ServiceView::class, produces = "application/hal+json")
    @ApiResponses(value = *arrayOf(
            ApiResponse(code = 200, message = "Successful operation"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    )
    fun service(): RunningCommandsView = RunningCommandsView()

    @POST()
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Insert new command",
            notes = "Commands for stop or restart service"
    )
    @ApiResponses(value = *arrayOf(
            ApiResponse(code = 204, message = "Successful operation"),
            ApiResponse(code = 400, message = "On invalid command"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    )
    fun state(
            @ApiParam(name = "command", value = "New service command", required = true)
            @Valid command: ServiceCommand
    ) = when (command.type.toUpperCase()) {
        "STOP" -> service.deferredStop()
        "RESTART" -> service.deferredRestart()
        else -> throw IllegalArgumentException("Command '$command' unknown")
    }

    @ApiModel(
            value = "TenantConfiguration", description = "This class represents the service command"
    )
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class ServiceCommand(
            @ApiModelProperty(
                    name = "type",
                    value = "type of command",
                    required = true,
                    allowableValues = "stop, restart"
            )
            @NotBlank
            @Pattern(regexp = "^(stop|restart)$")
            @JsonProperty("type")
            val type: String
    )
}

@Suppress("unused")
@Resource
@ApiModel(value = "Service Running Commands View", description = "Service Running Commands Model")
class RunningCommandsView() {

    @ApiModelProperty(value = "Service Running Commands")
    val commands: List<String> = emptyList()

    @Link
    var self: HALLink = HALLink.Builder(URI("service/command")).build()

}
