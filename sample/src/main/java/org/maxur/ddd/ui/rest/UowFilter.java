package org.maxur.ddd.ui.rest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>8/18/2016</pre>
 */
public class UowFilter implements ContainerResponseFilter, ContainerRequestFilter {

    @Override
    public void filter(
        final ContainerRequestContext requestContext
    ) throws IOException {

    }

    @Override
    public void filter(
        final ContainerRequestContext requestContext,
        final ContainerResponseContext responseContext
    ) throws IOException {

    }
}
