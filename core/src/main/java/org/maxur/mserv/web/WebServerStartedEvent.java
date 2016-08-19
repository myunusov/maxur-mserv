package org.maxur.mserv.web;

import org.maxur.mserv.core.LifecycleEvent;

import java.text.SimpleDateFormat;

import static java.lang.String.format;

/**
 * The type Application started event.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/28/2016</pre>
 */
public final class WebServerStartedEvent extends LifecycleEvent<WebServer> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private WebServerStartedEvent(final WebServer service) {
        super(service);
    }

    /**
     * Application started event.
     *
     * @param service the MicroService
     * @return the event
     */
    public static WebServerStartedEvent serviceStartedEvent(final WebServer service) {
        return new WebServerStartedEvent(service);
    }

    @Override
    public String message() {
        return format("Web Server is started on %s", getEntity().webConfig().uri());
    }
}
