package org.maxur.mserv.core.embedded.grizzly

import org.glassfish.grizzly.Grizzly
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.server.Request
import org.glassfish.grizzly.http.server.Response
import org.glassfish.grizzly.http.server.StaticHttpHandlerBase
import org.glassfish.grizzly.http.util.Header
import org.glassfish.grizzly.http.util.HttpStatus
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

abstract class AbstractStaticHttpHandler: StaticHttpHandlerBase()   {

    companion object {
        val log : Logger = Grizzly.logger(StaticHttpHandlerBase::class.java)
    }
    
    protected fun fine(msg: String) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, msg)
        }
    }

    /**
     *  If it's not HTTP GET - return method is not supported status
     */
    protected fun returnMethodIsNotAllowed(resource: String, request: Request, response: Response) {
        fine("File found $resource, but HTTP method ${request.method} is not allowed")
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405)
        response.setHeader(Header.Allow, "GET")
    }

    /**
     * Redirect to the url
     */
    protected fun redirectTo(response: Response, url: String) {
        response.setStatus(HttpStatus.MOVED_PERMANENTLY_301)
        response.setHeader(Header.Location, response.encodeRedirectURL(url))
    }

    protected fun processFile(request: Request, result: File, response: Response) {
        when {
            Method.GET != request.method ->
                returnMethodIsNotAllowed(result.name, request, response)
            else -> {
                pickupContentType(response, result.path)
                addToFileCache(request, response, result)
                sendFile(response, result)
            }
        }
    }

}

