/*
 * Copyright 2015 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */

package org.maxur.mserv.microservice.base;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.maxur.mserv.core.annotation.Param;
import org.maxur.mserv.bus.Bus;
import org.maxur.mserv.microservice.MicroService;
import org.maxur.mserv.web.WebServer;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

import static org.maxur.mserv.microservice.ServiceStartedEvent.serviceStartedEvent;
import static org.maxur.mserv.microservice.ServiceStoppedEvent.serviceStoppedEvent;

/**
 * Rest Micro Service.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>9/15/2015</pre>
 */
@Slf4j
public class MicroServiceRestImpl implements MicroService {

    private final WebServer webServer;

    private final Bus bus;

    private volatile boolean keepRunning = true;

    @Param(key = "version", optional = true)
    @Getter
    private String version = "";

    @Param(key = "released", optional = true)
    @Getter
    private Date released = new Date();

    @Getter
    private String name = "";


    /**
     * Instantiates a new Micro service rest.
     * <p>
     *
     * @param webServer webServer
     * @param bus       the Event bus
     */
    @Inject
    public MicroServiceRestImpl(
            final WebServer webServer,
            @Named("event.bus") Bus bus
    ) {
        this.webServer = webServer;
        this.bus = bus;

        final Thread mainThread = Thread.currentThread();

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                keepRunning = false;
                try {
                    mainThread.join();
                    MicroServiceRestImpl.this.stop();
                } catch (InterruptedException e) {
                    log.error("Error on stop service: ", e);
                    Thread.currentThread().interrupt();
                }
            }
        });

    }

    /**
     * Is keep running boolean.
     *
     * @return the boolean
     */
    public boolean isKeepRunning() {
        return keepRunning;
    }


    @Override
    public final void start() {
        if (keepRunning) {
            webServer.start();
            bus.post(serviceStartedEvent(this));
        }
    }

    @Override
    public final void stop() {
        webServer.stop();
        bus.post(serviceStoppedEvent(MicroServiceRestImpl.this));
    }

    @Override
    public MicroService withName(final String name) {
        this.name = name;
        return this;
    }

}
