package org.maxur.mserv.core.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Observer annotation.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/29/2016</pre>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Observer {
}
