@file:Suppress("unused")

package org.maxur.mserv.core.embedded.grizzly

import jersey.repackaged.com.google.common.util.concurrent.ThreadFactoryBuilder
import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.grizzly.http.server.NetworkListener
import org.glassfish.grizzly.http.server.ServerConfiguration
import org.glassfish.grizzly.ssl.SSLEngineConfigurator
import org.glassfish.jersey.process.JerseyProcessingUncaughtExceptionHandler
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.spi.Container
import org.maxur.mserv.core.domain.BaseService
import org.maxur.mserv.core.domain.Holder
import org.maxur.mserv.core.embedded.EmbeddedService
import org.maxur.mserv.core.embedded.EmbeddedServiceFactory
import org.maxur.mserv.core.embedded.WebAppConfig
import org.maxur.mserv.core.embedded.WebEntries
import org.maxur.mserv.core.embedded.WebServer
import org.maxur.mserv.core.embedded.properties.StaticContent
import org.maxur.mserv.core.embedded.properties.WebAppProperties
import org.maxur.mserv.core.kotlin.Locator
import org.maxur.mserv.core.rest.RestResourceConfig
import org.slf4j.bridge.SLF4JBridgeHandler
import java.net.URI
import javax.inject.Inject

/**
 * @author myunusov
 * @version 1.0
 * @since <pre>24.06.2017</pre>
 */
class WebServerGrizzlyFactoryImpl @Inject constructor(
    /** The Service locator. */
    val locator: Locator) : EmbeddedServiceFactory() {

    companion object {
        init {
            SLF4JBridgeHandler.removeHandlersForRootLogger()
            SLF4JBridgeHandler.install()
        }
    }

    /** Create new WebServer as EmbeddedService */
    override fun make(properties: Holder<Any>): EmbeddedService? {
        val webAppProperties: WebAppProperties = properties.get(locator)!!

        val restConfig = webAppProperties.rest?.let {
            restConfig(it.name)
        } ?: object : RestResourceConfig() {
            init {
                applicationName = name
            }
        }
        val staticContent = webAppProperties.staticContent(restConfig)

        val config = WebAppConfig(
            webAppProperties.url,
            webAppProperties.restPath,
            staticContent,
            restConfig
        )
        return WebServerGrizzlyImpl(config, locator)
    }

    private fun restConfig(name: String?): RestResourceConfig =
        locator.service(RestResourceConfig::class, name) ?:
            resourceConfigNotFoundError(locator, name ?: "undefined")

    private fun <T> resourceConfigNotFoundError(locator: Locator, name: String): T {
        val list = locator.names(ResourceConfig::class)
        throw IllegalStateException(
            "Resource Config '$name' is not supported. Try one from this list: $list or create one"
        )
    }
}

/** The Web Service (Grizzly Implementation) */
open class WebServerGrizzlyImpl(private val config: WebAppConfig, locator: Locator) : BaseService(locator), WebServer {

    private fun ServerConfiguration.title(): String = "$name '$httpServerName-$httpServerVersion'"

    private val httpServer: HttpServer = httpServer()

    /** The Base API uri */
    override val baseUri: URI get() = config.url

    override var name: String = "Unknown web service"
        get() = httpServer.serverConfiguration.title()

    override fun launch() {
        httpServer.start()
    }

    override fun shutdown() {
        httpServer.shutdownNow()
    }

    private fun httpServer(): HttpServer {
        val listener = networkListener(config.url, false, null)
        val server = createHttpServer(listener)
        server.serverConfiguration.addHttpHandler(makeDynamicHandler(), "/${config.restPath.contextPath}")
        config.staticContent.forEach {
            server.serverConfiguration.addHttpHandler(makeStaticHandler(it), "/${it.path.contextPath}")
        }
        return server
    }

    private fun makeDynamicHandler() =
        GrizzlyHttpContainer(config.resourceConfig, locator.implementation())

    private fun makeStaticHandler(content: StaticContent) = StaticHttpHandler(content)

    /** Web entries. */
    override fun entries(): WebEntries {
        val cfg = httpServer.serverConfiguration
        val entries = WebEntries(config.url)
        cfg.httpHandlersWithMapping.forEach { (_, regs) ->
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

