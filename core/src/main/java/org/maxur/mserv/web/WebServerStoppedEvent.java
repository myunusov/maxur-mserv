package org.maxur.mserv.web;

import org.maxur.mserv.core.Event;
import org.maxur.mserv.core.LifecycleEvent;

/**
 * The type Application stopped event.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/28/2016</pre>
 */
public final class WebServerStoppedEvent extends LifecycleEvent<WebServer> {

    private WebServerStoppedEvent(final WebServer service) {
        super(service);
    }

    /**
     * Application stopped event.
     *
     * @param service the MicroService
     * @return the event
     */
    public static Event serviceStoppedEvent(final WebServer service) {
        return new WebServerStoppedEvent(service);
    }

    @Override
    public String message() {
        return "Stop Web Server";
    }

}
