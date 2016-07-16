package org.maxur.mserv.events;

/**
 * The type On start event.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>16.07.2016</pre>
 */
public class ServiceStartedEvent extends Event {

    /**
     * Instantiates a new On start event.
     *
     * @param entity the entity
     */
    public ServiceStartedEvent(final Object entity) {
        super(entity);
    }
}
