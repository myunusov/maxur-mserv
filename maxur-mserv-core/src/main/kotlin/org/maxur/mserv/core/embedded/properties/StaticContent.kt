@file:Suppress("unused")

package org.maxur.mserv.core.embedded.properties

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

class StaticContent(
        @JsonProperty("roots")         val roots: Array<URI>,
        @JsonProperty("path")          val path: String,
        @JsonProperty("default-page", required = false) val page: String? = "index.html",
        @JsonProperty("start-url", required = false) val startUrl: String? = ""
) {

    val normalisePath: String = run {
        val ex = path.replace("/{2,}".toRegex(), "/")
        if (ex.endsWith("/")) ex.substring(0, ex.length - 1) else ex
    }
    
    private fun normalizeScheme(scheme: String?): String = scheme ?: "file"

    fun fileContent(): StaticContent =
            StaticContent(roots.filter {it.scheme in arrayOf (null, "file") }.toTypedArray(), path, page)

    fun clContent(): StaticContent =
            StaticContent(roots.filter {it.scheme == "classpath" }.toTypedArray(), path, page)


}