package org.maxur.mserv.core.embedded.properties

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.jaxrs.config.BeanConfig
import io.swagger.jaxrs.listing.ApiListingResource
import org.maxur.mserv.core.domain.Path
import org.maxur.mserv.core.domain.Resource
import org.maxur.mserv.core.rest.RestResourceConfig
import java.net.URI

/**
 * The Web Application parameters.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>12.06.2017</pre>
 */
class WebAppProperties(
        @JsonProperty("url") val url: URI,
        @JsonProperty("rest", required = false) val rest: RestService?,
        @JsonProperty("static-content", required = false) val staticContent: Array<StaticContent>?,
        @JsonProperty("with-hal-browser", required = false) val withHalBrowser: Boolean = false,
        @JsonProperty("with-swagger-ui", required = false) val withSwaggerUi: Boolean = false
) {

    val restPath: Path = rest?.path ?: Path("api")

    companion object {
        val SWAGGER_URL = Resource("/META-INF/resources/webjars/swagger-ui/").subfolder ?:
                throw IllegalStateException("Swagger UI is not found in class path")

        val HAL_URL = Resource("/META-INF/resources/webjars/hal-browser/").subfolder ?:
                throw IllegalStateException("HAL Browser is not found in class path")

    }

    fun staticContent(restConfig: RestResourceConfig): ArrayList<StaticContent> {
        val result = ArrayList<StaticContent>()

        if (staticContent != null)
            result.addAll(staticContent)

        if (withHalBrowser) {
            result.add(halContent())
        }
        if (withSwaggerUi) {
            result.add(swaggerContent())
            initSwagger(restConfig.packages, rest!!.path, url)
            restConfig.resources(ApiListingResource::class.java.`package`.name)
        }
        return result
    }

    private fun swaggerContent(): StaticContent {
        return StaticContent(
                Path("docs"),
                arrayOf(SWAGGER_URL),
                "index.html",
                "index.html?url=/api/swagger.json"
        )
    }

    private fun halContent(): StaticContent {
        return StaticContent(
                Path("hal"),
                arrayOf(HAL_URL),
                "browser.html",
                "#/api/service"
        )
    }

    private fun initSwagger(packages: MutableList<String>, path: Path, uri: URI) {
        val config = BeanConfig()
        config.basePath = "/" + path.asString
        config.host = "${uri.host}:${uri.port}"
        config.resourcePackage = packages.joinToString(",")
        config.scan = true
    }

}

