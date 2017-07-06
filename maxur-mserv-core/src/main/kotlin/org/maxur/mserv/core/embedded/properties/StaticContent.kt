@file:Suppress("unused")

package org.maxur.mserv.core.embedded.properties

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

class StaticContent(
        @JsonProperty("roots")         val roots: Array<URI>,
        @JsonProperty("path")          val path: Path,
        @JsonProperty("default-page", required = false) val page: String? = "index.html",
        @JsonProperty("start-url", required = false) val startUrl: String? = ""
) {


    private fun normalizeScheme(scheme: String?): String = scheme ?: "file"

    fun fileContent(): StaticContent =
            StaticContent(roots.filter {it.scheme in arrayOf (null, "file") }.toTypedArray(), path, page)

    fun clContent(): StaticContent =
            StaticContent(roots.filter {it.scheme == "classpath" }.toTypedArray(), path, page)


}