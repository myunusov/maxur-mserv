package org.maxur.mserv.microservice;

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
public final class ServiceStartedEvent extends LifecycleEvent<MicroService> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private ServiceStartedEvent(final MicroService service) {
        super(service);
    }

    /**
     * Application started event.
     *
     * @param service the MicroService
     * @return the event
     */
    public static ServiceStartedEvent serviceStartedEvent(final MicroService service) {
        return new ServiceStartedEvent(service);
    }

    @Override
    public String message() {
        return format("Service %s v.%s (%s) is started",
                getEntity().getName(),
                getEntity().getVersion(),
                dateFormat.format(getEntity().getReleased())
        );
    }
}
