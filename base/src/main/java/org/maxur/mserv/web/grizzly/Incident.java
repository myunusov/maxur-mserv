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

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents any incident on application to transfer it.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/25/13</pre>
 */
public class Incident implements Serializable {

    private static final long serialVersionUID = 2368849548039200044L;

    /**
     * Message
     */
    private String message;


    /**
     * Default constructor for serialization.
     */
    @SuppressWarnings("UnusedDeclaration")
    public Incident() {
    }

    /**
     * Constructor.
     *
     * @param message Message
     */
    public Incident(final String message) {
        this.message = message;
    }

    /**
     * Create list of incidents by messages.
     *
     * @param messages list of messages.
     * @return list of incidents.
     */
    public static List<Incident> incidents(final String... messages) {
        return Arrays.stream(messages)
            .map(Incident::new)
            .collect(Collectors.toList());
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    @SuppressWarnings("UnusedDeclaration")
    public String getMessage() {
        return message;
    }
}
