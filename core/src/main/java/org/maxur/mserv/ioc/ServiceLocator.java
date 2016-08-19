package org.maxur.mserv.ioc;

import java.lang.annotation.Annotation;

/**
 * The interface Service locator.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/28/2016</pre>
 */
public interface ServiceLocator {

    /**
     * Returns Bean by descriptions.
     *
     * @param <T>         the type of Bean
     * @param aClass      the a class of Bean
     * @param annotations the annotations of Bean
     * @return the Bean
     */
    <T> T bean(Class<T> aClass, Annotation... annotations);

    /**
     * Bean t.
     *
     * @param <T>    the type parameter
     * @param aClass the a class
     * @param name   the name
     * @return the t
     */
    <T> T bean(Class<T> aClass, String name);

}
