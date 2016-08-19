package org.maxur.mserv.config;

/**
 * The type Property load exception.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>15.07.2016</pre>
 */
public class ConfigLoadException extends IllegalStateException {

    /**
     * Instantiates a new Property load exception.
     *
     * @param message the message
     * @param args    the args
     */
    public ConfigLoadException(final String message, final Object... args) {
        super(String.format(message, args));
    }

    /**
     * Instantiates a new Property load exception.
     *
     * @param cause   the cause
     * @param message the message
     * @param args    the args
     */
    public ConfigLoadException(final Throwable cause, final String message, final Object... args) {
        super(String.format(message, args), cause);
    }
}
