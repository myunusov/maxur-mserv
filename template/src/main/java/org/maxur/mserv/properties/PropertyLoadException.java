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
     * @param s the s
     */
    public PropertyLoadException(final String s) {
        super(s);
    }

    /**
     * Instantiates a new Property load exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public PropertyLoadException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
