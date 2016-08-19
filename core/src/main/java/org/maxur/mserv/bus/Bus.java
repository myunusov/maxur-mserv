package org.maxur.mserv.bus;

import org.maxur.mserv.core.Event;

/**
 * The interface Bus.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/28/2016</pre>
 */
public interface Bus {

    /**
     * Unregister observer.
     *
     * @param observer the observer
     */
    void unregister(Object observer);

    /**
     * Post event to bus.
     *
     * @param event the event
     */
    void post(Event event);

    /**
     * Register observer.
     *
     * @param observer the observer
     */
    void register(Object observer);

    /**
     * Bus Identifier.
     *
     * @return the Bus Identifier
     */
    String id();

}
