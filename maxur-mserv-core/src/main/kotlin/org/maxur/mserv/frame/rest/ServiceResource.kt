package org.maxur.mserv.frame.rest

import dk.nykredit.jackson.dataformat.hal.HALLink
import dk.nykredit.jackson.dataformat.hal.annotation.Link
import dk.nykredit.jackson.dataformat.hal.annotation.Resource
import io.swagger.annotations.Api
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.maxur.mserv.frame.MicroService
import java.net.URI
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

/**
 * The type Service resource.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/29/13</pre>
 */
@Path("/service")
@Api(value = "/service", description = "Endpoint for Service infos")
class ServiceResource @Inject constructor(val service: MicroService) {

    @GET
    @Produces("application/hal+json")
    @ApiOperation(value = "Represent this service",
        response = ServiceView::class, produces = "application/hal+json")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successful operation"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    fun service(): ServiceView = ServiceView(service)
}

@Suppress("unused")
@Resource
@ApiModel(value = "Service View", description = "Service Presentation Model")
class ServiceView(service: MicroService) {

    @ApiModelProperty(value = "Service name")
    val name: String = "${service.name}: ${service.version}"

    @Link
    var self: HALLink = HALLink.Builder(URI("service")).build()

    @Link("command")
    var command: HALLink = HALLink.Builder(URI("service/command")).build()
}

