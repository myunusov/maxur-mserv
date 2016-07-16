/*
 * Copyright 2015 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */


package org.maxur.mserv.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.ext.Provider;

/**
 * web server grizzly impl
 */
@Provider
public class WebServerGrizzlyImpl extends WebServer {

    private HttpServer httpServer;

    @Inject
    private ResourceConfig config;

    @Inject
    private ServiceLocator locator;

    /**
     * WebApp folder
     */
    @SuppressWarnings("unused")
    @Named("webapp.folderName")
    private String webappFolderName;

    @Override
    protected void launch() {
        httpServer = GrizzlyHttpServerFactory.createHttpServer(
            getWebappUrl(),
            config,
            locator
        );
        httpServer.getServerConfiguration().addHttpHandler(
                new StaticHttpHandler(webappFolderName),
                WEB_APP_URL + "api-docs"
        );
    }

    @Override
    protected void shutdown() {
        httpServer.shutdownNow();
    }



}