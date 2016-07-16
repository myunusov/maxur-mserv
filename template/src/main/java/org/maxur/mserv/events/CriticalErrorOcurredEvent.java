package org.maxur.mserv.events;

/**
 * The type Critical error ocurred event.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>16.07.2016</pre>
 */
public class CriticalErrorOcurredEvent extends Event {

    private final RuntimeException e;

    /**
     * Instantiates a new Critical error ocurred event.
     *
     * @param entity the entity
     * @param e      the e
     */
    public CriticalErrorOcurredEvent(final Object entity, final RuntimeException e) {
        super(entity);
        this.e = e;
    }

    /**
     * Error runtime exception.
     *
     * @return the runtime exception
     */
    public RuntimeException error() {
        return e;
    }
}
