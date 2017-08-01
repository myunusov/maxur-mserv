@file:Suppress("unused")

package org.maxur.mserv.core.embedded.properties

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

data class StaticContent(
        @JsonProperty("path") val path: Path,
        @JsonProperty("roots") val roots: Array<URI>,
        @JsonProperty("default-page", required = false) val page: String? = "index.html",
        @JsonProperty("start-url", required = false) val startUrl: String? = ""
)
