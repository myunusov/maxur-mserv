package org.maxur.mserv.core;

import lombok.Getter;

/**
 * The type Lifecycle event.
 *
 * @param <T> entity type
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/29/2016</pre>
 */
public abstract class LifecycleEvent<T> implements Event {

    @Getter
    private final T entity;

    /**
     * Instantiates a new Lifecycle event.
     *
     * @param entity the entity
     */
    protected LifecycleEvent(final T entity) {
        this.entity = entity;
    }

    /**
     * Message string.
     *
     * @return the string
     */
    public abstract String message();
}
