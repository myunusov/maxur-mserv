/*
 * Copyright 2015 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */

package org.maxur.ddd.ui.rest;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * The type Application resource.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/29/13</pre>
 */
@Path("/application")
@Api(tags = "Application Resources")
public class ApplicationResource {

    /**
     * Gets a application documentation
     *
     * @param uriInfo uriInfo
     * @return response ok
     */
    @GET
    @Path("/swagger.json")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
        value = "Gets a REST interface documentation.",
        response = String.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 500, message = "System Error")
    })
    public String getSwaggerDoc(@Context UriInfo uriInfo) {
        final URI uri = uriInfo.getBaseUri();
        final String baseUri = String.format("%s:%d", uri.getHost(), uri.getPort());
        final URL url = Resources.getResource("swagger.json");
        try {
            final String swaggerDoc = Resources.toString(url, Charsets.UTF_8);
            return swaggerDoc.replaceAll("baseurl", baseUri);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}