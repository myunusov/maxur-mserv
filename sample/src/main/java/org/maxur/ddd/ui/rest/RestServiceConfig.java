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

import org.jvnet.hk2.annotations.Service;
import org.maxur.mserv.web.grizzly.JerseyResourceConfig;

/**
 * Rest Service Configuration
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>30.08.2015</pre>
 */
@Service
public class RestServiceConfig extends JerseyResourceConfig {

    /**
     * constructor
     */
    public RestServiceConfig() {
        setApplicationName("t-ddd");
        packages("org.maxur.ddd.ui.rest");
    }

}
