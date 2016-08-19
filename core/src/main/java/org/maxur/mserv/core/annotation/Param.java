package org.maxur.mserv.core.annotation;

import java.lang.annotation.*;

/**
 * The interface Param.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>30.07.2016</pre>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {

    /**
     * Config key.
     *
     * @return the string
     */
    String key();

    /**
     * Optional boolean.
     *
     * @return the boolean
     */
    boolean optional() default false;
}
