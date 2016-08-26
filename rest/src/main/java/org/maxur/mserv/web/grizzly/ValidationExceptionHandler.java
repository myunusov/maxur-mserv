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

import org.maxur.mserv.core.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * The type Validation exception handler.
 *
 * @author Igor Chirkov
 * @since <pre>9/21/2015</pre>
 */
public class ValidationExceptionHandler implements ExceptionMapper<ValidationException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    @Override
    public Response toResponse(ValidationException exception) {
        LOGGER.warn(exception.getMessage(), exception);
        return Response
            .status(BAD_REQUEST)
            .type(APPLICATION_JSON)
            .entity(makeErrorEntity(exception))
            .build();
    }

    private GenericEntity<List<Incident>> makeErrorEntity(final ValidationException exception) {
        return new GenericEntity<List<Incident>>(Incident.incidents("Invalid data", exception.getMessage())) {
        };
    }
}