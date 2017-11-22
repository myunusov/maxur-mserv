package org.maxur.mserv.core.rest

import org.glassfish.jersey.server.monitoring.ApplicationEvent
import org.glassfish.jersey.server.monitoring.ApplicationEventListener
import org.glassfish.jersey.server.monitoring.RequestEvent
import org.glassfish.jersey.server.monitoring.RequestEventListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * The type Service event listener.

 * @author Maxim Yunusov
 * *
 * @version 1.0
 * *
 * @since <pre>9/24/2015</pre>
 */
class ServiceEventListener(private val prefix: String) : ApplicationEventListener {

    companion object {
        val log: Logger = LoggerFactory.getLogger(ServiceRequestEventListener::class.java)
    }

    @Volatile private var requestCnt = 0

    override fun onEvent(event: ApplicationEvent) {
        when (event.type) {
            ApplicationEvent.Type.INITIALIZATION_FINISHED -> log.debug("Application "
                + event.resourceConfig.applicationName
                + " was initialized.")
            ApplicationEvent.Type.DESTROY_FINISHED -> log.debug("Application "
                + event.resourceConfig.applicationName + " destroyed.")
            else -> {
                //ignore
            }
        }
    }

    override fun onRequest(requestEvent: RequestEvent): RequestEventListener {
        requestCnt++
        log.debug("Request $requestCnt started.")
        val uriInfo = requestEvent.uriInfo
        if (uriInfo.path.startsWith(prefix)) {
            log.info("Request: " + uriInfo.requestUri)
        }
        // return the listener instance that will handle this request.
        return ServiceRequestEventListener(requestCnt)
    }
}
