package org.maxur.ddd.service;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.maxur.mserv.core.annotation.Observer;
import org.maxur.mserv.core.LifecycleEvent;

/**
 * The type Logger.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/28/2016</pre>
 */
@Slf4j
@Observer
@Service
public class EventLogger {

    /**
     * On Lifecycle Event.
     *
     * @param event the Lifecycle event
     */
    @Subscribe
    public void on(final LifecycleEvent event) {
        log.info(event.message());
    }

}
