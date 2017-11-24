@file:Suppress("unused")

package org.maxur.mserv.frame.embedded.properties

import com.fasterxml.jackson.annotation.JsonProperty
import org.maxur.mserv.frame.domain.Path
import java.net.URI

data class StaticContent(
        @JsonProperty("path") val path: Path,
        @JsonProperty("roots") val roots: Array<URI>,
        @JsonProperty("default-page", required = false) val page: String? = "index.html",
        @JsonProperty("start-url", required = false) val startUrl: String? = ""
)
