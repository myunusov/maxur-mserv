package org.maxur.mserv.core.annotation;

import java.lang.annotation.*;

/**
 * The interface Key.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>30.07.2016</pre>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Key {

    /**
     * Value string.
     *
     * @return the string
     */
    String value() default "";
}
