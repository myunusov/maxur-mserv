package org.maxur.mserv.events;

import java.lang.reflect.InvocationTargetException;

/**
 * The interface Service observer.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>15.07.2016</pre>
 */
public interface ServiceObserver {

    /**
     * The constant EVENT_HANDLER_METHOD_NAME.
     */
    String EVENT_HANDLER_METHOD_NAME = "on";

    /**
     * Apply.
     *
     * @param event the event
     */
    default void apply(final Event event) {
        try {
            getClass()
                    .getMethod(EVENT_HANDLER_METHOD_NAME, event.getClass())
                    .invoke(this, event);
        } catch (NoSuchMethodException e) {
            // IGNORE
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            } else {
                throw new IllegalStateException(e.getCause());
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Defaut service observer.
     *
     * @return the service observer
     */
    static ServiceObserver defaut() {
        return new ServiceObserver() {
            @Override
            public void apply(final Event event) {
                System.out.println(event);
            }
        };
    }
}
