package org.maxur.mserv.events;

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>16.07.2016</pre>
 */
public class ParametersLoadedEvent extends Event {

    private final Object properties;

    /**
     * Instantiates a new Event.
     *  @param entity the entity
     *  @param properties user properties
     */
    public ParametersLoadedEvent(final Object entity, final Object properties) {
        super(entity);
        this.properties = properties;
    }

    public Object properties() {
        return properties;
    }
}
