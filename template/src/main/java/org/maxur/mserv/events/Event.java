package org.maxur.mserv.events;

/**
 * The type Event.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>16.07.2016</pre>
 */
public abstract class Event {

    private final Object entity;

    /**
     * Instantiates a new Event.
     *
     * @param entity the entity
     */
    Event(final Object entity) {
        this.entity = entity;
    }

    /**
     * Entity t.
     *
     * @param <T> the type parameter
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public <T> T entity() {
        return (T) entity;
    }
}
