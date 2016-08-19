package org.maxur.mserv.ioc.hk2;

import lombok.extern.slf4j.Slf4j;
import org.maxur.mserv.ioc.ServiceLocator;

import javax.inject.Inject;
import java.lang.annotation.Annotation;

/**
 * The type Service locator hk 2.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/28/2016</pre>
 */
@Slf4j
public class ServiceLocatorHk2Impl implements ServiceLocator {

    private final org.glassfish.hk2.api.ServiceLocator serviceLocator;

    @Override
    public <T> T bean(final Class<T> aClass, final Annotation... annotations) {
        return serviceLocator.getService(aClass, annotations);
    }

    @Override
    public <T> T bean(final Class<T> aClass, final String name) {
        return serviceLocator.getService(aClass, name);
    }

    /**
     * Instantiates a new Service locator hk 2.
     *
     * @param serviceLocator the service locator
     */
    @Inject
    public ServiceLocatorHk2Impl(org.glassfish.hk2.api.ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

}
