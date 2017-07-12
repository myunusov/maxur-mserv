@file:Suppress("unused")

package org.maxur.mserv.core.embedded.properties

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

class StaticContent(
        @JsonProperty("path") val path: Path,
        @JsonProperty("roots") val roots: Array<URI>,
        @JsonProperty("default-page", required = false) val page: String? = "index.html",
        @JsonProperty("start-url", required = false) val startUrl: String? = ""
) {

    constructor(path: String, vararg roots: String) : this(Path(path), roots.map { URI.create(it) }.toTypedArray())

    private fun normalizeScheme(scheme: String?): String = scheme ?: "file"

    fun fileContent(): StaticContent =
            StaticContent(path, roots.filter {it.scheme in arrayOf (null, "file") }.toTypedArray(), page)

    fun clContent(): StaticContent =
            StaticContent(path, roots.filter {it.scheme == "classpath" }.toTypedArray(), page)


}