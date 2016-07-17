package org.maxur.mserv.properties;

/**
 * The type Property load exception.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>15.07.2016</pre>
 */
public class PropertyLoadException extends IllegalStateException {

    /**
     * Instantiates a new Property load exception.
     *
     * @param message the message
     * @param args    the args
     */
    public PropertyLoadException(final String message, final Object... args) {
        super(message);
    }

    /**
     * Instantiates a new Property load exception.
     *
     * @param cause   the cause
     * @param message the message
     * @param args    the args
     */
    public PropertyLoadException(final Throwable cause, final String message, final Object... args) {
        super(String.format(message, args), cause);
    }
}
