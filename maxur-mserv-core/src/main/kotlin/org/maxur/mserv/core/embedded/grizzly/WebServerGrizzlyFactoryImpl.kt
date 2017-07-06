@file:Suppress("unused")

package org.maxur.mserv.core.embedded.grizzly

import io.swagger.jaxrs.config.BeanConfig
import io.swagger.jaxrs.listing.ApiListingResource
import jersey.repackaged.com.google.common.util.concurrent.ThreadFactoryBuilder
import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.grizzly.http.server.NetworkListener
import org.glassfish.grizzly.http.server.ServerConfiguration
import org.glassfish.grizzly.ssl.SSLEngineConfigurator
import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.jersey.process.JerseyProcessingUncaughtExceptionHandler
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.spi.Container
import org.jvnet.hk2.annotations.Service
import org.maxur.mserv.core.Locator
import org.maxur.mserv.core.domain.BaseService
import org.maxur.mserv.core.domain.Holder
import org.maxur.mserv.core.embedded.*
import org.maxur.mserv.core.embedded.properties.Path
import org.maxur.mserv.core.embedded.properties.StaticContent
import org.maxur.mserv.core.embedded.properties.WebAppProperties
import org.maxur.mserv.core.rest.RestResourceConfig
import org.slf4j.bridge.SLF4JBridgeHandler
import java.net.URI
import javax.inject.Inject

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
@Service(name = "Grizzly")
class WebServerGrizzlyFactoryImpl @Inject constructor(val locator: Locator) : EmbeddedServiceFactory() {

    companion object {
        init {
            SLF4JBridgeHandler.removeHandlersForRootLogger()
            SLF4JBridgeHandler.install()
        }
    }

    override fun make(properties: Holder<Any?>): EmbeddedService? {
        // TODO mov this logic to domain
        val webAppProperties: WebAppProperties = properties.get(locator)!!
        val restServiceName = webAppProperties.rest!!.name
        val restConfig: RestResourceConfig = locator.service(RestResourceConfig::class.java, restServiceName) ?:
                return resourceConfigNotFoundError(locator, restServiceName ?: "undefined")

        val staticContent = ArrayList<StaticContent>()

        val config = RestAppConfig(
                webAppProperties.url,
                webAppProperties.rest,
                staticContent,
                restConfig
        )
        staticContent.addAll(webAppProperties.staticContent)
        if (webAppProperties.withHalBrowser) {
            staticContent.add(halContent())
        }
        if (webAppProperties.withSwaggerUi) {
            val doc = swaggerContent()
            staticContent.add(doc)
            initSwagger(restConfig.packages, config)
            restConfig.resources(ApiListingResource::class.java.`package`.name)
        }

        return WebServerGrizzlyImpl(config, locator)
    }

    private fun swaggerContent(): StaticContent {
        return StaticContent(
                arrayOf(URI("classpath:/META-INF/resources/webjars/swagger-ui/3.0.17/")),
                Path("docs"),
                "index.html",
                "/index.html?url=/api/swagger.json"
        )
    }

    private fun halContent(): StaticContent {
        return StaticContent(
                arrayOf(URI("classpath:/META-INF/resources/webjars/hal-browser/3325375/")),
                Path("hal"),
                "browser.html",
                "/#/api/service"
        )
    }

    private fun initSwagger(packages: MutableList<String>, restConfig: RestAppConfig) {
        val config = BeanConfig()
        config.basePath = "/" + restConfig.rest.path.path
        config.host = "${restConfig.url.host}:${restConfig.url.port}"
        config.resourcePackage = packages.joinToString(",")
        config.scan = true
    }

    private fun resourceConfigNotFoundError(locator: Locator, name: String): EmbeddedService? {
        val list = locator.names(ResourceConfig::class.java)
        throw IllegalStateException(
                "Resource Config '$name' is not supported. Try one from this list: $list or create one"
        )
    }

}

open class WebServerGrizzlyImpl(
        private val config: RestAppConfig,
        locator: Locator
) : BaseService(locator), WebServer {

    fun ServerConfiguration.title(): String = "$name '$httpServerName-$httpServerVersion'"

    private val httpServer: HttpServer = httpServer()

    override val baseUri: URI get() = config.url

    override var name: String = "Unknown web service"
        get() = httpServer.serverConfiguration.title()

    override fun launch() {
        httpServer.start()
    }

    override fun shutdown() {
        httpServer.shutdownNow()
    }

    override fun relaunch() {
        httpServer.shutdownNow()
        httpServer.start()
    }
    
    private fun httpServer(): HttpServer {
        val listener = networkListener(config.url, false, null)
        val server = createHttpServer(listener)
        makeDynamicHandler(server.serverConfiguration, config.rest.path)
        makeStaticHandlers(server.serverConfiguration)
        return server
    }

    private fun makeDynamicHandler(serverConfiguration: ServerConfiguration, path: Path) {
        serverConfiguration.addHttpHandler(
                GrizzlyHttpContainer(config.resourceConfig, locator.implementation<ServiceLocator>()),
                "/${path.contextPath}"
        )
    }

    private fun makeStaticHandlers(serverConfiguration: ServerConfiguration) {
        config.staticContent.forEach {
            serverConfiguration.addHttpHandler(
                    CompositeStaticHttpHandler.make(it),
                    "/${it.path.contextPath}"
            )
        }
    }

    override fun entries(): WebEntries {
        val cfg = httpServer.serverConfiguration
        val entries = WebEntries(config.url)
        cfg.httpHandlersWithMapping.forEach {
            (_, regs) ->
            run {
                for (reg in regs) entries.add(
                        reg.contextPath,
                        reg.urlPattern,
                        config.staticContentByPath(reg.contextPath)?.startUrl ?: ""
                )
            }
        }
        return entries
    }

    private fun createHttpServer(listener: NetworkListener): HttpServer {
        val server = HttpServer()
        server.addListener(listener)
        server.serverConfiguration.isPassTraceRequest = true
        server.serverConfiguration.defaultQueryEncoding = org.glassfish.grizzly.utils.Charsets.UTF8_CHARSET
        return server
    }

    private fun networkListener(
            uri: URI,
            secure: Boolean,
            sslEngineConfigurator: SSLEngineConfigurator?
    ): NetworkListener {
        val host = if (uri.host == null) NetworkListener.DEFAULT_NETWORK_HOST else uri.host
        val port = when {
            uri.port == -1 -> if (secure) Container.DEFAULT_HTTPS_PORT else Container.DEFAULT_HTTP_PORT
            else -> uri.port
        }
        val listener = NetworkListener("grizzly", host, port)
        listener.transport.workerThreadPoolConfig.threadFactory = ThreadFactoryBuilder()
                .setNameFormat("grizzly-http-server-%d")
                .setUncaughtExceptionHandler(JerseyProcessingUncaughtExceptionHandler())
                .build()
        listener.isSecure = secure
        if (sslEngineConfigurator != null) {
            listener.setSSLEngineConfig(sslEngineConfigurator)
        }
        return listener
    }


}

