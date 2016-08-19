package org.maxur.ddd.ui.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.maxur.ddd.service.Uow;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The type Example resource.
 *
 * @author Alexey Elin
 * @version 1.0 18.12.2015.
 */
@Path("/example")
@Singleton
@Api(tags = "Example Resource")
@Slf4j
public class ExampleResource {

    @Inject
    private Uow value;

    /**
     * Do hello response.
     *
     * @return the response
     */
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(
        value = "Say hello", response = String.class
    )
    public String doHello() {
        log.info("example resource: /hello");
        return "Hello World!";
    }
}
