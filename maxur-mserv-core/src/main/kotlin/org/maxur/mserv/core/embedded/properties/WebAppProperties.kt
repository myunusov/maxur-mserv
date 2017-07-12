package org.maxur.mserv.core.embedded.properties

import com.fasterxml.jackson.annotation.JsonProperty
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
        @JsonProperty("static-content", required = false) staticContent: Array<StaticContent>?,
        @JsonProperty("with-hal-browser", required = false) val withHalBrowser: Boolean = false,
        @JsonProperty("with-swagger-ui", required = false) val withSwaggerUi: Boolean = false
) {
    val staticContent: MutableList<StaticContent> =
            if (staticContent != null)
                mutableListOf(*staticContent)
            else
                ArrayList()
}

