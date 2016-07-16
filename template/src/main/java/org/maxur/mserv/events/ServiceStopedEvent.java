package org.maxur.mserv.events;

/**
 * The type On stop event.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>16.07.2016</pre>
 */
public class ServiceStopedEvent extends Event {

    /**
     * Instantiates a new On stop event.
     *
     * @param entity the entity
     */
    public ServiceStopedEvent(final Object entity) {
        super(entity);
    }
}
