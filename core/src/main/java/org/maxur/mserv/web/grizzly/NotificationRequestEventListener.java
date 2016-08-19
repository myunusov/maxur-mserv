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

import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Notification request event listener.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>9/24/2015</pre>
 */
public class NotificationRequestEventListener implements RequestEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationRequestEventListener.class);

    private final int requestNumber;
    private final long startTime;

    /**
     * Instantiates a new Notification request event listener.
     *
     * @param requestNumber number of request
     */
    public NotificationRequestEventListener(int requestNumber) {
        this.requestNumber = requestNumber;
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onEvent(RequestEvent event) {
        switch (event.getType()) {
            case RESOURCE_METHOD_START:
                LOG.info("Resource method "
                    + event.getUriInfo().getMatchedResourceMethod()
                    .getHttpMethod()
                    + " started for request " + requestNumber);
                break;
            case FINISHED:
                LOG.info("Request " + requestNumber
                    + " finished. Processing time "
                    + (System.currentTimeMillis() - startTime) + " ms.");
                break;
            default:
                //ignore
        }
    }
}
