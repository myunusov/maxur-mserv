package org.maxur.ldoc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Binder.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>03.08.2016</pre>
 */
@Target({ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessDomain {

    String name();

    String description();
}
