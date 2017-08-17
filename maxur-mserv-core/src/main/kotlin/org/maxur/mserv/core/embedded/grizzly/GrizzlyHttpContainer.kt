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
import org.glassfish.jersey.server.ApplicationHandler
import org.glassfish.jersey.server.ContainerException
import org.glassfish.jersey.server.ContainerRequest
import org.glassfish.jersey.server.ContainerResponse
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.ServerProperties
import org.glassfish.jersey.server.ServerProperties.REDUCE_CONTEXT_PATH_SLASHES_ENABLED
import org.glassfish.jersey.server.ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR
import org.glassfish.jersey.server.internal.ContainerUtils
import org.glassfish.jersey.server.spi.Container
import org.glassfish.jersey.server.spi.ContainerResponseWriter
import org.glassfish.jersey.server.spi.RequestScopedInitializer
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.OutputStream
import java.net.URI
import java.net.URISyntaxException
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider
import javax.ws.rs.core.Application
import javax.ws.rs.core.SecurityContext

/**
 * Jersey {@code Container} implementation based on Grizzly {@link org.glassfish.grizzly.http.server.HttpHandler}.
 */
class GrizzlyHttpContainer(@Volatile private var appHandler: ApplicationHandler) : HttpHandler(), Container {

    companion object {
        private val log = LoggerFactory.getLogger(GrizzlyHttpContainer::class.java)
    }

    private val RequestTYPE = object : TypeLiteral<Ref<Request>>() {}.type
    private val ResponseTYPE = object : TypeLiteral<Ref<Response>>() {}.type

    /**
     * Cached value of configuration property
     * [org.glassfish.jersey.server.ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR].
     * If `true` method [org.glassfish.grizzly.http.server.Response.setStatus] is used over
     * [org.glassfish.grizzly.http.server.Response.sendError].
     */
    private var configSetStatusOverSendError: Boolean = getProperty(RESPONSE_SET_STATUS_OVER_SEND_ERROR)

    /**
     * Cached value of configuration property
     * [org.glassfish.jersey.server.ServerProperties.REDUCE_CONTEXT_PATH_SLASHES_ENABLED].
     * If `true` method [org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer.getRequestUri]
     * will reduce the of leading context-path slashes to only one.
     */
    private var configReduceContextPathSlashesEnabled: Boolean = getProperty(REDUCE_CONTEXT_PATH_SLASHES_ENABLED)

    private var isDestroyed: Boolean = false

    /**
     * Create a new Grizzly HTTP container.
     * @param application JAX-RS / Jersey application to be deployed on Grizzly HTTP container.
     * @param parentLocator parent HK2 service locator.
     */
    constructor(application: Application, parentLocator: ServiceLocator)
        : this(ApplicationHandler(application, GrizzlyBinder(), parentLocator))

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

        private val emptyCompletionHandler = object : CompletionHandler<Response> {

            /** {@inheritDoc} */
            override fun cancelled() = Unit

            /** {@inheritDoc} */
            override fun failed(throwable: Throwable) = Unit

            /** {@inheritDoc} */
            override fun completed(result: Response) = Unit

            /** {@inheritDoc} */
            override fun updated(result: Response) = Unit
        }

        private val name: String = if (log.isDebugEnabled) {
            "ResponseWriter {id=${UUID.randomUUID()}, grizzlyResponse=${grizzlyResponse.hashCode()}}"
                .also { log.debug("{0} - init", it) }
        } else {
            "ResponseWriter"
        }

        /** {@inheritDoc} */
        override fun toString() = name

        /** {@inheritDoc} */
        override fun commit() {
            try {
                if (grizzlyResponse.isSuspended) {
                    grizzlyResponse.resume()
                }
            } finally {
                log.debug("{0} - commit() called", name)
            }
        }

        /** {@inheritDoc} */
        override fun suspend(
            timeOut: Long,
            timeUnit: TimeUnit,
            timeoutHandler: ContainerResponseWriter.TimeoutHandler?
        ): Boolean = try {
            grizzlyResponse.suspend(timeOut, timeUnit, emptyCompletionHandler) {
                timeoutHandler?.onTimeout(this@ResponseWriter)
                // TODO should we return true in some cases instead?
                // Returning false relies on the fact that the timeoutHandler will resume the response.
                false
            }
            true
        } catch (ex: IllegalStateException) {
            false
        } finally {
            log.debug("{0} - suspend(...) called", name)
        }

        /** {@inheritDoc} */
        @Throws(IllegalStateException::class)
        override fun setSuspendTimeout(timeOut: Long, timeUnit: TimeUnit) {
            try {
                grizzlyResponse.suspendContext.setTimeout(timeOut, timeUnit)
            } finally {
                log.debug("{0} - setTimeout(...) called", name)
            }
        }

        /** {@inheritDoc} */
        @Throws(ContainerException::class)
        override fun writeResponseStatusAndHeaders(
            contentLength: Long,
            responseContext: ContainerResponse
        ): OutputStream =
            try {
                val statusInfo = responseContext.statusInfo
                when {
                    statusInfo.reasonPhrase == null -> grizzlyResponse.status = statusInfo.statusCode
                    else -> grizzlyResponse.setStatus(statusInfo.statusCode, statusInfo.reasonPhrase)
                }
                grizzlyResponse.contentLengthLong = contentLength
                for ((key, values) in responseContext.stringHeaders) {
                    for (value in values) {
                        grizzlyResponse.addHeader(key, value)
                    }
                }
                grizzlyResponse.outputStream
            } finally {
                log.debug("{0} - writeResponseStatusAndHeaders() called", name)
            }

        /** {@inheritDoc} */
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

        /** {@inheritDoc} */
        override fun enableResponseBuffering() = true

        /** {@inheritDoc} */
        private fun rethrow(error: Throwable) {
            when (error) {
                is RuntimeException -> throw error
                else -> throw ContainerException(error)
            }
        }
    }

    /** {@inheritDoc} */
    override fun start() {
        super.start()
        appHandler.onStartup(this)
    }

    /** {@inheritDoc} */
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
            appHandler.handle(requestContext)
        } finally {
            log.debug("GrizzlyHttpContainer.service(...) finished")
        }
    }

    /** {@inheritDoc} */
    override fun getConfiguration() = appHandler.configuration!!

    /** {@inheritDoc} */
    override fun reload() {
        reload(appHandler.configuration)
    }

    /** {@inheritDoc} */
    override fun reload(configuration: ResourceConfig) {
        appHandler.onShutdown(this)
        appHandler = ApplicationHandler(configuration, GrizzlyBinder())
        appHandler.onReload(this)
        appHandler.onStartup(this)
        configSetStatusOverSendError = getProperty(RESPONSE_SET_STATUS_OVER_SEND_ERROR)
        configReduceContextPathSlashesEnabled = getProperty(REDUCE_CONTEXT_PATH_SLASHES_ENABLED)
    }

    /** {@inheritDoc} */
    override fun getApplicationHandler() = if (isDestroyed) null else appHandler

    /** {@inheritDoc} */
    override fun destroy() {
        super.destroy()
        appHandler.onShutdown(this)
        isDestroyed = true
    }

    private fun getSecurityContext(request: Request): SecurityContext = object : SecurityContext {
        /** {@inheritDoc} */
        override fun isUserInRole(role: String) = false
        /** {@inheritDoc} */
        override fun isSecure() = request.isSecure
        /** {@inheritDoc} */
        override fun getUserPrincipal() = request.userPrincipal
        /** {@inheritDoc} */
        override fun getAuthenticationScheme() = request.authType
    }

    private fun getBaseUri(request: Request): URI = try {
        uri(request, getBasePath(request))
    } catch (ex: URISyntaxException) {
        throw IllegalArgumentException(ex)
    }

    private fun getBasePath(request: Request): String {
        val contextPath = request.contextPath
        return when {
            contextPath.isNullOrEmpty() -> "/"
            contextPath.endsWith('/').not() -> contextPath + "/"
            else -> contextPath
        }
    }

    private fun getRequestUri(request: Request): URI = try {
        val serverAddress = uri(request).toString()
        var uri = if (configReduceContextPathSlashesEnabled && !request.contextPath.isNullOrEmpty()) {
            ContainerUtils.reduceLeadingSlashes(request.requestURI)
        } else {
            request.requestURI
        }
        val queryString = request.queryString
        if (queryString != null) {
            uri = uri + "?" + ContainerUtils.encodeUnsafeCharacters(queryString)
        }
        URI(serverAddress + uri)
    } catch (ex: URISyntaxException) {
        throw IllegalArgumentException(ex)
    }

    private fun uri(request: Request, basePath: String? = null): URI = URI(
        request.scheme, null, request.serverName, request.serverPort, basePath, null, null
    )

    private fun getProperty(key: String) = ServerProperties.getValue(
        configuration.properties, key, false, Boolean::class.javaObjectType
    )
}

/**
 * Grizzly container {@link PropertiesDelegate properties delegate}.
 */
class GrizzlyRequestPropertiesDelegate(private val request: Request) : PropertiesDelegate {
    /** {@inheritDoc} */
    override fun getProperty(name: String): Any? = request.getAttribute(name)

    /** {@inheritDoc} */
    override fun getPropertyNames(): Collection<String>? = request.attributeNames

    /** {@inheritDoc} */
    override fun setProperty(name: String, value: Any) = request.setAttribute(name, value)

    /** {@inheritDoc} */
    override fun removeProperty(name: String) = request.removeAttribute(name)
}