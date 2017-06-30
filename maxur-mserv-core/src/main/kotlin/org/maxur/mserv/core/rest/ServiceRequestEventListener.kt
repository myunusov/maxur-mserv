package org.maxur.mserv.core.rest

import org.glassfish.jersey.server.monitoring.RequestEvent
import org.glassfish.jersey.server.monitoring.RequestEventListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * The type Service request event listener.

 * @author Maxim Yunusov
 * *
 * @version 1.0
 * *
 * @since <pre>9/24/2015</pre>
 */
class ServiceRequestEventListener

    internal constructor(private val requestNumber: Int) : RequestEventListener {

    companion object {
        val log: Logger = LoggerFactory.getLogger(ServiceRequestEventListener::class.java)
    }

    private val startTime: Long = System.currentTimeMillis()

    override fun onEvent(event: RequestEvent) {
        when (event.type) {
            RequestEvent.Type.RESOURCE_METHOD_START -> log.debug("Resource method "
                    + event.uriInfo.matchedResourceMethod
                    .httpMethod
                    + " started for request " + requestNumber)
            RequestEvent.Type.FINISHED -> log.debug("Request " + requestNumber
                    + " finished. Processing time "
                    + (System.currentTimeMillis() - startTime) + " ms.")
            else -> {
                //ignore
            }
        }
    }
}
