package org.maxur.mserv.microservice;

import org.maxur.mserv.core.Event;
import org.maxur.mserv.core.LifecycleEvent;

import static java.lang.String.format;

/**
 * The type Application stopped event.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/28/2016</pre>
 */
public final class ServiceStoppedEvent extends LifecycleEvent<MicroService> {

    private ServiceStoppedEvent(final MicroService service) {
        super(service);
    }

    /**
     * Application stopped event.
     *
     * @param service the MicroService
     * @return the event
     */
    public static Event serviceStoppedEvent(final MicroService service) {
        return new ServiceStoppedEvent(service);
    }

    @Override
    public String message() {
        return format("Service '%s' is stopped",
                getEntity().getName()
        );
    }

}
