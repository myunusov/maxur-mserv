package org.maxur.mserv.bus.guava;

import com.google.common.eventbus.EventBus;
import lombok.experimental.Delegate;
import org.maxur.mserv.bus.Bus;
import org.maxur.mserv.core.Event;

/**
 * The type Bus guava.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/28/2016</pre>
 */
public final class BusGuavaImpl implements Bus {

    @Delegate
    private final EventBus eventBus;

    private BusGuavaImpl() {
        this.eventBus = new EventBus(new EventBusLoggingHandler());
    }

    /**
     * Bus bus guava.
     *
     * @return the bus guava
     */
    public static BusGuavaImpl bus() {
        return new BusGuavaImpl();
    }

    @Override
    public void post(final Event event) {
        eventBus.post(event);
    }

    @Override
    public String id() {
        return identifier();
    }
}
