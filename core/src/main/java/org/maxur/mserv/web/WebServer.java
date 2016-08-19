/*
 * Copyright 2015 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */

package org.maxur.mserv.web;

import org.maxur.mserv.core.annotation.Param;
import org.maxur.mserv.bus.Bus;

/**
 * This Abstract class represents interface of Web Server.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>30.08.2015</pre>
 */
public abstract class WebServer {

    /**
     * webapp folder url
     */
    protected static final String WEB_APP_URL = "/";

    private final Bus bus;

    @Param(key = "web")
    private WebConfig webConfig;


    /**
     * Instantiates a new Web server.
     *
     * @param bus the bus
     */
    protected WebServer(final Bus bus) {
        this.bus = bus;
    }

    /**
     * Start Web server.
     */
    public void start() {
        launch();
        bus.post(WebServerStartedEvent.serviceStartedEvent(this));
    }


    /**
     * Stop Web server.
     */
    public void stop() {
        bus.post(WebServerStoppedEvent.serviceStoppedEvent(this));
        shutdown();
    }


    /**
     * web server launch
     */
    protected abstract void launch();

    /**
     * web server shutdown
     */
    protected abstract void shutdown();


    /**
     * WebApp URL
     *
     * @return the web config
     */
    public WebConfig webConfig() {
        return webConfig;
    }

}
