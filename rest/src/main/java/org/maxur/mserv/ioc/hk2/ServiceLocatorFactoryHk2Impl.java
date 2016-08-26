package org.maxur.mserv.ioc.hk2;

import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.maxur.mserv.ioc.ServiceLocator;

/**
 * The type Service locator factory hk 2.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/28/2016</pre>
 */
public final class ServiceLocatorFactoryHk2Impl {

    private static final String LOCATOR_DEFAULT_NAME = "default";

    private ServiceLocatorFactoryHk2Impl() {
    }

    /**
     * Locator service locator.
     *
     * @param binders HK2 Binders
     * @return the service locator
     */
    public static ServiceLocator locator(final Binder... binders) {
        return locator(LOCATOR_DEFAULT_NAME, binders);
    }

    /**
     * Locator service locator.
     *
     * @param name    the name
     * @param binders HK2 Binders
     * @return the service locator
     */
    public static ServiceLocator locator(final String name, final Binder... binders) {
        return new ServiceLocatorFactoryHk2Impl().newLocator(name, binders);
    }

    private ServiceLocator newLocator(final String name, final Binder... binders) {
        final org.glassfish.hk2.api.ServiceLocator serviceLocator =
            ServiceLocatorUtilities.createAndPopulateServiceLocator(name);
        ServiceLocatorUtilities.bind(serviceLocator, binders);
        return serviceLocator.getService(ServiceLocator.class);
    }


}
