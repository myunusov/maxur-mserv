package org.maxur.mserv.core.embedded.grizzly

import org.glassfish.grizzly.CompletionHandler
import org.glassfish.grizzly.http.server.HttpHandler
import org.glassfish.grizzly.http.server.Request
import org.glassfish.grizzly.http.server.Response
import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.hk2.api.TypeLiteral
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer
import org.glassfish.jersey.grizzly2.httpserver.internal.LocalizationMessages
import org.glassfish.jersey.internal.PropertiesDelegate
import org.glassfish.jersey.internal.inject.ReferencingFactory
import org.glassfish.jersey.internal.util.collection.Ref
import org.glassfish.jersey.process.internal.RequestScoped
import org.glassfish.jersey.server.*
import org.glassfish.jersey.server.internal.ContainerUtils
import org.glassfish.jersey.server.spi.Container
import org.glassfish.jersey.server.spi.ContainerResponseWriter
import org.glassfish.jersey.server.spi.RequestScopedInitializer
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.OutputStream
import java.net.URI
import java.net.URISyntaxException
import java.security.Principal
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider
import javax.ws.rs.core.Application
import javax.ws.rs.core.SecurityContext

class GrizzlyHttpContainer(@Volatile private var appHandler: ApplicationHandler?): HttpHandler(), Container {

    companion object {
        val log: org.slf4j.Logger = LoggerFactory.getLogger(GrizzlyHttpContainer::class.java)
    }
    private val RequestTYPE = object : TypeLiteral<Ref<Request>>() {}.type
    private val ResponseTYPE = object : TypeLiteral<Ref<Response>>() {}.type

     /**
     * Cached value of configuration property
     * [org.glassfish.jersey.server.ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR].
     * If `true` method [org.glassfish.grizzly.http.server.Response.setStatus] is used over
     * [org.glassfish.grizzly.http.server.Response.sendError].
     */
    private var configSetStatusOverSendError: Boolean = false

    /**
     * Cached value of configuration property
     * [org.glassfish.jersey.server.ServerProperties.REDUCE_CONTEXT_PATH_SLASHES_ENABLED].
     * If `true` method [org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer.getRequestUri]
     * will reduce the of leading context-path slashes to only one.
     */
    private var configReduceContextPathSlashesEnabled: Boolean = false


    /**
     * Create a new Grizzly HTTP container.

     * @param application   JAX-RS / Jersey application to be deployed on Grizzly HTTP container.
     * *
     * @param parentLocator parent HK2 service locator.
     */
    constructor(application: Application, parentLocator: ServiceLocator)
            : this(ApplicationHandler(application, GrizzlyBinder(), parentLocator))  {
        cacheConfigSetStatusOverSendError()
        cacheConfigEnableLeadingContextPathSlashes()
    }

    /**
     * Referencing factory for Grizzly request.
     */
    private class GrizzlyRequestReferencingFactory @Inject
    constructor(referenceFactory: Provider<Ref<Request>>) : ReferencingFactory<Request>(referenceFactory)

    /**
     * Referencing factory for Grizzly response.
     */
    private class GrizzlyResponseReferencingFactory @Inject
    constructor(referenceFactory: Provider<Ref<Response>>) : ReferencingFactory<Response>(referenceFactory)

    /**
     * An internal binder to enable Grizzly HTTP container specific types injection.
     *
     *
     * This binder allows to inject underlying Grizzly HTTP request and response instances.
     * Note that since Grizzly `Request` class is not proxiable as it does not expose an empty constructor,
     * the injection of Grizzly request instance into singleton JAX-RS and Jersey providers is only supported via
     * [injection provider][javax.inject.Provider].
     */
    internal class GrizzlyBinder : AbstractBinder() {

        override fun configure() {
            bindFactory(GrizzlyRequestReferencingFactory::class.java).to(Request::class.java)
                    .proxy(false).`in`(RequestScoped::class.java)
            bindFactory(ReferencingFactory.referenceFactory<Request>()).to(object : TypeLiteral<Ref<Request>>() {
            }).`in`(RequestScoped::class.java)

            bindFactory(GrizzlyResponseReferencingFactory::class.java).to(Response::class.java)
                    .proxy(true).proxyForSameScope(false).`in`(RequestScoped::class.java)
            bindFactory(ReferencingFactory.referenceFactory<Response>()).to(object : TypeLiteral<Ref<Response>>() {
            }).`in`(RequestScoped::class.java)
        }
    }

    private class ResponseWriter internal constructor(
            private val grizzlyResponse: Response,
            private val configSetStatusOverSendError: Boolean
    ) : ContainerResponseWriter {

        private val EMPTY_COMPLETION_HANDLER = object : CompletionHandler<Response> {
            override fun cancelled() = Unit
            override fun failed(throwable: Throwable) = Unit
            override fun completed(result: Response) = Unit
            override fun updated(result: Response) = Unit
        }

        private val name: String

        init {
            if (log.isDebugEnabled()) {
                this.name = "ResponseWriter {id=${UUID.randomUUID()}, grizzlyResponse=${grizzlyResponse.hashCode()}}"
                log.debug("{0} - init", name)
            } else {
                this.name = "ResponseWriter"
            }
        }

        override fun toString(): String {
            return name
        }

        override fun commit() {
            try {
                if (grizzlyResponse.isSuspended) {
                    grizzlyResponse.resume()
                }
            } finally {
                log.debug("{0} - commit() called", name)
            }
        }

        override fun suspend(
                timeOut: Long,
                timeUnit: TimeUnit,
                timeoutHandler: ContainerResponseWriter.TimeoutHandler?
        ): Boolean {
            try {
                grizzlyResponse.suspend(timeOut, timeUnit, EMPTY_COMPLETION_HANDLER
                ) {
                    timeoutHandler?.onTimeout(this@ResponseWriter)

                    // TODO should we return true in some cases instead?
                    // Returning false relies on the fact that the timeoutHandler will resume the response.
                    false
                }
                return true
            } catch (ex: IllegalStateException) {
                return false
            } finally {
                log.debug("{0} - suspend(...) called", name)
            }
        }

        @Throws(IllegalStateException::class)
        override fun setSuspendTimeout(timeOut: Long, timeUnit: TimeUnit) {
            try {
                grizzlyResponse.suspendContext.setTimeout(timeOut, timeUnit)
            } finally {
                log.debug("{0} - setTimeout(...) called", name)
            }
        }

        @Throws(ContainerException::class)
        override fun writeResponseStatusAndHeaders(contentLength: Long,
                                                   context: ContainerResponse): OutputStream {
            try {
                val statusInfo = context.statusInfo
                if (statusInfo.reasonPhrase == null) {
                    grizzlyResponse.status = statusInfo.statusCode
                } else {
                    grizzlyResponse.setStatus(statusInfo.statusCode, statusInfo.reasonPhrase)
                }

                grizzlyResponse.contentLengthLong = contentLength

                for ((key, value) in context.stringHeaders) {
                    for (v in value) {
                        grizzlyResponse.addHeader(key, v)
                    }
                }

                return grizzlyResponse.outputStream
            } finally {
                log.debug("{0} - writeResponseStatusAndHeaders() called", name)
            }
        }

        override fun failure(error: Throwable) {
            try {
                if (!grizzlyResponse.isCommitted) {
                    try {
                        if (configSetStatusOverSendError) {
                            grizzlyResponse.reset()
                            grizzlyResponse.setStatus(500, "Request failed.")
                        } else {
                            grizzlyResponse.sendError(500, "Request failed.")
                        }
                    } catch (ex: IllegalStateException) {
                        // a race condition externally committing the response can still occur...
                        log.trace("Unable to reset failed response.", ex)
                    } catch (ex: IOException) {
                        throw ContainerException(
                                LocalizationMessages.EXCEPTION_SENDING_ERROR_RESPONSE(500, "Request failed."),
                                ex)
                    }

                }
            } finally {
                log.debug("{0} - failure(...) called", name)
                rethrow(error)
            }
        }

        override fun enableResponseBuffering(): Boolean {
            return true
        }

        /**
         * Rethrow the original exception as required by JAX-RS, 3.3.4

         * @param error throwable to be re-thrown
         */
        private fun rethrow(error: Throwable) {
            if (error is RuntimeException) {
                throw error
            } else {
                throw ContainerException(error)
            }
        }
    }

    override fun start() {
        super.start()
        appHandler!!.onStartup(this)
    }

    override fun service(request: Request, response: Response) {
        val responseWriter = ResponseWriter(response, configSetStatusOverSendError)
        try {
            log.debug("GrizzlyHttpContainer.service(...) started")
            val baseUri = getBaseUri(request)
            val requestUri = getRequestUri(request)
            val requestContext = ContainerRequest(baseUri,
                    requestUri, request.method.methodString,
                    getSecurityContext(request), GrizzlyRequestPropertiesDelegate(request))
            requestContext.entityStream = request.inputStream
            for (headerName in request.headerNames) {
                requestContext.headers(headerName, request.getHeaders(headerName))
            }
            requestContext.setWriter(responseWriter)

            requestContext.requestScopedInitializer = RequestScopedInitializer { locator ->
                locator.getService<Ref<Request>>(RequestTYPE).set(request)
                locator.getService<Ref<Response>>(ResponseTYPE).set(response)
            }
            appHandler!!.handle(requestContext)
        } finally {
            log.debug("GrizzlyHttpContainer.service(...) finished")
        }
    }

    private fun containsContextPath(request: Request): Boolean {
        return request.contextPath != null && request.contextPath.isNotEmpty()
    }

    override fun getConfiguration(): ResourceConfig {
        return appHandler!!.configuration
    }

    override fun reload() {
        reload(appHandler!!.configuration)
    }

    override fun reload(configuration: ResourceConfig) {
        appHandler!!.onShutdown(this)

        appHandler = ApplicationHandler(configuration, GrizzlyBinder())
        appHandler!!.onReload(this)
        appHandler!!.onStartup(this)
        cacheConfigSetStatusOverSendError()
        cacheConfigEnableLeadingContextPathSlashes()
    }

    override fun getApplicationHandler(): ApplicationHandler {
        return appHandler!!
    }

    override fun destroy() {
        super.destroy()
        appHandler!!.onShutdown(this)
        appHandler = null
    }

    private fun getSecurityContext(request: Request): SecurityContext {
        return object : SecurityContext {

            override fun isUserInRole(role: String): Boolean {
                return false
            }

            override fun isSecure(): Boolean {
                return request.isSecure
            }

            override fun getUserPrincipal(): Principal {
                return request.userPrincipal
            }

            override fun getAuthenticationScheme(): String {
                return request.authType
            }
        }
    }

    private fun getBaseUri(request: Request): URI {
        try {
            return URI(request.scheme, null, request.serverName,
                    request.serverPort, getBasePath(request), null, null)
        } catch (ex: URISyntaxException) {
            throw IllegalArgumentException(ex)
        }

    }

    private fun getBasePath(request: Request): String {
        val contextPath = request.contextPath

        if (contextPath == null || contextPath.isEmpty()) {
            return "/"
        } else if (contextPath[contextPath.length - 1] != '/') {
            return contextPath + "/"
        } else {
            return contextPath
        }
    }

    private fun getRequestUri(request: Request): URI {
        try {
            val serverAddress = getServerAddress(request)

            var uri: String
            if (configReduceContextPathSlashesEnabled && containsContextPath(request)) {
                uri = ContainerUtils.reduceLeadingSlashes(request.requestURI)
            } else {
                uri = request.requestURI
            }

            val queryString = request.queryString
            if (queryString != null) {
                uri = uri + "?" + ContainerUtils.encodeUnsafeCharacters(queryString)
            }

            return URI(serverAddress + uri)
        } catch (ex: URISyntaxException) {
            throw IllegalArgumentException(ex)
        }

    }

    @Throws(URISyntaxException::class)
    private fun getServerAddress(request: Request): String {
        return URI(request.scheme, null, request.serverName, request.serverPort, null, null, null).toString()
    }

    /**
     * The method reads and caches value of configuration property
     * [org.glassfish.jersey.server.ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR] for future purposes.
     */
    private fun cacheConfigSetStatusOverSendError() {
        this.configSetStatusOverSendError = ServerProperties.getValue(getConfiguration().properties,
                ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, false, Boolean::class.java)
    }

    /**
     * The method reads and caches value of configuration property
     * [org.glassfish.jersey.server.ServerProperties.REDUCE_CONTEXT_PATH_SLASHES_ENABLED] for future purposes.
     */
    private fun cacheConfigEnableLeadingContextPathSlashes() {
        this.configReduceContextPathSlashesEnabled = ServerProperties.getValue(getConfiguration().properties,
                ServerProperties.REDUCE_CONTEXT_PATH_SLASHES_ENABLED, false, Boolean::class.java)
    }

}


class GrizzlyRequestPropertiesDelegate(private val request: Request) : PropertiesDelegate {
    override fun getProperty(name: String): Any? = request.getAttribute(name)
    override fun getPropertyNames(): Collection<String>? = request.attributeNames
    override fun setProperty(name: String, value: Any) = request.setAttribute(name, value)
    override fun removeProperty(name: String) = request.removeAttribute(name)
}