package org.maxur.mserv.bus.guava;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * The type Event bus logging handler.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>7/28/2016</pre>
 */
@Slf4j
final class EventBusLoggingHandler implements SubscriberExceptionHandler {

    @Override
    public void handleException(final Throwable exception, final SubscriberExceptionContext context) {
        log.warn(message(context));
        log.debug(message(context), exception);
    }

    private static String message(SubscriberExceptionContext context) {
        Method method = context.getSubscriberMethod();
        return String.format(
            "Exception thrown by subscriber method %s(%s) on subscriber %s when dispatching event: %s",
            method.getName(),
            method.getParameterTypes()[0].getName(),
            context.getSubscriber(),
            context.getEvent()
        );
    }
}


