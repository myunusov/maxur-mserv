/*
 * Copyright 2015 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */


package org.maxur.mserv.web.grizzly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

/**
 * The type Runtime exception handler.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>11.09.2015</pre>
 */
public class RuntimeExceptionHandler implements ExceptionMapper<RuntimeException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionHandler.class);

    @Override
    public Response toResponse(RuntimeException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return Response
                .status(INTERNAL_SERVER_ERROR)
                .type(APPLICATION_JSON)
                .entity(makeErrorEntity(exception))
                .build();
    }

    private GenericEntity<List<Incident>> makeErrorEntity(final RuntimeException exception) {
        return new GenericEntity<List<Incident>>(Incident.incidents("System error", exception.getMessage())) {
        };
    }


}
