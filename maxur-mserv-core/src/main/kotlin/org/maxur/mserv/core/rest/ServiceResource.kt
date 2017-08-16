package org.maxur.mserv.core.rest

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
import org.maxur.mserv.core.MicroService
import java.net.URI
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * The type Application resource.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/29/13</pre>
 */
@Path("/service")
@Api(value = "/service", description = "Endpoint for Service specific operations")
class ServiceResource @Inject constructor(val service: MicroService) {

    @GET
    @Produces("application/hal+json")
    @ApiOperation(value = "Represent this service",
            response = ServiceView::class, produces = "application/hal+json")
    @ApiResponses(value = *arrayOf(
            ApiResponse(code = 200, message = "Successful operation"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    )
    fun service(): ServiceView = ServiceView(service)

    @PUT()
    @Path("/{state}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Change service state",
            notes = "Commands for stop or restart service"
    )
    @ApiResponses(value = *arrayOf(
            ApiResponse(code = 204, message = "Successful operation"),
            ApiResponse(code = 400, message = "On invalid command"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    )
    fun state(
            @ApiParam(name = "state", value = "New service state", required = true, allowableValues = "stop, restart")
            @PathParam("state") command: String
    ) = when (command.toUpperCase()) {
        "STOP" -> service.deferredStop()
        "RESTART" -> service.deferredRestart()
        else -> throw IllegalArgumentException("Command '$command' unknown")
    }
}

@Suppress("unused")
@Resource
@ApiModel(value = "Service View", description = "Service Presentation Model")
class ServiceView(service: MicroService) {

    @ApiModelProperty(value = "Service name")
    val name: String = "${service.name}: ${service.version}"

    @Link
    var self: HALLink = HALLink.Builder(URI("service")).build()

    @Link("stop")
    var stop: HALLink = HALLink.Builder(URI("service/stop")).build()

    @Link("restart")
    var restart: HALLink = HALLink.Builder(URI("service/restart")).build()

}

