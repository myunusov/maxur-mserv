package org.maxur.mserv.ioc;

import org.maxur.mserv.config.ConfigResolver;

import java.util.List;

/**
 * The interface IoC.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>8/19/2016</pre>
 */
public interface IoC {

    /**
     * Init.
     *
     * @param binders the binders
     */
    void init(List<Class<?>> binders);

    /**
     * Config resolver config resolver.
     *
     * @return the config resolver
     */
    ConfigResolver configResolver();

    /**
     * Instance of class.
     *
     * @param clazz the clazz
     * @return the object
     */
    <T> T instanceOf(Class<T> clazz);

    /**
     * Locator service locator.
     *
     * @return the service locator
     */
    ServiceLocator locator();
}
