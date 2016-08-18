package org.maxur.mserv.events;

/**
 * The type Parameters loaded event.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>16.07.2016</pre>
 */
public class PropertiesLoadedEvent extends Event {

    private final Object properties;

    /**
     * Instantiates a new Event.
     *
     * @param entity     the entity
     * @param properties user properties
     */
    public PropertiesLoadedEvent(final Object entity, final Object properties) {
        super(entity);
        this.properties = properties;
    }

    /**
     * Properties object.
     *
     * @return the object
     */
    public Object properties() {
        return properties;
    }
}
