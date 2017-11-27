package org.maxur.mserv.frame.rest

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
import io.swagger.annotations.Example
import io.swagger.annotations.ExampleProperty
import org.maxur.mserv.core.command.CommandHandler
import java.net.URI
import javax.inject.Inject
import javax.validation.Valid
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
class RunningCommandResource @Inject constructor(
    /** the Command Handler */
    private val handler: CommandHandler
) {

    /** Returns all running commands */
    @GET
    @Produces("application/hal+json")
    @ApiOperation(value = "Represent this service running commands",
        response = ServiceView::class, produces = "application/hal+json")
    @ApiResponses(value = *arrayOf(
        ApiResponse(code = 200, message = "Successful operation"),
        ApiResponse(code = 500, message = "Internal server error")
    )
    )
    fun commands(): RunningCommandsView = RunningCommandsView()

    /** Add new command to queue */
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
    fun runCommand(
        @ApiParam(
            name = "command",
            type = "object",
            value = "New service command",
            required = true,
            examples = Example(value = ExampleProperty(
                mediaType = "application/json", value = "{ \"type\": \"restart\" }"
            ))
        )
        @Valid command: org.maxur.mserv.frame.command.ServiceCommand
    ) = handler
        .withInjector()
        .withDelay(1000)
        .handle(command)
}

/** Running command endpoint HAL view */
@Suppress("unused")
@Resource
@ApiModel(value = "Service Running Commands View", description = "Service Running Commands Model")
class RunningCommandsView {

    /** List of commands in execution queue */
    @ApiModelProperty(value = "Service Running Commands")
    val commands: List<String> = emptyList()

    /** This endpoint */
    @Link
    var self: HALLink = HALLink.Builder(URI("command")).build()
}
