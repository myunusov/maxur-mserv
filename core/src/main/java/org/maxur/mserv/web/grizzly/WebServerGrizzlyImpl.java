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

import jersey.repackaged.com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.glassfish.grizzly.http.server.HttpHandlerRegistration;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.utils.Charsets;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.ServiceLocatorProvider;
import org.glassfish.jersey.grizzly2.httpserver.JerseyGrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.internal.LocalizationMessages;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.process.JerseyProcessingUncaughtExceptionHandler;
import org.glassfish.jersey.server.ServerProperties;
import org.jetbrains.annotations.NotNull;
import org.maxur.mserv.bus.Bus;
import org.maxur.mserv.web.WebServer;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;

/**
 * web server grizzly impl
 */
@Provider
public class WebServerGrizzlyImpl extends WebServer {

    private HttpServer httpServer;

    private final JerseyResourceConfig config;

    private final ServiceLocator locator;


    /**
     * Instantiates a new Web server grizzly.
     *
     * @param config  the config
     * @param locator the locator
     * @param bus     the bus
     */
    @Inject
    public WebServerGrizzlyImpl(
        final JerseyResourceConfig config,
        final ServiceLocator locator,
        @Named("event.bus") final Bus bus
    ) {
        super(bus);
        this.locator = locator;
        this.config = enrichConfig(config);
        makeLoggerBridge();
    }

    @Override
    protected void launch() {
        makeHttpService();
        try {
            httpServer.start();
        } catch (IOException var12) {
            httpServer.shutdownNow();
            throw new ProcessingException(LocalizationMessages.FAILED_TO_START_SERVER(var12.getMessage()), var12);
        }
    }


    @Override
    protected void shutdown() {
        if (httpServer != null) {
            httpServer.shutdownNow();
        }
    }

    private JerseyResourceConfig enrichConfig(final JerseyResourceConfig config) {
        config.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        config.property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
        config.register(JacksonFeature.class);
        config.register(RuntimeExceptionHandler.class);
        config.register(ValidationExceptionHandler.class);
        config.register(new ServiceLocatorFeature());
        config.register(NotificationApplicationEventListener.class);
        return config;
    }

    private void makeHttpService() {
        httpServer = createHttpServer(makeNetworkListener());
        final ServerConfiguration serverConfiguration = serverConfiguration();
        makeDynamicHandlers(serverConfiguration);
        makeStaticHandlers(serverConfiguration);

    }

    private void makeStaticHandlers(final ServerConfiguration serverConfiguration) {
        webConfig().content().entrySet().forEach(
            e -> serverConfiguration.addHttpHandler(
                new StaticHttpHandler(e.getKey()),
                normalisePath(WEB_APP_URL + e.getValue()))
        );
    }

    private void makeDynamicHandlers(final ServerConfiguration serverConfiguration) {
        final URI uri = webConfig().restUri();
        final JerseyGrizzlyHttpContainer handler = new JerseyGrizzlyHttpContainer(config, locator);
        final String contextPath = normalisePath(uri.getPath());
        serverConfiguration.addHttpHandler(
            handler,
            HttpHandlerRegistration.builder().
                contextPath(contextPath).
                build()
        );
    }

    @NotNull
    private ServerConfiguration serverConfiguration() {
        final ServerConfiguration serverConfiguration = httpServer.getServerConfiguration();
        serverConfiguration.setPassTraceRequest(true);
        serverConfiguration.setDefaultQueryEncoding(Charsets.UTF8_CHARSET);
        return serverConfiguration;
    }

    @NotNull
    private String normalisePath(final String path) {
        String ex = path.replaceAll("/{2,}", "/");
        return ex.endsWith("/") ? ex.substring(0, ex.length() - 1) : ex;
    }


    @NotNull
    private HttpServer createHttpServer(@NotNull final NetworkListener networkListener) {
        final HttpServer server = new HttpServer();
        server.addListener(networkListener);
        return server;
    }

    @NotNull
    private NetworkListener makeNetworkListener() {
        final URI uri = webConfig().restUri();
        NetworkListener listener = new NetworkListener("grizzly", uri.getHost(), uri.getPort());
        listener.getTransport()
            .getWorkerThreadPoolConfig()
            .setThreadFactory((
                new ThreadFactoryBuilder())
                .setNameFormat("grizzly-http-server-%d")
                .setUncaughtExceptionHandler(new JerseyProcessingUncaughtExceptionHandler()).build()
            );
        listener.setSecure(false);
        return listener;
    }


    private void makeLoggerBridge() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    /**
     * service locator feature
     */
    private static class ServiceLocatorFeature implements Feature {

        @Override
        public boolean configure(FeatureContext context) {
            ServiceLocatorProvider.getServiceLocator(context);
            return true;
        }
    }

}
