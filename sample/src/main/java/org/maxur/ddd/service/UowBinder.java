package org.maxur.ddd.service;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.maxur.mserv.core.annotation.Binder;

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>8/18/2016</pre>
 */
@Binder
public class UowBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bindFactory(UowReferencingFactory.class)
            .to(Uow.class);
    }

    private static class UowReferencingFactory implements Factory<Uow> {

        @Override
        public Uow provide() {
            return new Uow();
        }

        @Override
        public void dispose(final Uow uow) {
            uow.commit();
        }


    }

}
