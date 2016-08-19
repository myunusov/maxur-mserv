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

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Notification application event listener.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>9/24/2015</pre>
 */
public class NotificationApplicationEventListener implements ApplicationEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationApplicationEventListener.class);

    private volatile int requestCnt = 0;

    @Override
    public void onEvent(ApplicationEvent event) {
        switch (event.getType()) {
            case INITIALIZATION_FINISHED:
                LOG.info("Application "
                    + event.getResourceConfig().getApplicationName()
                    + " was initialized.");
                break;
            case DESTROY_FINISHED:
                LOG.info("Application "
                    + event.getResourceConfig().getApplicationName() + " destroyed.");
                break;
            default:
                //ignore
        }
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        requestCnt++;
        LOG.info("Request " + requestCnt + " started.");
        // return the listener instance that will handle this request.
        return new NotificationRequestEventListener(requestCnt);
    }
}